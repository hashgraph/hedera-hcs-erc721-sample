package com.hedera.hashgraph.seven_twenty_one.contract.handler;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.State;
import com.hedera.hashgraph.seven_twenty_one.contract.Status;
import com.hedera.hashgraph.seven_twenty_one.contract.StatusException;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.ApproveFunctionArguments;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ApproveFunctionHandler
    extends FunctionHandler<ApproveFunctionArguments> {

    private static final Logger logger = LogManager.getLogger(
        ApproveFunctionHandler.class.getName()
    );

    @Override
    public ApproveFunctionArguments parse(FunctionBody functionBody) {
        return ApproveFunctionArguments.parse(functionBody);
    }

    @Override
    public void validate(
        State state,
        Address caller,
        ApproveFunctionArguments arguments
    ) throws StatusException {
        // i. Owner != 0
        ensureNotNull(state.getOwner(), Status.CONSTRUCTOR_NOT_CALLED);

        // ii. TokenOwners[id] != 0x
        var tokenOwner = state.getTokenOwner(arguments.id);
        ensureNotNull(tokenOwner, Status.TOKEN_NOT_FOUND);

        // iii. caller = TokenOwners[id] || OperatorApprovals[TokenOwners[id]][caller]
        ensure(
            Objects.equals(caller, tokenOwner) ||
            state.isOperatorApproved(caller, arguments.id),
            Status.UNAUTHORIZED
        );

        // iv. spender != TokenOwners[id]
        ensure(
            !Objects.equals(arguments.spender, tokenOwner),
            Status.APPROVE_SPENDER_IS_OWNER
        );
    }

    @Override
    public void call(
        State state,
        Address caller,
        ApproveFunctionArguments arguments
    ) {
        // i. TokenApprovals[id] = spender
        state.setTokenApproval(arguments.id, arguments.spender);
    }

    @Override
    public void log(Address caller, ApproveFunctionArguments arguments) {
        logger.info(
            "Approve caller: {}, spender: {}, token: {}",
            caller,
            arguments.spender,
            arguments.id
        );
    }
}
