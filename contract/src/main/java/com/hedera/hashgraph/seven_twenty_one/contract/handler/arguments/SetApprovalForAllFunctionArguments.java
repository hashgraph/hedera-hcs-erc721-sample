package com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;

public final class SetApprovalForAllFunctionArguments {

    public final Address operator;

    public final boolean approved;

    public SetApprovalForAllFunctionArguments(
        Address operator,
        boolean approved
    ) {
        this.operator = operator;
        this.approved = approved;
    }

    public static SetApprovalForAllFunctionArguments parse(
        FunctionBody functionBody
    ) {
        assert functionBody.hasSetApprovalForAll();
        var data = functionBody.getSetApprovalForAll();

        return new SetApprovalForAllFunctionArguments(
            Address.fromByteString(data.getOperator()),
            data.getApproved()
        );
    }
}
