package com.hedera.hashgraph.seven_twenty_one.contract.repository;

import static com.hedera.hashgraph.seven_twenty_one.contract.db.Tables.ADDRESS_TRANSACTION;
import static com.hedera.hashgraph.seven_twenty_one.contract.db.Tables.TRANSACTION;

import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.Status;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;

public class TransactionRepository {

    private final DSLContext context;

    private final AddressRepository addressRepository;

    private final Map<FunctionBody.DataCase, AbstractTransactionRepository<?>> repositoryMap;

    private final Set<FunctionBody.DataCase> repositoriesWithData = new HashSet<>();

    @Nullable
    private BatchBindStep transactionBatch;

    @Nullable
    private BatchBindStep addressBatch;

    public TransactionRepository(
        DSLContext context,
        AddressRepository addressRepository
    ) {
        this.context = context;
        this.addressRepository = addressRepository;
        this.repositoryMap =
            Map.ofEntries(
                Map.entry(
                    FunctionBody.DataCase.CONSTRUCT,
                    new ConstructorTransactionRepository(
                        context,
                        addressRepository
                    )
                ),
                Map.entry(
                    FunctionBody.DataCase.APPROVE,
                    new ApproveTransactionRepository(context, addressRepository)
                ),
                Map.entry(
                    FunctionBody.DataCase.SETAPPROVALFORALL,
                    new SetApprovalForAllTransactionRepository(
                        context,
                        addressRepository
                    )
                ),
                Map.entry(
                    FunctionBody.DataCase.MINT,
                    new MintTransactionRepository(context, addressRepository)
                ),
                Map.entry(
                    FunctionBody.DataCase.BURN,
                    new BurnTransactionRepository(context, addressRepository)
                ),
                Map.entry(
                    FunctionBody.DataCase.TRANSFERFROM,
                    new TransferFromTransactionRepository(
                        context,
                        addressRepository
                    )
                )
            );
    }

    private BatchBindStep newTransactionBatch() throws SQLException {
        return context.batch(
            context
                .insertInto(
                    TRANSACTION,
                    TRANSACTION.TIMESTAMP,
                    TRANSACTION.FUNCTION,
                    TRANSACTION.CALLER_ADDRESS_ID,
                    TRANSACTION.HEDERA_OPERATOR_ACCOUNT_NUM,
                    TRANSACTION.HEDERA_VALID_START_NANOS,
                    TRANSACTION.STATUS
                )
                .values((Long) null, null, null, null, null, null)
                .onConflictDoNothing()
        );
    }

    private BatchBindStep newAddressBatch() throws SQLException {
        return context.batch(
            context
                .insertInto(
                    ADDRESS_TRANSACTION,
                    ADDRESS_TRANSACTION.TRANSACTION_TIMESTAMP,
                    ADDRESS_TRANSACTION.ADDRESS_ID
                )
                .values((Long) null, null)
                .onConflictDoNothing()
        );
    }

    public <ArgumentsT> void put(
        Instant consensusTimestamp,
        Address caller,
        TransactionId transactionId,
        Status status,
        FunctionBody.DataCase function,
        ArgumentsT functionArguments
    ) throws SQLException {
        if (transactionBatch == null) {
            transactionBatch = newTransactionBatch();
        }

        if (addressBatch == null) {
            addressBatch = newAddressBatch();
        }

        @SuppressWarnings("unchecked")
        var repository = (AbstractTransactionRepository<ArgumentsT>) repositoryMap.get(
            function
        );

        assert transactionBatch != null;
        assert addressBatch != null;

        var consensusTimestampNanos = ChronoUnit.NANOS.between(
            Instant.EPOCH,
            consensusTimestamp
        );

        var validStartNanos = ChronoUnit.NANOS.between(
            Instant.EPOCH,
            transactionId.validStart
        );

        var callerAddressId = addressRepository.toAddressId(caller);

        transactionBatch =
            transactionBatch.bind(
                consensusTimestampNanos,
                function.getNumber(),
                callerAddressId,
                transactionId.accountId.num,
                validStartNanos,
                status.value
            );

        // the <caller> is always associated with the transaction
        addressBatch =
            addressBatch.bind(consensusTimestampNanos, callerAddressId);

        if (repository != null) {
            repository.put(consensusTimestampNanos, functionArguments);

            for (var address : repository.getAddressList(functionArguments)) {
                assert addressBatch != null;

                addressBatch =
                    addressBatch.bind(
                        consensusTimestampNanos,
                        addressRepository.toAddressId(address)
                    );
            }

            repositoriesWithData.add(function);
        }
    }

    public void execute() {
        if (transactionBatch != null) {
            transactionBatch.execute();
            transactionBatch = null;
        }

        if (addressBatch != null) {
            addressBatch.execute();
            addressBatch = null;
        }

        for (var function : repositoriesWithData) {
            var repository = repositoryMap.get(function);

            if (repository != null) {
                repository.execute();
            }
        }

        repositoriesWithData.clear();
    }
}
