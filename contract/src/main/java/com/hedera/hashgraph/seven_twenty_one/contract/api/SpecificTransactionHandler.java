package com.hedera.hashgraph.seven_twenty_one.contract.api;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.io.CharStreams;
import com.hedera.hashgraph.seven_twenty_one.contract.Status;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Tuple;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Objects;

public class SpecificTransactionHandler implements Handler<RoutingContext> {

    private final PgPool pgPool;

    private final String sql = CharStreams.toString(
        new InputStreamReader(
            Objects.requireNonNull(
                TransactionHandler.class.getClassLoader()
                    .getResourceAsStream("queries/specific-transaction.sql")
            ),
            UTF_8
        )
    );

    SpecificTransactionHandler(PgPool pgPool) throws IOException {
        this.pgPool = pgPool;
    }

    private static void finish(
        RoutingContext routingContext,
        TransactionResponseItem transaction
    ) {
        routingContext
            .response()
            .putHeader("content-type", "application/json")
            .end(Json.encodeToBuffer(transaction));
    }

    @Override
    public void handle(RoutingContext routingContext) {
        var operatorAccountNum = Long.parseLong(
            routingContext.request().getParam("operatorAccountNum")
        );

        var validStartNanos = Long.parseLong(
            routingContext.request().getParam("validStartNanos")
        );

        try {
            pgPool
                .preparedQuery(sql)
                .execute(
                    Tuple.of(validStartNanos, operatorAccountNum),
                    ar -> {
                        if (ar.failed()) {
                            routingContext.fail(ar.cause());
                            return;
                        }

                        var rows = ar.result();
                        var item = new TransactionResponseItem();

                        try {
                            for (var row : rows) {
                                System.out.println(
                                    "Row: " + row.deepToString()
                                );
                                item.caller = row.getString("caller");
                                item.consensusAt =
                                    Instant.ofEpochSecond(
                                        0,
                                        row.getLong("timestamp")
                                    );
                                item.data = row.get(JsonObject.class, 4);
                                item.function = row.getString("function");
                                item.status =
                                    Status.valueOf(row.getInteger("status"));
                            }

                            finish(routingContext, item);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
