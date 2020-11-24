package com.hedera.hashgraph.seven_twenty_one.contract.repository;

import static com.hedera.hashgraph.seven_twenty_one.contract.db.Tables.TRANSACTION_APPROVE;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.ApproveFunctionArguments;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;

public final class ApproveTransactionRepository
    extends AbstractTransactionRepository<ApproveFunctionArguments> {

    ApproveTransactionRepository(
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
                    TRANSACTION_APPROVE,
                    TRANSACTION_APPROVE.TIMESTAMP,
                    TRANSACTION_APPROVE.SPENDER_ADDRESS_ID,
                    TRANSACTION_APPROVE.TOKEN_ID
                )
                .values((Long) null, null, null)
                .onConflictDoNothing()
        );
    }

    @Override
    public Collection<Address> getAddressList(
        ApproveFunctionArguments arguments
    ) {
        return List.of(arguments.spender);
    }

    @Override
    protected BatchBindStep bind(
        BatchBindStep batch,
        long consensusTimestampNanos,
        ApproveFunctionArguments arguments
    ) {
        return batch.bind(
            consensusTimestampNanos,
            addressRepository.toAddressId(arguments.spender),
            arguments.id.value
        );
    }
}
