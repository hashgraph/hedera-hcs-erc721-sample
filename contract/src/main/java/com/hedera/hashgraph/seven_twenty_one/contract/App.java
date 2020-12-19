package com.hedera.hashgraph.seven_twenty_one.contract;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.*;
import com.hedera.hashgraph.seven_twenty_one.contract.api.ApiVerticle;
import com.hedera.hashgraph.seven_twenty_one.contract.repository.AddressRepository;
import com.hedera.hashgraph.seven_twenty_one.contract.repository.TransactionRepository;
import com.hedera.hashgraph.seven_twenty_one.proto.ConstructorFunctionData;
import com.hedera.hashgraph.seven_twenty_one.proto.Function;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;
import io.github.cdimascio.dotenv.Dotenv;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.postgresql.ds.PGSimpleDataSource;

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

    final DataSource dataSource = createJDBCDataSource();

    final ConnectionProvider jooqConnectionProvider = new DataSourceConnectionProvider(
        dataSource
    );

    final PgPool pgPool = createPgPool();

    final DSLContext jooqContext = DSL.using(
        jooqConnectionProvider,
        SQLDialect.POSTGRES
    );

    final AddressRepository addressRepository = new AddressRepository(
        jooqContext
    );

    final TransactionRepository transactionRepository = new TransactionRepository(
        jooqContext,
        addressRepository
    );

    // listener to the Hedera topic
    final TopicListener topicListener = new TopicListener(
        contractState,
        hederaClient,
        topicId,
        transactionRepository
    );

    // on an interval, we commit our state
    final CommitInterval commitInterval = new CommitInterval(
        env,
        contractState,
        transactionRepository
    );

    final Vertx vertx = Vertx.vertx();

    private App()
        throws HederaReceiptStatusException, TimeoutException, HederaPreCheckStatusException, InterruptedException {}

    Client createHederaClient() throws InterruptedException {
        var networkName = env.get("H721_NETWORK", "testnet");
        Client client;

        // noinspection EnhancedSwitchMigration
        switch (networkName) {
            case "mainnet":
                client =
                    Client.forNetwork(
                        Map.ofEntries(
                            Map.entry("35.237.200.180:50211", new AccountId(3)),
                            Map.entry("35.186.191.247:50211", new AccountId(4)),
                            Map.entry("35.192.2.25:50211", new AccountId(5)),
                            Map.entry("35.199.161.108:50211", new AccountId(6))
                        )
                    );

                client.setMirrorNetwork(
                    List.of("hcs.mainnet.mirrornode.hedera.com:5600")
                );
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
        var operatorId = Objects.requireNonNull(
            hederaClient.getOperatorAccountId()
        );

        var ownerKey = Optional
            .ofNullable(env.get("H721_OWNER_KEY"))
            .map(PrivateKey::fromString)
            .orElseGet(
                () -> {
                    var newKey = PrivateKey.generate();

                    logger
                        .always()
                        .log(
                            "No owner key provided; Generated, Private = {}, Public = {}",
                            newKey,
                            newKey.getPublicKey()
                        );

                    return newKey;
                }
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
            .setCaller(ByteString.copyFrom(ownerKey.getPublicKey().toBytes()))
            .setOperatorAccountNum(operatorId.num)
            .setValidStartNanos(validStartNanos)
            .setConstruct(functionData)
            .build();

        var functionBodyBytes = functionBody.toByteArray();
        var functionSignature = ownerKey.sign(functionBodyBytes);

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
            .execute(hederaClient, Duration.ofMinutes(5))
            .getReceipt(hederaClient, Duration.ofMinutes(5));

        logger.warn("Created new Topic {}", topicId);

        return topicId;
    }

    DataSource createJDBCDataSource() {
        var databaseUrl = requireEnv("H721_DATABASE_URL");
        var databaseUsername = requireEnv("H721_DATABASE_USERNAME");
        var databasePassword = env.get("H721_DATABASE_PASSWORD", "");

        var dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:" + databaseUrl);
        dataSource.setUser(databaseUsername);
        dataSource.setPassword(databasePassword);

        return dataSource;
    }

    PgPool createPgPool() {
        return PgPool.pool(
            PgConnectOptions
                .fromUri(requireEnv("H721_DATABASE_URL"))
                .setUser(requireEnv("H721_DATABASE_USERNAME"))
                .setPassword(env.get("H721_DATABASE_PASSWORD", "")),
            new PoolOptions().setMaxSize(10)
        );
    }

    String requireEnv(String name) {
        return Objects.requireNonNull(
            env.get(name),
            "missing environment variable: " + name
        );
    }

    void deployStateVerticle() {
        var port = Integer.parseInt(env.get("H721_STATE_PORT", "9000"));
        var deploymentOptions = new DeploymentOptions().setInstances(8);

        vertx.deployVerticle(
            () -> new ApiVerticle(contractState, port, pgPool),
            deploymentOptions
        );

        logger.info("State API Listening on :{}", port);
    }

    void runMigrations() {
        Flyway
            .configure()
            .dataSource(dataSource)
            .locations("classpath:migrations")
            .load()
            .migrate();
    }

    public static void main(String[] args)
        throws InterruptedException, HederaReceiptStatusException, TimeoutException, HederaPreCheckStatusException {
        var app = new App();

        // run migrations for the persistence layer
        app.runMigrations();

        // start listening to the contract instance (topic) on the Hedera
        // mirror node
        app.topicListener.startListening();

        // expose the read-only API for the contract state
        app.deployStateVerticle();

        // wait while the APIs and the topic listener run in the background
        while (true) Thread.sleep(0);
    }
}
