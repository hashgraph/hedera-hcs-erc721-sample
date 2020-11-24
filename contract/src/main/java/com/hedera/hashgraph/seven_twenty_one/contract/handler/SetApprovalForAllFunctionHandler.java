package com.hedera.hashgraph.seven_twenty_one.contract.handler;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.State;
import com.hedera.hashgraph.seven_twenty_one.contract.Status;
import com.hedera.hashgraph.seven_twenty_one.contract.StatusException;
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
    ) throws StatusException {
        // i. Owner != 0x
        ensureNotNull(state.getOwner(), Status.CONSTRUCTOR_NOT_CALLED);

        // ii. caller != operator
        ensure(
            !caller.equals(arguments.operator),
            Status.SET_APPROVAL_FOR_ALL_CALLER_IS_OPERATOR
        );
    }

    @Override
    public void call(
        State state,
        Address caller,
        SetApprovalForAllFunctionArguments arguments
    ) {
        // i. OperatorApprovals[caller][operator] = approved
        state.setOperatorApproval(
            caller,
            arguments.operator,
            arguments.approved
        );
    }
}
