/*
 * This file is generated by jOOQ.
 */
package com.hedera.hashgraph.seven_twenty_one.contract.db;


import com.hedera.hashgraph.seven_twenty_one.contract.db.tables.Address;
import com.hedera.hashgraph.seven_twenty_one.contract.db.tables.AddressTransaction;
import com.hedera.hashgraph.seven_twenty_one.contract.db.tables.Event;
import com.hedera.hashgraph.seven_twenty_one.contract.db.tables.EventApproval;
import com.hedera.hashgraph.seven_twenty_one.contract.db.tables.EventApprovalForAll;
import com.hedera.hashgraph.seven_twenty_one.contract.db.tables.EventConstructed;
import com.hedera.hashgraph.seven_twenty_one.contract.db.tables.EventTransfer;
import com.hedera.hashgraph.seven_twenty_one.contract.db.tables.FlywaySchemaHistory;
import com.hedera.hashgraph.seven_twenty_one.contract.db.tables.Transaction;
import com.hedera.hashgraph.seven_twenty_one.contract.db.tables.TransactionApprove;
import com.hedera.hashgraph.seven_twenty_one.contract.db.tables.TransactionBurn;
import com.hedera.hashgraph.seven_twenty_one.contract.db.tables.TransactionConstructor;
import com.hedera.hashgraph.seven_twenty_one.contract.db.tables.TransactionMint;
import com.hedera.hashgraph.seven_twenty_one.contract.db.tables.TransactionSetApprovalForAll;
import com.hedera.hashgraph.seven_twenty_one.contract.db.tables.TransactionTransferFrom;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>public</code> schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<Record, Long> IDENTITY_ADDRESS = Identities0.IDENTITY_ADDRESS;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<Record> ADDRESS_PKEY = UniqueKeys0.ADDRESS_PKEY;
    public static final UniqueKey<Record> ADDRESS_ADDRESS_KEY = UniqueKeys0.ADDRESS_ADDRESS_KEY;
    public static final UniqueKey<Record> ADDRESS_TRANSACTION_PKEY = UniqueKeys0.ADDRESS_TRANSACTION_PKEY;
    public static final UniqueKey<Record> EVENT_PKEY = UniqueKeys0.EVENT_PKEY;
    public static final UniqueKey<Record> EVENT_APPROVAL_PKEY = UniqueKeys0.EVENT_APPROVAL_PKEY;
    public static final UniqueKey<Record> EVENT_APPROVAL_FOR_ALL_PKEY = UniqueKeys0.EVENT_APPROVAL_FOR_ALL_PKEY;
    public static final UniqueKey<Record> EVENT_CONSTRUCTED_PKEY = UniqueKeys0.EVENT_CONSTRUCTED_PKEY;
    public static final UniqueKey<Record> EVENT_TRANSFER_PKEY = UniqueKeys0.EVENT_TRANSFER_PKEY;
    public static final UniqueKey<Record> FLYWAY_SCHEMA_HISTORY_PK = UniqueKeys0.FLYWAY_SCHEMA_HISTORY_PK;
    public static final UniqueKey<Record> TRANSACTION_PKEY = UniqueKeys0.TRANSACTION_PKEY;
    public static final UniqueKey<Record> TRANSACTION_APPROVE_PKEY = UniqueKeys0.TRANSACTION_APPROVE_PKEY;
    public static final UniqueKey<Record> TRANSACTION_BURN_PKEY = UniqueKeys0.TRANSACTION_BURN_PKEY;
    public static final UniqueKey<Record> TRANSACTION_CONSTRUCTOR_PKEY = UniqueKeys0.TRANSACTION_CONSTRUCTOR_PKEY;
    public static final UniqueKey<Record> TRANSACTION_MINT_PKEY = UniqueKeys0.TRANSACTION_MINT_PKEY;
    public static final UniqueKey<Record> TRANSACTION_SET_APPROVAL_FOR_ALL_PKEY = UniqueKeys0.TRANSACTION_SET_APPROVAL_FOR_ALL_PKEY;
    public static final UniqueKey<Record> TRANSACTION_TRANSFER_FROM_PKEY = UniqueKeys0.TRANSACTION_TRANSFER_FROM_PKEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<Record, Record> ADDRESS_TRANSACTION__ADDRESS_TRANSACTION_ADDRESS_ID_FKEY = ForeignKeys0.ADDRESS_TRANSACTION__ADDRESS_TRANSACTION_ADDRESS_ID_FKEY;
    public static final ForeignKey<Record, Record> EVENT_APPROVAL__EVENT_APPROVAL_OWNER_ADDRESS_ID_FKEY = ForeignKeys0.EVENT_APPROVAL__EVENT_APPROVAL_OWNER_ADDRESS_ID_FKEY;
    public static final ForeignKey<Record, Record> EVENT_APPROVAL__EVENT_APPROVAL_APPROVED_ADDRESS_ID_FKEY = ForeignKeys0.EVENT_APPROVAL__EVENT_APPROVAL_APPROVED_ADDRESS_ID_FKEY;
    public static final ForeignKey<Record, Record> EVENT_APPROVAL_FOR_ALL__EVENT_APPROVAL_FOR_ALL_OWNER_ADDRESS_ID_FKEY = ForeignKeys0.EVENT_APPROVAL_FOR_ALL__EVENT_APPROVAL_FOR_ALL_OWNER_ADDRESS_ID_FKEY;
    public static final ForeignKey<Record, Record> EVENT_APPROVAL_FOR_ALL__EVENT_APPROVAL_FOR_ALL_OPERATOR_ADDRESS_ID_FKEY = ForeignKeys0.EVENT_APPROVAL_FOR_ALL__EVENT_APPROVAL_FOR_ALL_OPERATOR_ADDRESS_ID_FKEY;
    public static final ForeignKey<Record, Record> EVENT_TRANSFER__EVENT_TRANSFER_FROM_ADDRESS_ID_FKEY = ForeignKeys0.EVENT_TRANSFER__EVENT_TRANSFER_FROM_ADDRESS_ID_FKEY;
    public static final ForeignKey<Record, Record> EVENT_TRANSFER__EVENT_TRANSFER_TO_ADDRESS_ID_FKEY = ForeignKeys0.EVENT_TRANSFER__EVENT_TRANSFER_TO_ADDRESS_ID_FKEY;
    public static final ForeignKey<Record, Record> TRANSACTION__TRANSACTION_CALLER_ADDRESS_ID_FKEY = ForeignKeys0.TRANSACTION__TRANSACTION_CALLER_ADDRESS_ID_FKEY;
    public static final ForeignKey<Record, Record> TRANSACTION_APPROVE__TRANSACTION_APPROVE_SPENDER_ADDRESS_ID_FKEY = ForeignKeys0.TRANSACTION_APPROVE__TRANSACTION_APPROVE_SPENDER_ADDRESS_ID_FKEY;
    public static final ForeignKey<Record, Record> TRANSACTION_MINT__TRANSACTION_MINT_TO_ADDRESS_ID_FKEY = ForeignKeys0.TRANSACTION_MINT__TRANSACTION_MINT_TO_ADDRESS_ID_FKEY;
    public static final ForeignKey<Record, Record> TRANSACTION_SET_APPROVAL_FOR_ALL__TRANSACTION_SET_APPROVAL_FOR_ALL_OPERATOR_ADDRESS_ID_FKEY = ForeignKeys0.TRANSACTION_SET_APPROVAL_FOR_ALL__TRANSACTION_SET_APPROVAL_FOR_ALL_OPERATOR_ADDRESS_ID_FKEY;
    public static final ForeignKey<Record, Record> TRANSACTION_TRANSFER_FROM__TRANSACTION_TRANSFER_FROM_FROM_ADDRESS_ID_FKEY = ForeignKeys0.TRANSACTION_TRANSFER_FROM__TRANSACTION_TRANSFER_FROM_FROM_ADDRESS_ID_FKEY;
    public static final ForeignKey<Record, Record> TRANSACTION_TRANSFER_FROM__TRANSACTION_TRANSFER_FROM_TO_ADDRESS_ID_FKEY = ForeignKeys0.TRANSACTION_TRANSFER_FROM__TRANSACTION_TRANSFER_FROM_TO_ADDRESS_ID_FKEY;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 {
        public static Identity<Record, Long> IDENTITY_ADDRESS = Internal.createIdentity(Address.ADDRESS, Address.ADDRESS.ID);
    }

    private static class UniqueKeys0 {
        public static final UniqueKey<Record> ADDRESS_PKEY = Internal.createUniqueKey(Address.ADDRESS, "address_pkey", new TableField[] { Address.ADDRESS.ID }, true);
        public static final UniqueKey<Record> ADDRESS_ADDRESS_KEY = Internal.createUniqueKey(Address.ADDRESS, "address_address_key", new TableField[] { Address.ADDRESS.ADDRESS_ }, true);
        public static final UniqueKey<Record> ADDRESS_TRANSACTION_PKEY = Internal.createUniqueKey(AddressTransaction.ADDRESS_TRANSACTION, "address_transaction_pkey", new TableField[] { AddressTransaction.ADDRESS_TRANSACTION.ADDRESS_ID, AddressTransaction.ADDRESS_TRANSACTION.TRANSACTION_TIMESTAMP }, true);
        public static final UniqueKey<Record> EVENT_PKEY = Internal.createUniqueKey(Event.EVENT, "event_pkey", new TableField[] { Event.EVENT.TIMESTAMP }, true);
        public static final UniqueKey<Record> EVENT_APPROVAL_PKEY = Internal.createUniqueKey(EventApproval.EVENT_APPROVAL, "event_approval_pkey", new TableField[] { EventApproval.EVENT_APPROVAL.TIMESTAMP }, true);
        public static final UniqueKey<Record> EVENT_APPROVAL_FOR_ALL_PKEY = Internal.createUniqueKey(EventApprovalForAll.EVENT_APPROVAL_FOR_ALL, "event_approval_for_all_pkey", new TableField[] { EventApprovalForAll.EVENT_APPROVAL_FOR_ALL.TIMESTAMP }, true);
        public static final UniqueKey<Record> EVENT_CONSTRUCTED_PKEY = Internal.createUniqueKey(EventConstructed.EVENT_CONSTRUCTED, "event_constructed_pkey", new TableField[] { EventConstructed.EVENT_CONSTRUCTED.TIMESTAMP }, true);
        public static final UniqueKey<Record> EVENT_TRANSFER_PKEY = Internal.createUniqueKey(EventTransfer.EVENT_TRANSFER, "event_transfer_pkey", new TableField[] { EventTransfer.EVENT_TRANSFER.TIMESTAMP }, true);
        public static final UniqueKey<Record> FLYWAY_SCHEMA_HISTORY_PK = Internal.createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, "flyway_schema_history_pk", new TableField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK }, true);
        public static final UniqueKey<Record> TRANSACTION_PKEY = Internal.createUniqueKey(Transaction.TRANSACTION, "transaction_pkey", new TableField[] { Transaction.TRANSACTION.TIMESTAMP }, true);
        public static final UniqueKey<Record> TRANSACTION_APPROVE_PKEY = Internal.createUniqueKey(TransactionApprove.TRANSACTION_APPROVE, "transaction_approve_pkey", new TableField[] { TransactionApprove.TRANSACTION_APPROVE.TIMESTAMP }, true);
        public static final UniqueKey<Record> TRANSACTION_BURN_PKEY = Internal.createUniqueKey(TransactionBurn.TRANSACTION_BURN, "transaction_burn_pkey", new TableField[] { TransactionBurn.TRANSACTION_BURN.TIMESTAMP }, true);
        public static final UniqueKey<Record> TRANSACTION_CONSTRUCTOR_PKEY = Internal.createUniqueKey(TransactionConstructor.TRANSACTION_CONSTRUCTOR, "transaction_constructor_pkey", new TableField[] { TransactionConstructor.TRANSACTION_CONSTRUCTOR.TIMESTAMP }, true);
        public static final UniqueKey<Record> TRANSACTION_MINT_PKEY = Internal.createUniqueKey(TransactionMint.TRANSACTION_MINT, "transaction_mint_pkey", new TableField[] { TransactionMint.TRANSACTION_MINT.TIMESTAMP }, true);
        public static final UniqueKey<Record> TRANSACTION_SET_APPROVAL_FOR_ALL_PKEY = Internal.createUniqueKey(TransactionSetApprovalForAll.TRANSACTION_SET_APPROVAL_FOR_ALL, "transaction_set_approval_for_all_pkey", new TableField[] { TransactionSetApprovalForAll.TRANSACTION_SET_APPROVAL_FOR_ALL.TIMESTAMP }, true);
        public static final UniqueKey<Record> TRANSACTION_TRANSFER_FROM_PKEY = Internal.createUniqueKey(TransactionTransferFrom.TRANSACTION_TRANSFER_FROM, "transaction_transfer_from_pkey", new TableField[] { TransactionTransferFrom.TRANSACTION_TRANSFER_FROM.TIMESTAMP }, true);
    }

    private static class ForeignKeys0 {
        public static final ForeignKey<Record, Record> ADDRESS_TRANSACTION__ADDRESS_TRANSACTION_ADDRESS_ID_FKEY = Internal.createForeignKey(Keys.ADDRESS_PKEY, AddressTransaction.ADDRESS_TRANSACTION, "address_transaction_address_id_fkey", new TableField[] { AddressTransaction.ADDRESS_TRANSACTION.ADDRESS_ID }, true);
        public static final ForeignKey<Record, Record> EVENT_APPROVAL__EVENT_APPROVAL_OWNER_ADDRESS_ID_FKEY = Internal.createForeignKey(Keys.ADDRESS_PKEY, EventApproval.EVENT_APPROVAL, "event_approval_owner_address_id_fkey", new TableField[] { EventApproval.EVENT_APPROVAL.OWNER_ADDRESS_ID }, true);
        public static final ForeignKey<Record, Record> EVENT_APPROVAL__EVENT_APPROVAL_APPROVED_ADDRESS_ID_FKEY = Internal.createForeignKey(Keys.ADDRESS_PKEY, EventApproval.EVENT_APPROVAL, "event_approval_approved_address_id_fkey", new TableField[] { EventApproval.EVENT_APPROVAL.APPROVED_ADDRESS_ID }, true);
        public static final ForeignKey<Record, Record> EVENT_APPROVAL_FOR_ALL__EVENT_APPROVAL_FOR_ALL_OWNER_ADDRESS_ID_FKEY = Internal.createForeignKey(Keys.ADDRESS_PKEY, EventApprovalForAll.EVENT_APPROVAL_FOR_ALL, "event_approval_for_all_owner_address_id_fkey", new TableField[] { EventApprovalForAll.EVENT_APPROVAL_FOR_ALL.OWNER_ADDRESS_ID }, true);
        public static final ForeignKey<Record, Record> EVENT_APPROVAL_FOR_ALL__EVENT_APPROVAL_FOR_ALL_OPERATOR_ADDRESS_ID_FKEY = Internal.createForeignKey(Keys.ADDRESS_PKEY, EventApprovalForAll.EVENT_APPROVAL_FOR_ALL, "event_approval_for_all_operator_address_id_fkey", new TableField[] { EventApprovalForAll.EVENT_APPROVAL_FOR_ALL.OPERATOR_ADDRESS_ID }, true);
        public static final ForeignKey<Record, Record> EVENT_TRANSFER__EVENT_TRANSFER_FROM_ADDRESS_ID_FKEY = Internal.createForeignKey(Keys.ADDRESS_PKEY, EventTransfer.EVENT_TRANSFER, "event_transfer_from_address_id_fkey", new TableField[] { EventTransfer.EVENT_TRANSFER.FROM_ADDRESS_ID }, true);
        public static final ForeignKey<Record, Record> EVENT_TRANSFER__EVENT_TRANSFER_TO_ADDRESS_ID_FKEY = Internal.createForeignKey(Keys.ADDRESS_PKEY, EventTransfer.EVENT_TRANSFER, "event_transfer_to_address_id_fkey", new TableField[] { EventTransfer.EVENT_TRANSFER.TO_ADDRESS_ID }, true);
        public static final ForeignKey<Record, Record> TRANSACTION__TRANSACTION_CALLER_ADDRESS_ID_FKEY = Internal.createForeignKey(Keys.ADDRESS_PKEY, Transaction.TRANSACTION, "transaction_caller_address_id_fkey", new TableField[] { Transaction.TRANSACTION.CALLER_ADDRESS_ID }, true);
        public static final ForeignKey<Record, Record> TRANSACTION_APPROVE__TRANSACTION_APPROVE_SPENDER_ADDRESS_ID_FKEY = Internal.createForeignKey(Keys.ADDRESS_PKEY, TransactionApprove.TRANSACTION_APPROVE, "transaction_approve_spender_address_id_fkey", new TableField[] { TransactionApprove.TRANSACTION_APPROVE.SPENDER_ADDRESS_ID }, true);
        public static final ForeignKey<Record, Record> TRANSACTION_MINT__TRANSACTION_MINT_TO_ADDRESS_ID_FKEY = Internal.createForeignKey(Keys.ADDRESS_PKEY, TransactionMint.TRANSACTION_MINT, "transaction_mint_to_address_id_fkey", new TableField[] { TransactionMint.TRANSACTION_MINT.TO_ADDRESS_ID }, true);
        public static final ForeignKey<Record, Record> TRANSACTION_SET_APPROVAL_FOR_ALL__TRANSACTION_SET_APPROVAL_FOR_ALL_OPERATOR_ADDRESS_ID_FKEY = Internal.createForeignKey(Keys.ADDRESS_PKEY, TransactionSetApprovalForAll.TRANSACTION_SET_APPROVAL_FOR_ALL, "transaction_set_approval_for_all_operator_address_id_fkey", new TableField[] { TransactionSetApprovalForAll.TRANSACTION_SET_APPROVAL_FOR_ALL.OPERATOR_ADDRESS_ID }, true);
        public static final ForeignKey<Record, Record> TRANSACTION_TRANSFER_FROM__TRANSACTION_TRANSFER_FROM_FROM_ADDRESS_ID_FKEY = Internal.createForeignKey(Keys.ADDRESS_PKEY, TransactionTransferFrom.TRANSACTION_TRANSFER_FROM, "transaction_transfer_from_from_address_id_fkey", new TableField[] { TransactionTransferFrom.TRANSACTION_TRANSFER_FROM.FROM_ADDRESS_ID }, true);
        public static final ForeignKey<Record, Record> TRANSACTION_TRANSFER_FROM__TRANSACTION_TRANSFER_FROM_TO_ADDRESS_ID_FKEY = Internal.createForeignKey(Keys.ADDRESS_PKEY, TransactionTransferFrom.TRANSACTION_TRANSFER_FROM, "transaction_transfer_from_to_address_id_fkey", new TableField[] { TransactionTransferFrom.TRANSACTION_TRANSFER_FROM.TO_ADDRESS_ID }, true);
    }
}