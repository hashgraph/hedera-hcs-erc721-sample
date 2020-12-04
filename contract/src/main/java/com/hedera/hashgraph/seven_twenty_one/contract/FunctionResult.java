package com.hedera.hashgraph.seven_twenty_one.contract;

import com.hedera.hashgraph.sdk.TransactionId;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class FunctionResult {

    public final Instant consensusAt;

    public final Address caller;

    public final TransactionId transactionId;

    public final Status status;

    public FunctionResult(
        Instant consensusAt,
        Address caller,
        TransactionId transactionId,
        Status status
    ) {
        this.consensusAt = consensusAt;
        this.caller = caller;
        this.transactionId = transactionId;
        this.status = status;
    }

    public boolean isExpired(Instant fromTimestamp) {
        return (
            ChronoUnit.MINUTES.between(
                transactionId.validStart,
                fromTimestamp
            ) >
            2
        );
    }
}
