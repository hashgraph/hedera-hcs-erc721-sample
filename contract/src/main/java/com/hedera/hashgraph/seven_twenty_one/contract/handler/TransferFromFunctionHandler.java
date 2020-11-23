package com.hedera.hashgraph.seven_twenty_one.contract.handler;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.State;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.TransferFromFunctionArguments;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;

public final class TransferFromFunctionHandler
    extends FunctionHandler<TransferFromFunctionArguments> {

    @Override
    public TransferFromFunctionArguments parse(FunctionBody functionBody) {
        return TransferFromFunctionArguments.parse(functionBody);
    }

    @Override
    public void validate(
        State state,
        Address caller,
        TransferFromFunctionArguments arguments
    ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void call(
        State state,
        Address caller,
        TransferFromFunctionArguments arguments
    ) {
        throw new UnsupportedOperationException();
    }
}
