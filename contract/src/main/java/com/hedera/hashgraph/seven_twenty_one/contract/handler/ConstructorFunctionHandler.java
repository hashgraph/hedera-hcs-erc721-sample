package com.hedera.hashgraph.seven_twenty_one.contract.handler;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.State;
import com.hedera.hashgraph.seven_twenty_one.contract.Status;
import com.hedera.hashgraph.seven_twenty_one.contract.StatusException;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments.ConstructorFunctionArguments;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ConstructorFunctionHandler
    extends FunctionHandler<ConstructorFunctionArguments> {

    private static final Logger logger = LogManager.getLogger(
        ConstructorFunctionHandler.class.getName()
    );

    @Override
    public ConstructorFunctionArguments parse(FunctionBody functionBody) {
        return ConstructorFunctionArguments.parse(functionBody);
    }

    @Override
    public void validate(
        State state,
        Address caller,
        ConstructorFunctionArguments arguments
    ) throws StatusException {
        // i. Owner = 0x
        ensure(state.getOwner() == null, Status.CONSTRUCTOR_ALREADY_CALLED);

        // ii. caller != 0x
        ensureNotNull(caller, Status.CALLER_NOT_SET);
    }

    @Override
    public void call(
        State state,
        Address caller,
        ConstructorFunctionArguments arguments
    ) {
        // note: i through v are not implemented

        // vi. Owner = caller
        state.setOwner(caller);

        // vii. HolderTokens = {}
        //  initial state

        // viii. TokenOwners = {}
        //  initial state

        // ix. OperatorApprovals = {}
        //  initial state

        // x. TokenApprovals = {}
        //  initial state

        // xi. TokenName = tokenName
        state.setTokenName(arguments.tokenName);

        // xii. TokenSymbol = tokenSymbol
        state.setTokenSymbol(arguments.tokenSymbol);

        // xiii. BaseURI = baseURI
        state.setBaseURI(arguments.baseURI);
        //
        // xiv. TokenURIs = {}
        //  initial state
    }

    @Override
    public void log(long sequenceNumber, Address caller, ConstructorFunctionArguments arguments) {
        logger.info(
            "[{}] Constructor caller: {}, tokenName: {}, tokenSymbol: {}, baseURI: {}",
            sequenceNumber,
            caller,
            arguments.tokenName,
            arguments.tokenSymbol,
            arguments.baseURI
        );
    }
}
