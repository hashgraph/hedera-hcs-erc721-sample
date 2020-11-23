package com.hedera.hashgraph.seven_twenty_one.contract;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.*;
import com.hedera.hashgraph.seven_twenty_one.proto.ConstructorFunctionData;
import com.hedera.hashgraph.seven_twenty_one.proto.Function;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;
import io.github.cdimascio.dotenv.Dotenv;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class App {

    static final Logger logger = LogManager.getLogger(App.class);

    // access to the system environment overlaid with a .env file, if present
    final Dotenv env = Dotenv.configure().ignoreIfMissing().load();

    // client to the Hedera(tm) Hashgraph network
    // used when submitting transactions to hedera
    final Client hederaClient = createHederaClient();

    // current state of the hedera contract
    final State contractState = new State();

    // topic ID of the contract instance
    final TopicId topicId = getOrCreateContractInstance();

    // listener to the Hedera topic
    final TopicListener topicListener = new TopicListener(
        contractState,
        hederaClient,
        topicId
    );

    private App()
        throws HederaReceiptStatusException, TimeoutException, HederaPreCheckStatusException {}

    Client createHederaClient() {
        var networkName = env.get("H721_NETWORK", "testnet");
        Client client;

        // noinspection EnhancedSwitchMigration
        switch (networkName) {
            case "mainnet":
                client = Client.forMainnet();
                break;
            case "testnet":
                client = Client.forTestnet();
                break;
            default:
                throw new IllegalStateException(
                    "unknown hedera network name: " + networkName
                );
        }

        var operatorId = env.get("H721_OPERATOR_ID");
        var operatorKey = env.get("H721_OPERATOR_KEY");

        if (operatorId != null && operatorKey != null) {
            client.setOperator(
                AccountId.fromString(operatorId),
                PrivateKey.fromString(operatorKey)
            );
        }

        return client;
    }

    TopicId getOrCreateContractInstance()
        throws HederaReceiptStatusException, TimeoutException, HederaPreCheckStatusException {
        var maybeTopicId = Optional
            .ofNullable(env.get("H721_TOPIC"))
            .map(TopicId::fromString);

        if (maybeTopicId.isPresent()) {
            return maybeTopicId.get();
        }

        // TODO: log that we are creating a new topic

        return createContractInstance();
    }

    TopicId createContractInstance()
        throws TimeoutException, HederaPreCheckStatusException, HederaReceiptStatusException {
        var tokenName = requireEnv("H721_TOKEN_NAME");
        var tokenSymbol = requireEnv("H721_TOKEN_SYMBOL");
        var baseUri = env.get("H721_BASE_URI");
        var operatorKey = PrivateKey.fromString(
            requireEnv("H721_OPERATOR_KEY")
        );
        var operatorId = Objects.requireNonNull(
            hederaClient.getOperatorAccountId()
        );

        // step 1 is to create a fresh topic to use
        // nothing specific here, might want to use a memo to call out what we are (h721)

        var topicId = Objects.requireNonNull(
            new TopicCreateTransaction()
                .execute(hederaClient)
                .getReceipt(hederaClient)
                .topicId
        );

        // next we need to execute the <Constructor> function on this topic

        var functionDataBuilder = ConstructorFunctionData.newBuilder();

        if (baseUri != null) {
            functionDataBuilder.setBaseURI(baseUri);
        }

        var functionData = functionDataBuilder
            .setTokenName(tokenName)
            .setTokenSymbol(tokenSymbol)
            .build();

        var transactionId = TransactionId.generate(operatorId);
        var validStartNanos = ChronoUnit.NANOS.between(
            Instant.EPOCH,
            transactionId.validStart
        );

        var functionBody = FunctionBody
            .newBuilder()
            .setOperatorAccountNum(operatorId.num)
            .setValidStartNanos(validStartNanos)
            .setConstructor(functionData)
            .build();

        var functionBodyBytes = functionBody.toByteArray();
        var functionSignature = operatorKey.sign(functionBodyBytes);

        var function = Function
            .newBuilder()
            .setBody(ByteString.copyFrom(functionBodyBytes))
            .setSignature(ByteString.copyFrom(functionSignature))
            .build();

        new TopicMessageSubmitTransaction()
            .setMaxChunks(1)
            .setTopicId(topicId)
            .setTransactionId(transactionId)
            .setMessage(function.toByteArray())
            .execute(hederaClient)
            .getReceipt(hederaClient);

        logger.warn("Created new Topic {}", topicId);

        return topicId;
    }

    String requireEnv(String name) {
        return Objects.requireNonNull(
            env.get(name),
            "missing environment variable: " + name
        );
    }

    public static void main(String[] args)
        throws InterruptedException, HederaReceiptStatusException, TimeoutException, HederaPreCheckStatusException {
        var app = new App();

        // start listening to the contract instance (topic) on the Hedera
        // mirror node
        app.topicListener.startListening();

        // expose the read-only API for the contract state
        // app.deployStateVerticle();

        // wait while the APIs and the topic listener run in the background
        while (true) Thread.sleep(0);
    }
}
