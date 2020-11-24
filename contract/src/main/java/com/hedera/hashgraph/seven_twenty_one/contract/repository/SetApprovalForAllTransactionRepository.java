package com.hedera.hashgraph.seven_twenty_one.contract.repository;

import static com.hedera.hashgraph.seven_twenty_one.contract.db.Tables.TRANSACTION_SET_APPROVAL_FOR_ALL;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.SetApprovalForAllFunctionArguments;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;

public final class SetApprovalForAllTransactionRepository
    extends AbstractTransactionRepository<SetApprovalForAllFunctionArguments> {

    SetApprovalForAllTransactionRepository(
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
                    TRANSACTION_SET_APPROVAL_FOR_ALL,
                    TRANSACTION_SET_APPROVAL_FOR_ALL.TIMESTAMP,
                    TRANSACTION_SET_APPROVAL_FOR_ALL.OPERATOR_ADDRESS_ID,
                    TRANSACTION_SET_APPROVAL_FOR_ALL.APPROVED
                )
                .values((Long) null, null, null)
                .onConflictDoNothing()
        );
    }

    @Override
    public Collection<Address> getAddressList(
        SetApprovalForAllFunctionArguments arguments
    ) {
        return List.of(arguments.operator);
    }

    @Override
    protected BatchBindStep bind(
        BatchBindStep batch,
        long consensusTimestampNanos,
        SetApprovalForAllFunctionArguments arguments
    ) {
        return batch.bind(
            consensusTimestampNanos,
            addressRepository.toAddressId(arguments.operator),
            arguments.approved
        );
    }
}
