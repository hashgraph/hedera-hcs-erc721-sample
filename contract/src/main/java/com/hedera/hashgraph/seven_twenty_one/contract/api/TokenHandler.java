package com.hedera.hashgraph.seven_twenty_one.contract.api;

import com.hedera.hashgraph.seven_twenty_one.contract.Int;
import com.hedera.hashgraph.seven_twenty_one.contract.State;
import io.grpc.netty.shaded.io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.math.BigInteger;
import java.util.Objects;

public class TokenHandler implements Handler<RoutingContext> {

    private final State state;

    TokenHandler(State state) {
        this.state = state;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        var tokenId = new Int(
            new BigInteger(routingContext.request().getParam("token"))
        );
        var data = new JsonObject();

        // Get token owner
        //  Address ownerOf(Int id)
        var tokenOwner = state.getTokenOwner(tokenId);
        data.put("owner", tokenOwner != null ? tokenOwner.toString() : null);

        // Get approved
        //  Address getApproved(int id)
        data.put(
            "approved",
            Objects.toString(state.getApproved(tokenId), null)
        );

        routingContext
            .response()
            .putHeader(HttpHeaderNames.CONTENT_TYPE, "application/json")
            .setStatusCode(200)
            .end(data.encode());
    }
}
