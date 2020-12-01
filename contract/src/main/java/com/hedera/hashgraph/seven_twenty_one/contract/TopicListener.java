package com.hedera.hashgraph.seven_twenty_one.contract;

import com.google.protobuf.InvalidProtocolBufferException;
import com.hedera.hashgraph.sdk.*;
import com.hedera.hashgraph.seven_twenty_one.contract.handler.*;
import com.hedera.hashgraph.seven_twenty_one.contract.repository.TransactionRepository;
import com.hedera.hashgraph.seven_twenty_one.proto.Function;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class TopicListener {

    private static final Logger logger = LogManager.getLogger(
        TopicListener.class
    );

    // map of incoming function case to the handler that will accept
    private final Map<FunctionBody.DataCase, FunctionHandler<?>> functionHandlers = Map.ofEntries(
        Map.entry(
            FunctionBody.DataCase.CONSTRUCT,
            new ConstructorFunctionHandler()
        ),
        Map.entry(FunctionBody.DataCase.APPROVE, new ApproveFunctionHandler()),
        Map.entry(
            FunctionBody.DataCase.SETAPPROVALFORALL,
            new SetApprovalForAllFunctionHandler()
        ),
        Map.entry(FunctionBody.DataCase.MINT, new MintFunctionHandler()),
        Map.entry(FunctionBody.DataCase.BURN, new BurnFunctionHandler()),
        Map.entry(
            FunctionBody.DataCase.TRANSFERFROM,
            new TransferFromFunctionHandler()
        )
    );

    // the current state of the contract
    private final State state;

    // client to interact with the hedera network
    private final Client hederaClient;

    // the topic that we are listening to for function calls
    private final TopicId hederaTopicId;

    // handle of the open subscription to the topic
    @Nullable
    private SubscriptionHandle hederaSubscriptionHandle;

    private final TransactionRepository transactionRepository;

    public TopicListener(
        State state,
        Client hederaClient,
        TopicId hederaTopicId,
        TransactionRepository transactionRepository
    ) {
        this.state = state;
        this.hederaClient = hederaClient;
        this.hederaTopicId = hederaTopicId;
        this.transactionRepository = transactionRepository;
    }

    public synchronized void startListening() {
        stopListening();

        logger.info("Listening to Topic {}", hederaTopicId);

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
            logger.warn(
                "Ignoring invalid message at sequence " +
                topicMessage.sequenceNumber
            );
            return;
        }

        var expectedTransactionId = new TransactionId(
            new AccountId(functionBody.getOperatorAccountNum()),
            Instant.ofEpochSecond(0, functionBody.getValidStartNanos())
        );

        var transactionStatus = Status.OK;
        var functionCaller = functionBody.getCaller();

        var maybeCaller = Optional
            .ofNullable(functionCaller.isEmpty() ? null : functionCaller)
            .map(Address::fromByteString);

        // get a function handler that matches the data case
        @SuppressWarnings("unchecked")
        var functionHandler = Objects.requireNonNull(
            (FunctionHandler<Object>) functionHandlers.get(
                functionBody.getDataCase()
            )
        );

        // parse the function arguments from the protobuf data
        var functionArguments = functionHandler.parse(functionBody);

        try {
            if (
                !(
                    expectedTransactionId.accountId.equals(
                        Objects.requireNonNull(topicMessage.transactionId)
                            .accountId
                    ) &&
                    expectedTransactionId.validStart.equals(
                        topicMessage.transactionId.validStart
                    )
                )
            ) {
                // the transaction ID in the function body does not match the transaction ID that this
                // function was submitted under

                // this is a protection against DUPLICATE transactions so we treat this as a true error
                throw new StatusException(Status.TRANSACTION_ID_MISMATCH);
            }

            if (maybeCaller.isEmpty()) {
                // caller address we not set, immediately invalid
                throw new StatusException(Status.CALLER_NOT_SET);
            }

            var caller = maybeCaller.get();
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
                throw new StatusException(Status.INVALID_SIGNATURE);
            }

            // validate the function arguments
            // this will raise an exception that we need to handle and produce a failed function result
            functionHandler.validate(state, caller, functionArguments);

            // acquire a lock to update our state
            // we do this to block a snapshot from happening in the middle of
            // a state update
            state.lock();

            try {
                // finally, call the function
                functionHandler.call(state, caller, functionArguments);
            } finally {
                // release our state lock so that a snapshot may now happen
                state.unlock();
            }
        } catch (StatusException e) {
            transactionStatus = e.status;
        }

        // state has now transitioned
        state.setTimestamp(topicMessage.consensusTimestamp);

        try {
            // persist the transaction to our database
            transactionRepository.put(
                topicMessage.consensusTimestamp,
                maybeCaller.orElse(null),
                topicMessage.transactionId,
                transactionStatus,
                functionBody.getDataCase(),
                functionArguments
            );
        } catch (SQLException e) {
            // re-raise as unchecked to propagate up and terminate the process
            throw new RuntimeException(e);
        }
    }
}
