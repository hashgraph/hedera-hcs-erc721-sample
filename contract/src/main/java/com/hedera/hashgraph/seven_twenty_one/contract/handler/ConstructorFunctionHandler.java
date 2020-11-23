package com.hedera.hashgraph.seven_twenty_one.contract.handler;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.State;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.ConstructorFunctionArguments;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;

public final class ConstructorFunctionHandler
    extends FunctionHandler<ConstructorFunctionArguments> {

    @Override
    public ConstructorFunctionArguments parse(FunctionBody functionBody) {
        return ConstructorFunctionArguments.parse(functionBody);
    }

    @Override
    public void validate(
        State state,
        Address caller,
        ConstructorFunctionArguments arguments
    ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void call(
        State state,
        Address caller,
        ConstructorFunctionArguments arguments
    ) {
        throw new UnsupportedOperationException();
    }
}
