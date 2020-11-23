package com.hedera.hashgraph.seven_twenty_one.contract.handler;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.State;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.BurnFunctionArguments;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;

public final class BurnFunctionHandler
    extends FunctionHandler<BurnFunctionArguments> {

    @Override
    public BurnFunctionArguments parse(FunctionBody functionBody) {
        return BurnFunctionArguments.parse(functionBody);
    }

    @Override
    public void validate(
        State state,
        Address caller,
        BurnFunctionArguments arguments
    ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void call(
        State state,
        Address caller,
        BurnFunctionArguments arguments
    ) {
        throw new UnsupportedOperationException();
    }
}
