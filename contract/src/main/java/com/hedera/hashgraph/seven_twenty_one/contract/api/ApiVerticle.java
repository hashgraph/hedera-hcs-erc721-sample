package com.hedera.hashgraph.seven_twenty_one.contract.api;

import com.hedera.hashgraph.seven_twenty_one.contract.State;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.pgclient.PgPool;
import java.io.IOException;

public class ApiVerticle extends AbstractVerticle {

    private final State state;

    private final int port;

    private final PgPool pgPool;

    public ApiVerticle(State state, int port, PgPool pgPool) {
        this.state = state;
        this.port = port;
        this.pgPool = pgPool;
    }

    private static void failureHandler(RoutingContext routingContext) {
        var response = routingContext.response();

        // if we got into the failure handler the status code
        // has likely been populated
        if (routingContext.statusCode() > 0) {
            response.setStatusCode(routingContext.statusCode());
        }

        var cause = routingContext.failure();
        if (cause != null) {
            cause.printStackTrace();
            response.setStatusCode(500);
        }

        response.end();
    }

    @Override
    public void start(Promise<Void> promise) throws IOException {
        var server = vertx.createHttpServer();
        var router = Router.router(vertx);

        router
            .route()
            .handler(CorsHandler.create("*"))
            .failureHandler(ApiVerticle::failureHandler);

        router.get("/").handler(new BaseHandler(state));
        router.get("/token/:token").handler(new TokenHandler(state));
        router.get("/account/:address").handler(new AddressHandler(state));

        router
            .get("/transaction/:operatorAccountNum/:validStartNanos/receipt")
            .handler(new FunctionResultHandler(state));

        router.get("/transaction/:operatorAccountNum/:validStartNanos").handler(new SpecificTransactionHandler(pgPool));

        router.get("/transaction").handler(new TransactionHandler(pgPool));
        router
            .get("/account/:address/transaction")
            .handler(new AddressTransactionHandler(pgPool));

        server
            .requestHandler(router)
            .listen(
                port,
                result -> {
                    if (result.succeeded()) {
                        promise.complete();
                    } else {
                        promise.fail(result.cause());
                    }
                }
            );
    }
}
