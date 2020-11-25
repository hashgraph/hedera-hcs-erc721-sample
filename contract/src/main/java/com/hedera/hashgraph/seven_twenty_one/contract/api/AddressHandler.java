package com.hedera.hashgraph.seven_twenty_one.contract.api;

import com.hedera.hashgraph.seven_twenty_one.contract.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.State;
import io.grpc.netty.shaded.io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class AddressHandler implements Handler<RoutingContext> {

    private final State state;

    AddressHandler(State state) {
        this.state = state;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        var address = Address.fromString(
            routingContext.request().getParam("address")
        );
        var data = new JsonObject();

        // Get account balance
        data.put("balance", state.balanceOf(address));

        routingContext
            .response()
            .putHeader(HttpHeaderNames.CONTENT_TYPE, "application/json")
            .setStatusCode(200)
            .end(data.encode());
    }
}
