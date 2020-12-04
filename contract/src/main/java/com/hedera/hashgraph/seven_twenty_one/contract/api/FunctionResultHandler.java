package com.hedera.hashgraph.seven_twenty_one.contract.api;

import com.hedera.hashgraph.seven_twenty_one.contract.State;
import com.hedera.hashgraph.seven_twenty_one.contract.Status;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import java.time.Instant;

public class FunctionResultHandler implements Handler<RoutingContext> {

    private final State state;

    FunctionResultHandler(State state) {
        this.state = state;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        var operatorAccountNum = Long.parseLong(
            routingContext.request().getParam("operatorAccountNum")
        );
        var validStartNanos = Long.parseLong(
            routingContext.request().getParam("validStartNanos")
        );

        var functionResult = state.getFunctionResult(
            operatorAccountNum,
            validStartNanos
        );

        if (functionResult == null) {
            routingContext.response().setStatusCode(404).end();

            return;
        }

        var response = new FunctionResultResponse();
        response.id = operatorAccountNum + "/" + validStartNanos;
        response.caller = functionResult.caller.toString();
        response.consensusAt = functionResult.consensusAt;
        response.status = functionResult.status;

        routingContext
            .response()
            .putHeader("content-type", "application/json")
            .end(Json.encodeToBuffer(response));
    }

    private static class FunctionResultResponse {

        public String id = "";

        public String caller = "";

        public Instant consensusAt = Instant.EPOCH;

        public Status status = Status.OK;
    }
}
