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
    TopicListener topicListener = new TopicListener(state, null, new TopicId(0), null);

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
        var constructorFunctionSignature = callerKey.sign(constructorFunctionBodyBytes);

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
        topicListener.handleFunction(constructorFunction, Instant.ofEpochMilli(constructorValidStartNanos), constructorTransactionId);

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
        topicListener.handleFunction(mintFunction, Instant.ofEpochMilli(mintValidStartNanos), mintTransactionId);


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
        var tokenIdByteString = ByteString.copyFrom(new byte[]{17, (byte) 227, (byte) 244, 78, 59, (byte) 168, 57, (byte) 222, 90, 79, (byte) 136, (byte) 190, 68, (byte) 191, 65, (byte) 201, (byte) 230, 0, 43, 115, 19, (byte) 131, (byte) 241, (byte) 219, 117, 0, 38, (byte) 251, 63, (byte) 205, (byte) 133, (byte) 171});
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
        var constructorFunctionSignature = callerKey.sign(constructorFunctionBodyBytes);

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
        topicListener.handleFunction(constructorFunction, Instant.ofEpochMilli(constructorValidStartNanos), constructorTransactionId);

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
        topicListener.handleFunction(mintFunction, Instant.ofEpochMilli(mintValidStartNanos), mintTransactionId);


        // Post-Check

        // i. HolderTokens[to]’ = HolderTokens[to] + {id}
        Assertions.assertEquals(postHolderTokens, state.getTokens(toAddress));

        // ii. TokenOwners[id] = to
        Assertions.assertEquals(toAddress, state.getTokenOwner(tokenId));
    }

    @Test
    public void mintSpecificFunctionHex() throws InvalidProtocolBufferException {
        var callerKey = PrivateKey.generate();
        var callerAddress = new Address(callerKey.getPublicKey());
        var callerAccount = AccountId.fromString("0.0.5005");
        var toKey = PrivateKey.generate();
        var toAddress = new Address(toKey.getPublicKey());
        var baseURI = "baseURI";
        var tokenId = new Int(new BigInteger("8092078844887308171527841444894602301715842772692533675323768249341713417643"));

        var functionByteArray = hexStringToByteArray("0a750a207c474bebb06eb653445e200f407ef35a0209fcb01c5fa25742d7c78dcd8e32de10d04c1880e8b0b28a84f3a7163a440a209ad6ccda96dd9d1919a4c65419ac2c37aefded8ac0527412ffdbf19cdd7e6fb9122011e3f44e3ba839de5a4f88be44bf41c9e6002b731383f1db750026fb3fcd85ab12b50177814b83d97680b8cf7bb0ef04579b115ff95de9098788b510749e7c032f3c848833b983096758c45c08a8f6e83ff257ded0d7cd7cfbe55e91b26a7d67ca50000a207c474bebb06eb653445e200f407ef35a0209fcb01c5fa25742d7c78dcd8e32de10d04c1880e8b0b28a84f3a7163a440a209ad6ccda96dd9d1919a4c65419ac2c37aefded8ac0527412ffdbf19cdd7e6fb9122011e3f44e3ba839de5a4f88be44bf41c9e6002b731383f1db750026fb3fcd85ab");

        // prepare state
        var tokenName = "tokenName";
        var tokenSymbol = "tokenSymbol";

        var mintFunction =  Function.parseFrom(functionByteArray);

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
        var constructorFunctionSignature = callerKey.sign(constructorFunctionBodyBytes);

        var constructorFunction = Function
                .newBuilder()
                .setBody(ByteString.copyFrom(constructorFunctionBodyBytes))
                .setSignature(ByteString.copyFrom(constructorFunctionSignature))
                .build();

        // Mint Function already built, just need TransactionId and ValidStart

        var mintTransactionId = TransactionId.generate(callerAccount);
        var mintValidStartNanos = ChronoUnit.NANOS.between(
                Instant.EPOCH,
                mintTransactionId.validStart
        );


        // Construct before Pre-Check
        topicListener.handleFunction(constructorFunction, Instant.ofEpochMilli(constructorValidStartNanos), constructorTransactionId);

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
        topicListener.handleFunction(mintFunction, Instant.ofEpochMilli(mintValidStartNanos), mintTransactionId);


        // Post-Check

        // i. HolderTokens[to]’ = HolderTokens[to] + {id}
        Assertions.assertEquals(postHolderTokens, state.getTokens(toAddress));

        // ii. TokenOwners[id] = to
        Assertions.assertEquals(toAddress, state.getTokenOwner(tokenId));
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
