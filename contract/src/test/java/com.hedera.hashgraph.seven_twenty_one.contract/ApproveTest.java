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
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApproveTest {

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
        var callerAccount = AccountId.fromString("0.0.5005");
        var toKey = PrivateKey.generate();
        var toAddress = new Address(toKey.getPublicKey());
        var toAccount = AccountId.fromString("0.0.5006");
        var spenderKey = PrivateKey.generate();
        var spenderAddress = new Address(spenderKey.getPublicKey());
        var approvedKey = PrivateKey.generate();
        var approvedAddress = new Address(approvedKey.getPublicKey());
        var approvedAccount = AccountId.fromString("0.0.5006");
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

        // Build approve function

        var approveFunctionDataBuilder = ApproveFunctionData.newBuilder();

        var approveFunctionData = approveFunctionDataBuilder
            .setSpender(spenderAddress.toByteString())
            .setId(ByteString.copyFrom(tokenId.value.toByteArray()))
            .build();

        var approveTransactionId = TransactionId.generate(toAccount);
        var approveValidStartNanos = ChronoUnit.NANOS.between(
            Instant.EPOCH,
            approveTransactionId.validStart
        );

        var approveFunctionBody = FunctionBody
            .newBuilder()
            .setCaller(ByteString.copyFrom(toKey.getPublicKey().toBytes()))
            .setOperatorAccountNum(toAccount.num)
            .setValidStartNanos(approveValidStartNanos)
            .setApprove(approveFunctionData)
            .build();

        var approveFunctionBodyBytes = approveFunctionBody.toByteArray();
        var approveFunctionSignature = toKey.sign(approveFunctionBodyBytes);

        var approveFunction = Function
            .newBuilder()
            .setBody(ByteString.copyFrom(approveFunctionBodyBytes))
            .setSignature(ByteString.copyFrom(approveFunctionSignature))
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

        // ii. TokenOwners[id] != 0x
        Assertions.assertNotNull(state.getTokenOwner(tokenId));

        // iii. caller = TokenOwners[id] || OperatorApprovals[TokenOwners[id]][caller]
        // caller is toAddress for approve function in this case
        Assertions.assertEquals(state.getTokenOwner(tokenId), toAddress);

        // iv. spender != TokenOwners[id]
        Assertions.assertNotEquals(
            state.getTokenOwner(tokenId),
            spenderAddress
        );

        // Update State
        topicListener.handleFunction(
            approveFunction,
            Instant.ofEpochMilli(approveValidStartNanos),
            approveTransactionId
        );

        // Post-Check

        // i. TokenApprovals[id] = spender
        Assertions.assertEquals(
            spenderAddress,
            state.getTokenApprovals(tokenId)
        );

        // Test again for OperatorApprovals
        state = new State();
        topicListener = new TopicListener(state, null, new TopicId(0), null);

        var setApprovalForAllFunctionDataBuilder = SetApprovalForAllFunctionData.newBuilder();

        var setApprovalForAllFunctionData = setApprovalForAllFunctionDataBuilder
            .setOperator(approvedAddress.toByteString())
            .setApproved(true)
            .build();

        var setApprovalForAllTransactionId = TransactionId.generate(toAccount);
        var setApprovalForAllValidStartNanos = ChronoUnit.NANOS.between(
            Instant.EPOCH,
            setApprovalForAllTransactionId.validStart
        );

        var setApprovalForAllFunctionBody = FunctionBody
            .newBuilder()
            .setCaller(ByteString.copyFrom(toKey.getPublicKey().toBytes()))
            .setOperatorAccountNum(approvedAccount.num)
            .setValidStartNanos(setApprovalForAllValidStartNanos)
            .setSetApprovalForAll(setApprovalForAllFunctionData)
            .build();

        var setApprovalForAllFunctionBodyBytes = setApprovalForAllFunctionBody.toByteArray();
        var setApprovalForAllFunctionSignature = toKey.sign(
            setApprovalForAllFunctionBodyBytes
        );

        var setApprovalForAllFunction = Function
            .newBuilder()
            .setBody(ByteString.copyFrom(setApprovalForAllFunctionBodyBytes))
            .setSignature(
                ByteString.copyFrom(setApprovalForAllFunctionSignature)
            )
            .build();

        approveTransactionId = TransactionId.generate(toAccount);
        approveValidStartNanos =
            ChronoUnit.NANOS.between(
                Instant.EPOCH,
                approveTransactionId.validStart
            );

        approveFunctionBody =
            FunctionBody
                .newBuilder()
                .setCaller(
                    ByteString.copyFrom(approvedKey.getPublicKey().toBytes())
                )
                .setOperatorAccountNum(approvedAccount.num)
                .setValidStartNanos(approveValidStartNanos)
                .setApprove(approveFunctionData)
                .build();

        approveFunctionBodyBytes = approveFunctionBody.toByteArray();
        approveFunctionSignature = approvedKey.sign(approveFunctionBodyBytes);

        approveFunction =
            Function
                .newBuilder()
                .setBody(ByteString.copyFrom(approveFunctionBodyBytes))
                .setSignature(ByteString.copyFrom(approveFunctionSignature))
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
        topicListener.handleFunction(
            setApprovalForAllFunction,
            Instant.ofEpochMilli(setApprovalForAllValidStartNanos),
            setApprovalForAllTransactionId
        );

        // Pre-Check

        // i. Owner != 0x
        Assertions.assertNotNull(state.getOwner());

        // ii. TokenOwners[id] != 0x
        Assertions.assertNotNull(state.getTokenOwner(tokenId));

        // iii. caller = TokenOwners[id] || OperatorApprovals[TokenOwners[id]][caller]
        // caller is toAddress for approve function in this case
        Assertions.assertTrue(
            state.getOperatorApprovals(
                Objects.requireNonNull(state.getTokenOwner(tokenId)),
                approvedAddress
            )
        );

        // iv. spender != TokenOwners[id]
        Assertions.assertNotEquals(
            state.getTokenOwner(tokenId),
            spenderAddress
        );

        // Update State
        topicListener.handleFunction(
            approveFunction,
            Instant.ofEpochMilli(approveValidStartNanos),
            approveTransactionId
        );

        // Post-Check

        // i. TokenApprovals[id] = spender
        Assertions.assertEquals(
            spenderAddress,
            state.getTokenApprovals(tokenId)
        );
    }
}
