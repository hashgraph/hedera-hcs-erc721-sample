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
public class TransactionBurn extends TableImpl<Record> {

    private static final long serialVersionUID = 720633251;

    /**
     * The reference instance of <code>public.transaction_burn</code>
     */
    public static final TransactionBurn TRANSACTION_BURN = new TransactionBurn();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>public.transaction_burn.timestamp</code>.
     */
    public final TableField<Record, Long> TIMESTAMP = createField(DSL.name("timestamp"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.transaction_burn.token_id</code>.
     */
    public final TableField<Record, BigInteger> TOKEN_ID = createField(DSL.name("token_id"), org.jooq.impl.SQLDataType.DECIMAL_INTEGER.precision(78).nullable(false), this, "");

    /**
     * Create a <code>public.transaction_burn</code> table reference
     */
    public TransactionBurn() {
        this(DSL.name("transaction_burn"), null);
    }

    /**
     * Create an aliased <code>public.transaction_burn</code> table reference
     */
    public TransactionBurn(String alias) {
        this(DSL.name(alias), TRANSACTION_BURN);
    }

    /**
     * Create an aliased <code>public.transaction_burn</code> table reference
     */
    public TransactionBurn(Name alias) {
        this(alias, TRANSACTION_BURN);
    }

    private TransactionBurn(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private TransactionBurn(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> TransactionBurn(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, TRANSACTION_BURN);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Keys.TRANSACTION_BURN_PKEY;
    }

    @Override
    public List<UniqueKey<Record>> getKeys() {
        return Arrays.<UniqueKey<Record>>asList(Keys.TRANSACTION_BURN_PKEY);
    }

    @Override
    public TransactionBurn as(String alias) {
        return new TransactionBurn(DSL.name(alias), this);
    }

    @Override
    public TransactionBurn as(Name alias) {
        return new TransactionBurn(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TransactionBurn rename(String name) {
        return new TransactionBurn(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TransactionBurn rename(Name name) {
        return new TransactionBurn(name, null);
    }
}
