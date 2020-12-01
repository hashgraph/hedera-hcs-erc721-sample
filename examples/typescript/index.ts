import dotenv from "dotenv";
import $ from "./proto.js";
import {
    AccountId,
    Client,
    PrivateKey,
    Timestamp,
    TopicId,
    TopicMessageSubmitTransaction,
    TransactionId,
} from "@hashgraph/sdk";
import yargs from "yargs/yargs"
import Long from "long";

// Get process arguments
const argv = yargs(process.argv).argv;
const functionHex = argv._[2];

// Fill process.env from /.env
dotenv.config();

// Declare a Hedera(tm) operator that will pay for the message submission
const hederaOperatorId = AccountId.fromString(process.env.OPERATOR_ID!);
const hederaOperatorKey = PrivateKey.fromString(process.env.OPERATOR_KEY!);

// Parse the transaction from Hex
const functionBytes = Buffer.from(functionHex, "hex");
const fn = $.hedera_721.Function.decode(functionBytes);
const fnBody = $.hedera_721.FunctionBody.decode(fn.body);

// Construct a transaction ID linked to that function body
const validStartNanos = Long.fromValue(fnBody.validStartNanos);
const sec = validStartNanos.div("1000000000");
const nsec = validStartNanos.mod("1000000000");

const transactionId = new TransactionId(
    new AccountId(fnBody.operatorAccountNum),
    new Timestamp(sec, nsec),
);

// Submit this as a message to Hedera
const client = Client.forTestnet().setOperator(
    hederaOperatorId,
    hederaOperatorKey
);

(async () => {
    const resp = await new TopicMessageSubmitTransaction()
        .setTopicId(TopicId.fromString(process.env.TOPIC!))
        .setMessage(functionBytes)
        .setTransactionId(transactionId)
        .execute(client);

    const rec = await resp.getReceipt(client);
    console.log(rec);
})();
