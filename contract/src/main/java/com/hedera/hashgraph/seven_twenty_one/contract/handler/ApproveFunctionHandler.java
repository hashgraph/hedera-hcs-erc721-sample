package com.hedera.hashgraph.seven_twenty_one.contract.handler;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.State;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.ApproveFunctionArguments;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;

public final class ApproveFunctionHandler
    extends FunctionHandler<ApproveFunctionArguments> {

    @Override
    public ApproveFunctionArguments parse(FunctionBody functionBody) {
        return ApproveFunctionArguments.parse(functionBody);
    }

    @Override
    public void validate(
        State state,
        Address caller,
        ApproveFunctionArguments arguments
    ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void call(
        State state,
        Address caller,
        ApproveFunctionArguments arguments
    ) {
        throw new UnsupportedOperationException();
    }
}
