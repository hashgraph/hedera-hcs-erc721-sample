package com.hedera.hashgraph.seven_twenty_one.contract.api;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.Status;
import io.vertx.core.json.JsonObject;
import java.time.Instant;
import javax.annotation.Nullable;

public class TransactionResponseItem {

    public Instant consensusAt = Instant.EPOCH;

    public String caller = "";

    public Status status = Status.OK;

    public String function = "";

    @Nullable
    public JsonObject data = null;

    public TransactionResponseItem() {
        // don't remove
        // this is for Jackson
    }

    public TransactionResponseItem(
        Instant consensusAt,
        Status status,
        String function,
        Address caller,
        JsonObject data
    ) {
        this.caller = caller.toString();
        this.consensusAt = consensusAt;
        this.function = function;
        this.data = data;
        this.status = status;
    }
}
