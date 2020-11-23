package com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments;

import com.hedera.hashgraph.seven_twenty_one.contract.Int;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;

public final class BurnFunctionArguments {

    public final Int id;

    public BurnFunctionArguments(Int id) {
        this.id = id;
    }

    public static BurnFunctionArguments parse(FunctionBody functionBody) {
        assert functionBody.hasBurn();
        var data = functionBody.getBurn();

        return new BurnFunctionArguments(Int.fromByteString(data.getId()));
    }
}
