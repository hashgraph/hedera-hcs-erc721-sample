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
import com.hedera.hashgraph.seven_twenty_one.proto.MintFunctionData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public class MintTest {

    State state = new State();
    TopicListener topicListener = new TopicListener(
        state,
        null,
        new TopicId(0),
        null
    );

    @Test
    public void mintTest() throws InvalidProtocolBufferException {
        var callerKey = PrivateKey.generate();
        var callerAddress = new Address(callerKey.getPublicKey());
        var callerAccount = AccountId.fromString("0.0.5005");
        var toKey = PrivateKey.generate();
        var toAddress = new Address(toKey.getPublicKey());
        var baseURI = "baseURI";
        var tokenId = new Int(BigInteger.valueOf(555));

        // prepare state
        var tokenName = "tokenName";
        var tokenSymbol = "tokenSymbol";

        // Build constructor function
        var constructorFunctionDataBuilder = ConstructorFunctionData.newBuilder();

        var constructorFunctionData = constructorFunctionDataBuilder
            .setTokenName(tokenName)
            .setTokenSymbol(tokenSymbol)
            .setBaseURI(baseURI)
            .build();

        var constructorTransactionId = TransactionId.generate(callerAccount);
        var constructorValidStartNanos = ChronoUnit.NANOS.between(
            Instant.EPOCH,
            constructorTransactionId.validStart
        );

        var constructorFunctionBody = FunctionBody
            .newBuilder()
            .setCaller(ByteString.copyFrom(callerKey.getPublicKey().toBytes()))
            .setOperatorAccountNum(callerAccount.num)
            .setValidStartNanos(constructorValidStartNanos)
            .setConstruct(constructorFunctionData)
            .build();

        var constructorFunctionBodyBytes = constructorFunctionBody.toByteArray();
        var constructorFunctionSignature = callerKey.sign(
            constructorFunctionBodyBytes
        );

        var constructorFunction = Function
            .newBuilder()
            .setBody(ByteString.copyFrom(constructorFunctionBodyBytes))
            .setSignature(ByteString.copyFrom(constructorFunctionSignature))
            .build();

        // Build mint function

        var mintFunctionDataBuilder = MintFunctionData.newBuilder();

        var mintFunctionData = mintFunctionDataBuilder
            .setId(ByteString.copyFrom(tokenId.value.toByteArray()))
            .setTo(toAddress.toByteString())
            .build();

        var mintTransactionId = TransactionId.generate(callerAccount);
        var mintValidStartNanos = ChronoUnit.NANOS.between(
            Instant.EPOCH,
            mintTransactionId.validStart
        );

        var mintFunctionBody = FunctionBody
            .newBuilder()
            .setCaller(ByteString.copyFrom(callerKey.getPublicKey().toBytes()))
            .setOperatorAccountNum(callerAccount.num)
            .setValidStartNanos(mintValidStartNanos)
            .setMint(mintFunctionData)
            .build();

        var mintFunctionBodyBytes = mintFunctionBody.toByteArray();
        var mintFunctionSignature = callerKey.sign(mintFunctionBodyBytes);

        var mintFunction = Function
            .newBuilder()
            .setBody(ByteString.copyFrom(mintFunctionBodyBytes))
            .setSignature(ByteString.copyFrom(mintFunctionSignature))
            .build();

        // Construct before Pre-Check
        topicListener.handleFunction(
            constructorFunction,
            Instant.ofEpochMilli(constructorValidStartNanos),
            constructorTransactionId,
                1
        );

        // Pre-Check

        // i. Owner != 0x
        Assertions.assertNotNull(state.getOwner());

        // ii. to != 0x
        Assertions.assertNotNull(toAddress);

        // iii. caller = Owner
        Assertions.assertEquals(state.getOwner(), callerAddress);

        // Prepare for post-check
        var preHolderTokens = state.getTokens(toAddress);
        Set<Int> postHolderTokens = new HashSet<>(preHolderTokens);
        postHolderTokens.add(tokenId);

        // Update State
        topicListener.handleFunction(
            mintFunction,
            Instant.ofEpochMilli(mintValidStartNanos),
            mintTransactionId,
                1
        );

        // Post-Check

        // i. HolderTokens[to]’ = HolderTokens[to] + {id}
        Assertions.assertEquals(postHolderTokens, state.getTokens(toAddress));

        // ii. TokenOwners[id] = to
        Assertions.assertEquals(toAddress, state.getTokenOwner(tokenId));
    }

    @Test
    public void mintSpecificTokenId() throws InvalidProtocolBufferException {
        var callerKey = PrivateKey.generate();
        var callerAddress = new Address(callerKey.getPublicKey());
        var callerAccount = AccountId.fromString("0.0.5005");
        var toKey = PrivateKey.generate();
        var toAddress = new Address(toKey.getPublicKey());
        var baseURI = "baseURI";
        var tokenIdByteString = ByteString.copyFrom(
            new byte[] {
                17,
                (byte) 227,
                (byte) 244,
                78,
                59,
                (byte) 168,
                57,
                (byte) 222,
                90,
                79,
                (byte) 136,
                (byte) 190,
                68,
                (byte) 191,
                65,
                (byte) 201,
                (byte) 230,
                0,
                43,
                115,
                19,
                (byte) 131,
                (byte) 241,
                (byte) 219,
                117,
                0,
                38,
                (byte) 251,
                63,
                (byte) 205,
                (byte) 133,
                (byte) 171,
            }
        );
        var tokenId = Int.fromByteString(tokenIdByteString);

        // prepare state
        var tokenName = "tokenName";
        var tokenSymbol = "tokenSymbol";

        // Build constructor function
        var constructorFunctionDataBuilder = ConstructorFunctionData.newBuilder();

        var constructorFunctionData = constructorFunctionDataBuilder
            .setTokenName(tokenName)
            .setTokenSymbol(tokenSymbol)
            .setBaseURI(baseURI)
            .build();

        var constructorTransactionId = TransactionId.generate(callerAccount);
        var constructorValidStartNanos = ChronoUnit.NANOS.between(
            Instant.EPOCH,
            constructorTransactionId.validStart
        );

        var constructorFunctionBody = FunctionBody
            .newBuilder()
            .setCaller(ByteString.copyFrom(callerKey.getPublicKey().toBytes()))
            .setOperatorAccountNum(callerAccount.num)
            .setValidStartNanos(constructorValidStartNanos)
            .setConstruct(constructorFunctionData)
            .build();

        var constructorFunctionBodyBytes = constructorFunctionBody.toByteArray();
        var constructorFunctionSignature = callerKey.sign(
            constructorFunctionBodyBytes
        );

        var constructorFunction = Function
            .newBuilder()
            .setBody(ByteString.copyFrom(constructorFunctionBodyBytes))
            .setSignature(ByteString.copyFrom(constructorFunctionSignature))
            .build();

        // Build mint function

        var mintFunctionDataBuilder = MintFunctionData.newBuilder();

        var mintFunctionData = mintFunctionDataBuilder
            .setId(ByteString.copyFrom(tokenId.value.toByteArray()))
            .setTo(toAddress.toByteString())
            .build();

        var mintTransactionId = TransactionId.generate(callerAccount);
        var mintValidStartNanos = ChronoUnit.NANOS.between(
            Instant.EPOCH,
            mintTransactionId.validStart
        );

        var mintFunctionBody = FunctionBody
            .newBuilder()
            .setCaller(ByteString.copyFrom(callerKey.getPublicKey().toBytes()))
            .setOperatorAccountNum(callerAccount.num)
            .setValidStartNanos(mintValidStartNanos)
            .setMint(mintFunctionData)
            .build();

        var mintFunctionBodyBytes = mintFunctionBody.toByteArray();
        var mintFunctionSignature = callerKey.sign(mintFunctionBodyBytes);

        var mintFunction = Function
            .newBuilder()
            .setBody(ByteString.copyFrom(mintFunctionBodyBytes))
            .setSignature(ByteString.copyFrom(mintFunctionSignature))
            .build();

        // Construct before Pre-Check
        topicListener.handleFunction(
            constructorFunction,
            Instant.ofEpochMilli(constructorValidStartNanos),
            constructorTransactionId,
                1
        );

        // Pre-Check

        // i. Owner != 0x
        Assertions.assertNotNull(state.getOwner());

        // ii. to != 0x
        Assertions.assertNotNull(toAddress);

        // iii. caller = Owner
        Assertions.assertEquals(state.getOwner(), callerAddress);

        // Prepare for post-check
        var preHolderTokens = state.getTokens(toAddress);
        Set<Int> postHolderTokens = new HashSet<>(preHolderTokens);
        postHolderTokens.add(tokenId);

        // Update State
        topicListener.handleFunction(
            mintFunction,
            Instant.ofEpochMilli(mintValidStartNanos),
            mintTransactionId,
                1
        );

        // Post-Check

        // i. HolderTokens[to]’ = HolderTokens[to] + {id}
        Assertions.assertEquals(postHolderTokens, state.getTokens(toAddress));

        // ii. TokenOwners[id] = to
        Assertions.assertEquals(toAddress, state.getTokenOwner(tokenId));
    }
}
