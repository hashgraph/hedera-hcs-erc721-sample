package com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.Int;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;

public final class ApproveFunctionArguments {

    public final Address spender;

    public final Int id;

    public ApproveFunctionArguments(Address spender, Int id) {
        this.spender = spender;
        this.id = id;
    }

    public static ApproveFunctionArguments parse(FunctionBody functionBody) {
        assert functionBody.hasApprove();
        var data = functionBody.getApprove();

        return new ApproveFunctionArguments(
            Address.fromByteString(data.getSpender()),
            Int.fromByteString(data.getId())
        );
    }
}
