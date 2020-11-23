package com.hedera.hashgraph.seven_twenty_one.contract;

import com.google.protobuf.InvalidProtocolBufferException;
import com.hedera.hashgraph.sdk.*;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.FunctionHandler;
import com.hedera.hashgraph.seven_twenty_one.proto.Function;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;

public class TopicListener {

    // map of incoming function case to the handler that will accept
    private final Map<FunctionBody.DataCase, FunctionHandler<?>> functionHandlers = Collections.emptyMap();

    // the current state of the contract
    private final State state;

    // client to interact with the hedera network
    private final Client hederaClient;

    // the topic that we are listening to for function calls
    private final TopicId hederaTopicId;

    // handle of the open subscription to the topic
    @Nullable
    private SubscriptionHandle hederaSubscriptionHandle;

    public TopicListener(
        State state,
        Client hederaClient,
        TopicId hederaTopicId
    ) {
        this.state = state;
        this.hederaClient = hederaClient;
        this.hederaTopicId = hederaTopicId;
    }

    public synchronized void startListening() {
        stopListening();

        hederaSubscriptionHandle =
            new TopicMessageQuery()
                .setTopicId(hederaTopicId)
                .setStartTime(
                    Optional
                        .ofNullable(state.getTimestamp())
                        .orElse(Instant.EPOCH)
                        // the server returns messages from this timestamp onwards so to "resume" we give it +1 ns
                        .plusNanos(1)
                )
                .subscribe(hederaClient, this::handleTopicMessage);
    }

    public synchronized void stopListening() {
        if (hederaSubscriptionHandle != null) {
            hederaSubscriptionHandle.unsubscribe();
            hederaSubscriptionHandle = null;
        }
    }

    private void handleTopicMessage(TopicMessage topicMessage) {
        // try to parse a <Function> protobuf message
        // from the contents of the message on the topic

        Function function;
        FunctionBody functionBody;

        try {
            function = Function.parseFrom(topicMessage.contents);
            functionBody = FunctionBody.parseFrom(function.getBody());
        } catch (InvalidProtocolBufferException e) {
            // this is not a true error, someone submitted a protobuf that we don't understand
            // ignore the message
            // TODO: log the failure as a warning
            return;
        }

        var expectedTransactionId = new TransactionId(
            new AccountId(functionBody.getOperatorAccountNum()),
            Instant.ofEpochSecond(0, functionBody.getValidStartNanos())
        );

        if (!expectedTransactionId.equals(topicMessage.transactionId)) {
            // the transaction ID in the function body does not match the transaction ID that this
            // function was submitted under

            // this is a protection against DUPLICATE transactions so we treat this as a true error

            // FIXME: log this as an error
            return;
        }

        if (functionBody.getCaller().isEmpty()) {
            // caller address we not set, immediately invalid
            // FIXME: log this as an error
            return;
        }

        var caller = Address.fromByteString(functionBody.getCaller());
        var signature = function.getSignature().toByteArray();

        // verify that the function body was signed by the declared caller
        // this is protection against the integrity of the message and ensures that this is really tied with
        // the caller

        if (
            !caller.publicKey.verify(
                function.getBody().toByteArray(),
                signature
            )
        ) {
            // FIXME: log this as an error
            return;
        }

        // get a function handler that matches the data case
        @SuppressWarnings("unchecked")
        var functionHandler = Objects.requireNonNull(
            (FunctionHandler<Object>) functionHandlers.get(
                functionBody.getDataCase()
            )
        );

        // parse the function arguments from the protobuf data
        var functionArguments = functionHandler.parse(functionBody);

        // validate the function arguments
        // this will raise an exception that we need to handle and produce a failed function result
        functionHandler.validate(state, caller, functionArguments);

        // finally, call the function
        functionHandler.call(state, caller, functionArguments);
    }
}
