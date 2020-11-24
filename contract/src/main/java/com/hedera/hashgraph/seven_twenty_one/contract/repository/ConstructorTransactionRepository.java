package com.hedera.hashgraph.seven_twenty_one.contract.repository;

import static com.hedera.hashgraph.seven_twenty_one.contract.db.Tables.TRANSACTION_CONSTRUCTOR;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.ConstructorFunctionArguments;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;

public final class ConstructorTransactionRepository
    extends AbstractTransactionRepository<ConstructorFunctionArguments> {

    ConstructorTransactionRepository(
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
                    TRANSACTION_CONSTRUCTOR,
                    TRANSACTION_CONSTRUCTOR.TIMESTAMP,
                    TRANSACTION_CONSTRUCTOR.TOKEN_NAME,
                    TRANSACTION_CONSTRUCTOR.TOKEN_SYMBOL,
                    TRANSACTION_CONSTRUCTOR.BASE_URI
                )
                .values((Long) null, null, null, null)
                .onConflictDoNothing()
        );
    }

    @Override
    public Collection<Address> getAddressList(
        ConstructorFunctionArguments arguments
    ) {
        return Collections.emptyList();
    }

    @Override
    protected BatchBindStep bind(
        BatchBindStep batch,
        long consensusTimestampNanos,
        ConstructorFunctionArguments arguments
    ) {
        return batch.bind(
            consensusTimestampNanos,
            arguments.tokenName,
            arguments.tokenSymbol,
            arguments.baseURI
        );
    }
}
