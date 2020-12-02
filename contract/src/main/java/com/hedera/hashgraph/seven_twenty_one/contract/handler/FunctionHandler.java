package com.hedera.hashgraph.seven_twenty_one.contract.handler;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.State;
import com.hedera.hashgraph.seven_twenty_one.contract.Status;
import com.hedera.hashgraph.seven_twenty_one.contract.StatusException;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;
import javax.annotation.Nullable;

public abstract class FunctionHandler<ArgumentsT> {

    public abstract ArgumentsT parse(FunctionBody functionBody);

    public abstract void validate(
        State state,
        Address caller,
        ArgumentsT arguments
    ) throws StatusException;

    public abstract void log(Address caller, ArgumentsT arguments);

    public abstract void call(
        State state,
        Address caller,
        ArgumentsT arguments
    );

    protected void ensure(boolean condition, Status status)
        throws StatusException {
        if (!condition) {
            throw new StatusException(status);
        }
    }

    protected <T> void ensureNotNull(@Nullable T value, Status status)
        throws StatusException {
        ensure(value != null, status);
    }
}
