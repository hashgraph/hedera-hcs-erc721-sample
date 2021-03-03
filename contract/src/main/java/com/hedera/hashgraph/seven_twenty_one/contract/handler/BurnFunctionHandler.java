package com.hedera.hashgraph.seven_twenty_one.contract.handler;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.State;
import com.hedera.hashgraph.seven_twenty_one.contract.Status;
import com.hedera.hashgraph.seven_twenty_one.contract.StatusException;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.BurnFunctionArguments;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public final class BurnFunctionHandler
    extends FunctionHandler<BurnFunctionArguments> {

    private static final Logger logger = LogManager.getLogger(
        BurnFunctionHandler.class.getName()
    );

    @Override
    public BurnFunctionArguments parse(FunctionBody functionBody) {
        return BurnFunctionArguments.parse(functionBody);
    }

    @Override
    public void validate(
        State state,
        Address caller,
        BurnFunctionArguments arguments
    ) throws StatusException {
        // i. Owner != 0x
        ensureNotNull(state.getOwner(), Status.CONSTRUCTOR_NOT_CALLED);

        // ii. TokenOwners[id] != 0x
        ensureNotNull(
            state.getTokenOwner(arguments.id),
            Status.TOKEN_NOT_FOUND
        );

        // iii. TokenOwners[id] = caller
        ensure(
            Objects.equals(state.getTokenOwner(arguments.id), caller),
            Status.BURN_CALLER_NOT_OWNER
        );
    }

    @Override
    public void call(
        State state,
        Address caller,
        BurnFunctionArguments arguments
    ) {
        var tokenOwner = state.getTokenOwner(arguments.id);

        // i. HolderTokens[TokenOwners[id]]’ = HolderTokens[TokenOwners[id]] \ {id}
        //  Remove element from the set
        state.removeToken(arguments.id, Objects.requireNonNull(tokenOwner));

        // ii. TokenOwners[id] = 0x
        state.clearTokenOwner(arguments.id);

        // iii. TokenApprovals[id] = 0x
        state.clearTokenApproval(arguments.id);

        // iv. TokenURIs[id] = ""
        state.clearTokenURI(arguments.id);
    }

    @Override
    public void log(long sequenceNumber, Address caller, BurnFunctionArguments arguments) {
        logger.info("[{}] Burn caller: {}, token: {}", sequenceNumber, caller, arguments.id);
    }
}
