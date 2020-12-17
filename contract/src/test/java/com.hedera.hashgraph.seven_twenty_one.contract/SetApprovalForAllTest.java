package com.hedera.hashgraph.seven_twenty_one.contract;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TopicId;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.seven_twenty_one.proto.*;
import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SetApprovalForAllTest {

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

        // Build setApprovalForAll function

        var setApprovalForAllFunctionDataBuilder = SetApprovalForAllFunctionData.newBuilder();

        var setApprovalForAllFunctionData = setApprovalForAllFunctionDataBuilder
            .setOperator(toAddress.toByteString())
            .setApproved(true)
            .build();

        var setApprovalForAllTransactionId = TransactionId.generate(
            callerAccount
        );
        var setApprovalForAllValidStartNanos = ChronoUnit.NANOS.between(
            Instant.EPOCH,
            setApprovalForAllTransactionId.validStart
        );

        var setApprovalForAllFunctionBody = FunctionBody
            .newBuilder()
            .setCaller(ByteString.copyFrom(callerKey.getPublicKey().toBytes()))
            .setOperatorAccountNum(callerAccount.num)
            .setValidStartNanos(setApprovalForAllValidStartNanos)
            .setSetApprovalForAll(setApprovalForAllFunctionData)
            .build();

        var setApprovalForAllFunctionBodyBytes = setApprovalForAllFunctionBody.toByteArray();
        var setApprovalForAllFunctionSignature = callerKey.sign(
            setApprovalForAllFunctionBodyBytes
        );

        var setApprovalForAllFunction = Function
            .newBuilder()
            .setBody(ByteString.copyFrom(setApprovalForAllFunctionBodyBytes))
            .setSignature(
                ByteString.copyFrom(setApprovalForAllFunctionSignature)
            )
            .build();

        // Construct before Pre-Check
        topicListener.handleFunction(
            constructorFunction,
            Instant.ofEpochMilli(constructorValidStartNanos),
            constructorTransactionId
        );
        topicListener.handleFunction(
            mintFunction,
            Instant.ofEpochMilli(mintValidStartNanos),
            mintTransactionId
        );

        // Pre-Check

        // i. Owner != 0x
        Assertions.assertNotNull(state.getOwner());

        // ii. caller != operator
        Assertions.assertNotEquals(toAddress, callerAddress);

        // Update State
        topicListener.handleFunction(
            setApprovalForAllFunction,
            Instant.ofEpochMilli(setApprovalForAllValidStartNanos),
            setApprovalForAllTransactionId
        );

        // Post-Check

        // i. OperatorApprovals[caller][operator] = approved
        Assertions.assertTrue(
            state.getOperatorApprovals(callerAddress, toAddress)
        );
    }
}
