package com.hedera.hashgraph.seven_twenty_one.contract.api;

import com.hedera.hashgraph.seven_twenty_one.contract.State;
import io.grpc.netty.shaded.io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class BaseHandler implements Handler<RoutingContext> {

    private final State state;

    BaseHandler(State state) {
        this.state = state;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        var data = new JsonObject();

        // Get token name
        //  String name()
        data.put("name", state.getTokenName());

        // Get token symbol
        //  String symbol()
        data.put("symbol", state.getTokenSymbol());

        // Get base URI
        //  String baseURI()
        data.put("baseURI", state.getBaseURI());

        // Get total supply
        //  int totalSupply()
        data.put("totalSupply", state.getNumberOfTokenOwners());

        // Get owner
        //  Address owner()
        data.put(
            "owner",
            state.getOwner() != null ? state.getOwner().toString() : null
        );

        routingContext
            .response()
            .putHeader(HttpHeaderNames.CONTENT_TYPE, "application/json")
            .setStatusCode(200)
            .end(data.encode());
    }
}
