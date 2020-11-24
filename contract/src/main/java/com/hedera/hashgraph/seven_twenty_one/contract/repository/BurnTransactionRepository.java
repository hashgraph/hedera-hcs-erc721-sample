package com.hedera.hashgraph.seven_twenty_one.contract.repository;

import static com.hedera.hashgraph.seven_twenty_one.contract.db.Tables.TRANSACTION_BURN;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.BurnFunctionArguments;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;

public final class BurnTransactionRepository
    extends AbstractTransactionRepository<BurnFunctionArguments> {

    BurnTransactionRepository(
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
                    TRANSACTION_BURN,
                    TRANSACTION_BURN.TIMESTAMP,
                    TRANSACTION_BURN.TOKEN_ID
                )
                .values((Long) null, null)
                .onConflictDoNothing()
        );
    }

    @Override
    public Collection<Address> getAddressList(BurnFunctionArguments arguments) {
        return Collections.emptyList();
    }

    @Override
    protected BatchBindStep bind(
        BatchBindStep batch,
        long consensusTimestampNanos,
        BurnFunctionArguments arguments
    ) {
        return batch.bind(consensusTimestampNanos, arguments.id.value);
    }
}
