package com.hedera.hashgraph.seven_twenty_one.contract.handler;

import com.google.errorprone.annotations.Var;
import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.State;
import com.hedera.hashgraph.seven_twenty_one.contract.Status;
import com.hedera.hashgraph.seven_twenty_one.contract.StatusException;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.TransferFromFunctionArguments;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;

import java.util.Objects;

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
    ) throws StatusException {
        // i. Owner != 0x
        ensureNotNull(state.getOwner(), Status.CONSTRUCTOR_NOT_CALLED);

        // ii. TokenOwners[id] != 0x
        var tokenOwner = state.getTokenOwner(arguments.id);
        ensureNotNull(tokenOwner, Status.TOKEN_NOT_FOUND);

        // iii. TokenOwners[id] = caller || TokenApprovals[id] = caller || OperatorApprovals[TokenOwners[id]][caller]
        @Var var isAuthorized = Objects.equals(tokenOwner, caller);
        if (!isAuthorized) isAuthorized = state.isApproved(caller, arguments.id);
        if (!isAuthorized) isAuthorized = state.isOperatorApproved(caller, arguments.id);

        ensure(isAuthorized, Status.UNAUTHORIZED);

        // iv. TokenOwners[id] = from
        ensure(Objects.equals(tokenOwner, arguments.from), Status.TRANSFER_FROM_NOT_TOKEN_OWNER);

        // v. to != 0x
        ensureNotNull(arguments.to, Status.TRANSFER_TO_NOT_SET);
    }

    @Override
    public void call(
        State state,
        Address caller,
        TransferFromFunctionArguments arguments
    ) {
        // i. HolderTokens[from]’ = HolderTokens[from] \ {id}
        state.removeToken(arguments.id, arguments.from);

        // ii. HolderTokens[to]’ = HolderTokens[to] + {id}
        state.addToken(arguments.id, arguments.to);

        // iii. TokenOwners[i] = to
        state.setTokenOwner(arguments.id, arguments.to);

        // iv. TokenApprovals[id] = 0x
        state.clearTokenApproval(arguments.id);
    }
}
