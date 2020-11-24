package com.hedera.hashgraph.seven_twenty_one.contract.repository;

import static com.hedera.hashgraph.seven_twenty_one.contract.db.Tables.TRANSACTION_TRANSFER_FROM;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.TransferFromFunctionArguments;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;

public final class TransferFromTransactionRepository
    extends AbstractTransactionRepository<TransferFromFunctionArguments> {

    TransferFromTransactionRepository(
        DSLContext context,
        AddressRepository addressRepository
    ) {
        super(context, addressRepository);
    }

    @Override
    protected BatchBindStep newBatch() throws SQLException {
        return context.batch(
            context
                .insertInto(
                    TRANSACTION_TRANSFER_FROM,
                    TRANSACTION_TRANSFER_FROM.TIMESTAMP,
                    TRANSACTION_TRANSFER_FROM.TO_ADDRESS_ID,
                    TRANSACTION_TRANSFER_FROM.FROM_ADDRESS_ID,
                    TRANSACTION_TRANSFER_FROM.TOKEN_ID
                )
                .values((Long) null, null, null, null)
                .onConflictDoNothing()
        );
    }

    @Override
    public Collection<Address> getAddressList(
        TransferFromFunctionArguments arguments
    ) {
        return List.of(arguments.from, arguments.to);
    }

    @Override
    protected BatchBindStep bind(
        BatchBindStep batch,
        long consensusTimestampNanos,
        TransferFromFunctionArguments arguments
    ) {
        return batch.bind(
            consensusTimestampNanos,
            addressRepository.toAddressId(arguments.from),
            addressRepository.toAddressId(arguments.to),
            arguments.id.value
        );
    }
}
