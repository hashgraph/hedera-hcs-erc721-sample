import dotenv from "dotenv";
import $ from "./proto.js";
import {
    AccountId,
    Client,
    PrivateKey,
    TopicId,
    TopicMessageSubmitTransaction,
    TransactionId,
} from "@hashgraph/sdk";

// @ts-ignore
import BigInteger from "node-biginteger";

// fill process.env from /.env
dotenv.config();

// Declare a Hedera(tm) operator that will pay for the message submission

const hederaOperatorId = AccountId.fromString(process.env.OPERATOR_ID!);
const hederaOperatorKey = PrivateKey.fromString(process.env.OPERATOR_KEY!);

// Get the owner key for the contract
// This will let us use the mint transaction

const ownerKey = PrivateKey.fromString(process.env.OWNER_KEY!);

// Generate a Hedera(tm) Transaction ID
// This is tied inside the H721 protobuf to guard against duplicate invocations

const transactionId = TransactionId.generate(hederaOperatorId);

//
// We are going to send a mint transaction
//

// Generate a random ~address~ to mint our token to
const newKey = PrivateKey.generate();

// Addresses are simply the public key of a ed25519 private key
const newAddress = newKey.publicKey;

// We are going to mint token #5
const newTokenId = "5";

// Prepare the function body protobuf
const functionBody = $.hedera_721.FunctionBody.encode({
    caller: ownerKey.publicKey.toBytes(),
    operatorAccountNum: hederaOperatorId.num,
    validStartNanos: transactionId.validStart.nanos.add(
        transactionId.validStart.seconds.mul("1000000000")
    ),
    mint: $.hedera_721.MintFunctionData.create({
        id: BigInteger.fromString(newTokenId).toBuffer(),
        to: newAddress.toBytes(),
    }),
}).finish();

// Sign the function body
const functionSignature = ownerKey.sign(functionBody);

// Prepare and encode the function
const functionBytes = $.hedera_721.Function.encode({
    body: functionBody,
    signature: functionSignature,
}).finish();

// Submit this as a message to Hedera
const client = Client.forTestnet().setOperator(
    hederaOperatorId,
    hederaOperatorKey
);

new TopicMessageSubmitTransaction()
    .setTopicId(TopicId.fromString(process.env.TOPIC!))
    .setMessage(functionBytes)
    .setTransactionId(transactionId)
    .execute(client);
