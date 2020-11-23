package com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.Int;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;

public final class MintFunctionArguments {

    public final Address to;

    public final Int id;

    public MintFunctionArguments(Address to, Int id) {
        this.to = to;
        this.id = id;
    }

    public static MintFunctionArguments parse(FunctionBody functionBody) {
        assert functionBody.hasMint();
        var data = functionBody.getMint();

        return new MintFunctionArguments(
            Address.fromByteString(data.getTo()),
            Int.fromByteString(data.getId())
        );
    }
}
