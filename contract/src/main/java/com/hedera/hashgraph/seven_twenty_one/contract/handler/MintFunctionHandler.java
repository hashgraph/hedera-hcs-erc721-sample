package com.hedera.hashgraph.seven_twenty_one.contract.handler;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.State;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.MintFunctionArguments;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;

public final class MintFunctionHandler
    extends FunctionHandler<MintFunctionArguments> {

    @Override
    public MintFunctionArguments parse(FunctionBody functionBody) {
        return MintFunctionArguments.parse(functionBody);
    }

    @Override
    public void validate(
        State state,
        Address caller,
        MintFunctionArguments arguments
    ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void call(
        State state,
        Address caller,
        MintFunctionArguments arguments
    ) {
        throw new UnsupportedOperationException();
    }
}
