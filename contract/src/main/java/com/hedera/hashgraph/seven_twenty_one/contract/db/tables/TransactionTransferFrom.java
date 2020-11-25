/*
 * This file is generated by jOOQ.
 */
package com.hedera.hashgraph.seven_twenty_one.contract.db.tables;


import com.hedera.hashgraph.seven_twenty_one.contract.db.Keys;
import com.hedera.hashgraph.seven_twenty_one.contract.db.Public;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TransactionTransferFrom extends TableImpl<Record> {

    private static final long serialVersionUID = -1534472050;

    /**
     * The reference instance of <code>public.transaction_transfer_from</code>
     */
    public static final TransactionTransferFrom TRANSACTION_TRANSFER_FROM = new TransactionTransferFrom();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>public.transaction_transfer_from.timestamp</code>.
     */
    public final TableField<Record, Long> TIMESTAMP = createField(DSL.name("timestamp"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.transaction_transfer_from.from_address_id</code>.
     */
    public final TableField<Record, Long> FROM_ADDRESS_ID = createField(DSL.name("from_address_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.transaction_transfer_from.to_address_id</code>.
     */
    public final TableField<Record, Long> TO_ADDRESS_ID = createField(DSL.name("to_address_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.transaction_transfer_from.token_id</code>.
     */
    public final TableField<Record, BigInteger> TOKEN_ID = createField(DSL.name("token_id"), org.jooq.impl.SQLDataType.DECIMAL_INTEGER.precision(78).nullable(false), this, "");

    /**
     * Create a <code>public.transaction_transfer_from</code> table reference
     */
    public TransactionTransferFrom() {
        this(DSL.name("transaction_transfer_from"), null);
    }

    /**
     * Create an aliased <code>public.transaction_transfer_from</code> table reference
     */
    public TransactionTransferFrom(String alias) {
        this(DSL.name(alias), TRANSACTION_TRANSFER_FROM);
    }

    /**
     * Create an aliased <code>public.transaction_transfer_from</code> table reference
     */
    public TransactionTransferFrom(Name alias) {
        this(alias, TRANSACTION_TRANSFER_FROM);
    }

    private TransactionTransferFrom(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private TransactionTransferFrom(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> TransactionTransferFrom(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, TRANSACTION_TRANSFER_FROM);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Keys.TRANSACTION_TRANSFER_FROM_PKEY;
    }

    @Override
    public List<UniqueKey<Record>> getKeys() {
        return Arrays.<UniqueKey<Record>>asList(Keys.TRANSACTION_TRANSFER_FROM_PKEY);
    }

    @Override
    public List<ForeignKey<Record, ?>> getReferences() {
        return Arrays.<ForeignKey<Record, ?>>asList(Keys.TRANSACTION_TRANSFER_FROM__TRANSACTION_TRANSFER_FROM_FROM_ADDRESS_ID_FKEY, Keys.TRANSACTION_TRANSFER_FROM__TRANSACTION_TRANSFER_FROM_TO_ADDRESS_ID_FKEY);
    }

    public Address transactionTransferFromFromAddressIdFkey() {
        return new Address(this, Keys.TRANSACTION_TRANSFER_FROM__TRANSACTION_TRANSFER_FROM_FROM_ADDRESS_ID_FKEY);
    }

    public Address transactionTransferFromToAddressIdFkey() {
        return new Address(this, Keys.TRANSACTION_TRANSFER_FROM__TRANSACTION_TRANSFER_FROM_TO_ADDRESS_ID_FKEY);
    }

    @Override
    public TransactionTransferFrom as(String alias) {
        return new TransactionTransferFrom(DSL.name(alias), this);
    }

    @Override
    public TransactionTransferFrom as(Name alias) {
        return new TransactionTransferFrom(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TransactionTransferFrom rename(String name) {
        return new TransactionTransferFrom(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TransactionTransferFrom rename(Name name) {
        return new TransactionTransferFrom(name, null);
    }
}