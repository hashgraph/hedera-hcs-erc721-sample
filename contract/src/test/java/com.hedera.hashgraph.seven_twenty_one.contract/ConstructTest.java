package com.hedera.hashgraph.seven_twenty_one.contract;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TopicId;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.seven_twenty_one.proto.ConstructorFunctionData;
import com.hedera.hashgraph.seven_twenty_one.proto.Function;
import com.hedera.hashgraph.seven_twenty_one.proto.FunctionBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ConstructTest {
    State state = new State();
    TopicListener topicListener = new TopicListener(state, null, new TopicId(0), null);

    @Test
    public void constructTest() throws InvalidProtocolBufferException {
        var callerKey = PrivateKey.generate();
        var callerAddress = new Address(callerKey.getPublicKey());
        var callerAccount = AccountId.fromString("0.0.5005");
        var baseURI = "baseURI";

        // prepare state
        var tokenName = "tokenName";
        var tokenSymbol = "tokenSymbol";

        var functionDataBuilder = ConstructorFunctionData.newBuilder();

        var functionData = functionDataBuilder
                .setTokenName(tokenName)
                .setTokenSymbol(tokenSymbol)
                .setBaseURI(baseURI)
                .build();

        var transactionId = TransactionId.generate(callerAccount);
        var validStartNanos = ChronoUnit.NANOS.between(
                Instant.EPOCH,
                transactionId.validStart
        );

        var functionBody = FunctionBody
                .newBuilder()
                .setCaller(ByteString.copyFrom(callerKey.getPublicKey().toBytes()))
                .setOperatorAccountNum(callerAccount.num)
                .setValidStartNanos(validStartNanos)
                .setConstruct(functionData)
                .build();

        var functionBodyBytes = functionBody.toByteArray();
        var functionSignature = callerKey.sign(functionBodyBytes);

        var constructorFunction = Function
                .newBuilder()
                .setBody(ByteString.copyFrom(functionBodyBytes))
                .setSignature(ByteString.copyFrom(functionSignature))
                .build();

        // Pre-Check

        // i. Owner = 0x
        Assertions.assertNull(state.getOwner());

        // ii. caller != 0x
        Assertions.assertNotNull(callerAddress);


        // Update State
        topicListener.handleFunction(constructorFunction, Instant.ofEpochMilli(validStartNanos), transactionId);


        // Post-Check

        // i. Owner = caller
        Assertions.assertEquals(callerAddress, state.getOwner());

        // ii. HolderTokens = {}
        Assertions.assertTrue(state.isHolderTokensEmpty());

        // iii. TokenOwners = {}
        Assertions.assertTrue(state.isTokenOwnersEmpty());

        // iv. OperatorApprovals = {}
        Assertions.assertTrue(state.isOperatorApprovalsEmpty());

        // v. TokenApprovals = {}
        Assertions.assertTrue(state.isTokenApprovalsEmpty());

        // vi. TokenName = tokenName
        Assertions.assertEquals(tokenName, state.getTokenName());

        // vii. TokenSymbol = tokenSymbol
        Assertions.assertEquals(tokenSymbol, state.getTokenSymbol());

        // viii. BaseURI = baseURI
        Assertions.assertEquals(baseURI, state.getBaseURI());

        // ix. TokenURIs = {}
        Assertions.assertTrue(state.isTokenURIsEmpty());
    }
}
