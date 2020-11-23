package com.hedera.hashgraph.seven_twenty_one.contract.handler.arguments;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.Int;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;

public final class TransferFromFunctionArguments {

    public final Address from;

    public final Address to;

    public final Int id;

    public TransferFromFunctionArguments(Address from, Address to, Int id) {
        this.from = from;
        this.to = to;
        this.id = id;
    }

    public static TransferFromFunctionArguments parse(
        FunctionBody functionBody
    ) {
        assert functionBody.hasTransferFrom();
        var data = functionBody.getTransferFrom();

        return new TransferFromFunctionArguments(
            Address.fromByteString(data.getFrom()),
            Address.fromByteString(data.getTo()),
            Int.fromByteString(data.getId())
        );
    }
}
