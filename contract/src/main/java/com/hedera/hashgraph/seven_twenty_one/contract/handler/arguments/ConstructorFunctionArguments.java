package com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments;

import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;

public final class ConstructorFunctionArguments {

    public final String tokenName;

    public final String tokenSymbol;

    public final String baseURI;

    public ConstructorFunctionArguments(
        String tokenName,
        String tokenSymbol,
        String baseURI
    ) {
        this.tokenName = tokenName;
        this.tokenSymbol = tokenSymbol;
        this.baseURI = baseURI;
    }

    public static ConstructorFunctionArguments parse(
        FunctionBody functionBody
    ) {
        assert functionBody.hasConstruct();
        var data = functionBody.getConstruct();

        return new ConstructorFunctionArguments(
            data.getTokenName(),
            data.getTokenSymbol(),
            data.getBaseURI()
        );
    }
}
