package com.hedera.hashgraph.seven_twenty_one.contract.handler;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.State;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.SetApprovalForAllFunctionArguments;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;

public final class SetApprovalForAllFunctionHandler
    extends FunctionHandler<SetApprovalForAllFunctionArguments> {

    @Override
    public SetApprovalForAllFunctionArguments parse(FunctionBody functionBody) {
        return SetApprovalForAllFunctionArguments.parse(functionBody);
    }

    @Override
    public void validate(
        State state,
        Address caller,
        SetApprovalForAllFunctionArguments arguments
    ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void call(
        State state,
        Address caller,
        SetApprovalForAllFunctionArguments arguments
    ) {
        throw new UnsupportedOperationException();
    }
}
