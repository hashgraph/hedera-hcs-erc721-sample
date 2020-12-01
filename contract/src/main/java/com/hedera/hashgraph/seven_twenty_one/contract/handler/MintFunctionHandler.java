package com.hedera.hashgraph.seven_twenty_one.contract.handler;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.State;
import com.hedera.hashgraph.seven_twenty_one.contract.Status;
import com.hedera.hashgraph.seven_twenty_one.contract.StatusException;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.MintFunctionArguments;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;
import java.util.Objects;

public final class MintFunctionHandler
    extends FunctionHandler<MintFunctionArguments> {

    @Override
    public MintFunctionArguments parse(FunctionBody functionBody) {
        return MintFunctionArguments.parse(functionBody);
    }

    @Override
    public void validate(
        State state,
        Address caller,
        MintFunctionArguments arguments
    ) throws StatusException {
        // i. Owner != 0x
        ensureNotNull(state.getOwner(), Status.CONSTRUCTOR_NOT_CALLED);

        // ii. to != 0x
        ensureNotNull(arguments.to, Status.MINT_TO_NOT_SET);

        // iii. TokenOwners[id] = 0x
        ensure(
            state.getTokenOwner(arguments.id) == null,
            Status.MINT_TOKEN_EXISTS
        );

        // iv. caller = Owner
        //  Only the Owner may mint tokens
        ensure(Objects.equals(state.getOwner(), caller), Status.UNAUTHORIZED);
    }

    @Override
    public void call(
        State state,
        Address caller,
        MintFunctionArguments arguments
    ) {
        // i. HolderTokens[to]' = HolderTokens[to] + {id}
        //  Add id to the set
        state.addToken(arguments.id, arguments.to);

        // ii. TokenOwners[id] = to
        state.setTokenOwner(arguments.id, arguments.to);
    }
}
