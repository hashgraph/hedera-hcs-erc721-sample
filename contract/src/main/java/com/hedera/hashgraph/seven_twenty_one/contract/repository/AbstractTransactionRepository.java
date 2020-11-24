package com.hedera.hashgraph.seven_twenty_one.contract.repository;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import java.sql.SQLException;
import java.util.Collection;
import javax.annotation.Nullable;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;

public abstract class AbstractTransactionRepository<ArgumentsT> {

    protected final DSLContext context;

    protected final AddressRepository addressRepository;

    @Nullable
    private BatchBindStep batch;

    AbstractTransactionRepository(
        DSLContext context,
        AddressRepository addressRepository
    ) {
        this.context = context;
        this.addressRepository = addressRepository;
    }

    protected abstract BatchBindStep newBatch() throws SQLException;

    public abstract Collection<Address> getAddressList(ArgumentsT arguments);

    public void put(long consensusTimestampNanos, ArgumentsT arguments)
        throws SQLException {
        if (batch == null) {
            batch = newBatch();
        }

        batch = bind(batch, consensusTimestampNanos, arguments);
    }

    public void execute() {
        if (batch != null) {
            batch.execute();
            batch = null;
        }
    }

    protected abstract BatchBindStep bind(
        BatchBindStep batch,
        long consensusTimestampNanos,
        ArgumentsT arguments
    );
}
