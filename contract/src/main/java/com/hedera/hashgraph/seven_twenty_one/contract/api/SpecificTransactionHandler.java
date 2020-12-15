package com.hedera.hashgraph.seven_twenty_one.contract.api;

import com.google.common.io.CharStreams;
import com.hedera.hashgraph.seven_twenty_one.contract.Status;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.TransferFromFunctionHandler;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SpecificTransactionHandler implements Handler<RoutingContext> {

    private static final Logger logger = LogManager.getLogger(
            TransferFromFunctionHandler.class.getName()
    );

    private final PgPool pgPool;

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

        var sql = "SELECT t.timestamp, " +
                "encode(caller.address, 'hex') as caller, " +
                "       t.status, " +
                "       case t.function " +
                "           when 4 then 'constructor' " +
                "           when 5 then 'approve' " +
                "           when 6 then 'setApprovalForAll' " +
                "           when 7 then 'mint' " +
                "           when 8 then 'burn' " +
                "           when 9 then 'transferFrom' " +
                "           end as \"function\", " +
                "       case t.function " +
                "           when 4 then ( " +
                "               select jsonb_build_object( " +
                "                              'tokenName', tc.token_name, " +
                "                              'tokenSymbol', tc.token_symbol, " +
                "                              'baseURI', tc.base_uri " +
                "                          ) " +
                "               from transaction_constructor tc " +
                "               where tc.timestamp = t.timestamp " +
                "           ) " +
                "           when 5 then ( " +
                "               select jsonb_build_object('spender', encode(spender.address, 'hex'), 'id', '' || ta.token_id) " +
                "               from transaction_approve ta " +
                "               inner join address spender on spender.id = ta.spender_address_id " +
                "               where ta.timestamp = t.timestamp " +
                "           ) " +
                "           when 6 then ( " +
                "               select jsonb_build_object('operator', encode(operator_.address, 'hex'), 'approved', tsafa.approved) " +
                "               from transaction_set_approval_for_all tsafa " +
                "                        inner join address operator_ on operator_.id = tsafa.operator_address_id " +
                "               where tsafa.timestamp = t.timestamp " +
                "           ) " +
                "           when 7 then ( " +
                "               select jsonb_build_object('to', encode(to_.address, 'hex'), 'id', '' || tm.token_id) " +
                "               from transaction_mint tm " +
                "                        inner join address to_ on to_.id = tm.to_address_id " +
                "               where tm.timestamp = t.timestamp " +
                "           ) " +
                "           when 8 then ( " +
                "               select jsonb_build_object('id', '' || tb.token_id) " +
                "               from transaction_burn tb " +
                "               where tb.timestamp = t.timestamp " +
                "           ) " +
                "           when 9 then ( " +
                "               select jsonb_build_object('from', encode(from_.address, 'hex'), 'to', encode(to_.address, 'hex'), 'id', '' || ttf.token_id) " +
                "               from transaction_transfer_from ttf " +
                "                        left join address from_ on from_.id = ttf.from_address_id " +
                "                        left join address to_ on to_.id = ttf.to_address_id " +
                "               where ttf.timestamp = t.timestamp " +
                "           ) " +
                "           end as data " +
                "FROM public.transaction t " +
                "inner join address caller on caller.id = t.caller_address_id " +
                "WHERE hedera_valid_start_nanos = " + validStartNanos + " AND hedera_operator_account_num = " + operatorAccountNum;

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
                        var item = new TransactionResponseItem();

                        try {
                            for (var row : rows) {
                                System.out.println("Row: " + row.deepToString());
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
