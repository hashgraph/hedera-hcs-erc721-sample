package com.hedera.hashgraph.seven_twenty_one.contract.handler;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.State;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;

public abstract class FunctionHandler<ArgumentsT> {

    public abstract ArgumentsT parse(FunctionBody functionBody);

    public abstract void validate(
        State state,
        Address caller,
        ArgumentsT arguments
    );

    public abstract void call(
        State state,
        Address caller,
        ArgumentsT arguments
    );
}
