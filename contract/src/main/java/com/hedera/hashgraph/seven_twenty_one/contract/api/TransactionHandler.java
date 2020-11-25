package com.hedera.hashgraph.seven_twenty_one.contract.api;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.io.CharStreams;
import com.hedera.hashgraph.seven_twenty_one.contract.Status;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgPool;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionHandler implements Handler<RoutingContext> {

    private final PgPool pgPool;

    private final String sql = CharStreams.toString(
        new InputStreamReader(
            Objects.requireNonNull(
                TransactionHandler.class.getClassLoader()
                    .getResourceAsStream("queries/latest-50-transactions.sql")
            ),
            UTF_8
        )
    );

    TransactionHandler(PgPool pgPool) throws IOException {
        this.pgPool = pgPool;
    }

    private static void finish(
        RoutingContext routingContext,
        List<TransactionResponseItem> transactions
    ) {
        var response = new TransactionResponse();
        response.transactions = transactions;

        routingContext
            .response()
            .putHeader("content-type", "application/json")
            .end(Json.encodeToBuffer(response));
    }

    @Override
    public void handle(RoutingContext routingContext) {
        try {
            pgPool
                .preparedQuery(sql)
                .execute(
                    ar -> {
                        if (ar.failed()) {
                            routingContext.fail(ar.cause());
                            return;
                        }

                        var rows = ar.result();
                        var transactions = new ArrayList<TransactionResponseItem>(
                            rows.rowCount()
                        );

                        try {
                            for (var row : rows) {
                                var item = new TransactionResponseItem();

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

                                transactions.add(item);
                            }

                            finish(routingContext, transactions);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class TransactionResponse {

        public List<TransactionResponseItem> transactions = new ArrayList<>();
    }
}
