package com.hedera.hashgraph.seven_twenty_one.contract.repository;

import static com.hedera.hashgraph.seven_twenty_one.contract.db.Tables.TRANSACTION_MINT;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.MintFunctionArguments;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;

public final class MintTransactionRepository
    extends AbstractTransactionRepository<MintFunctionArguments> {

    MintTransactionRepository(
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
                    TRANSACTION_MINT,
                    TRANSACTION_MINT.TIMESTAMP,
                    TRANSACTION_MINT.TO_ADDRESS_ID,
                    TRANSACTION_MINT.TOKEN_ID
                )
                .values((Long) null, null, null)
                .onConflictDoNothing()
        );
    }

    @Override
    public Collection<Address> getAddressList(MintFunctionArguments arguments) {
        return List.of(arguments.to);
    }

    @Override
    protected BatchBindStep bind(
        BatchBindStep batch,
        long consensusTimestampNanos,
        MintFunctionArguments arguments
    ) {
        return batch.bind(
            consensusTimestampNanos,
            addressRepository.toAddressId(arguments.to),
            arguments.id.value
        );
    }
}
