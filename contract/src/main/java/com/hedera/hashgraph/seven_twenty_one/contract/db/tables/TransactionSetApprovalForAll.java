/*
 * This file is generated by jOOQ.
 */
package com.hedera.hashgraph.seven_twenty_one.contract.db.tables;


import com.hedera.hashgraph.seven_twenty_one.contract.db.Keys;
import com.hedera.hashgraph.seven_twenty_one.contract.db.Public;

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
public class TransactionSetApprovalForAll extends TableImpl<Record> {

    private static final long serialVersionUID = 179692206;

    /**
     * The reference instance of <code>public.transaction_set_approval_for_all</code>
     */
    public static final TransactionSetApprovalForAll TRANSACTION_SET_APPROVAL_FOR_ALL = new TransactionSetApprovalForAll();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>public.transaction_set_approval_for_all.timestamp</code>.
     */
    public final TableField<Record, Long> TIMESTAMP = createField(DSL.name("timestamp"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.transaction_set_approval_for_all.operator_address_id</code>.
     */
    public final TableField<Record, Long> OPERATOR_ADDRESS_ID = createField(DSL.name("operator_address_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.transaction_set_approval_for_all.approved</code>.
     */
    public final TableField<Record, Boolean> APPROVED = createField(DSL.name("approved"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * Create a <code>public.transaction_set_approval_for_all</code> table reference
     */
    public TransactionSetApprovalForAll() {
        this(DSL.name("transaction_set_approval_for_all"), null);
    }

    /**
     * Create an aliased <code>public.transaction_set_approval_for_all</code> table reference
     */
    public TransactionSetApprovalForAll(String alias) {
        this(DSL.name(alias), TRANSACTION_SET_APPROVAL_FOR_ALL);
    }

    /**
     * Create an aliased <code>public.transaction_set_approval_for_all</code> table reference
     */
    public TransactionSetApprovalForAll(Name alias) {
        this(alias, TRANSACTION_SET_APPROVAL_FOR_ALL);
    }

    private TransactionSetApprovalForAll(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private TransactionSetApprovalForAll(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> TransactionSetApprovalForAll(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, TRANSACTION_SET_APPROVAL_FOR_ALL);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Keys.TRANSACTION_SET_APPROVAL_FOR_ALL_PKEY;
    }

    @Override
    public List<UniqueKey<Record>> getKeys() {
        return Arrays.<UniqueKey<Record>>asList(Keys.TRANSACTION_SET_APPROVAL_FOR_ALL_PKEY);
    }

    @Override
    public List<ForeignKey<Record, ?>> getReferences() {
        return Arrays.<ForeignKey<Record, ?>>asList(Keys.TRANSACTION_SET_APPROVAL_FOR_ALL__TRANSACTION_SET_APPROVAL_FOR_ALL_OPERATOR_ADDRESS_ID_FKEY);
    }

    public Address address() {
        return new Address(this, Keys.TRANSACTION_SET_APPROVAL_FOR_ALL__TRANSACTION_SET_APPROVAL_FOR_ALL_OPERATOR_ADDRESS_ID_FKEY);
    }

    @Override
    public TransactionSetApprovalForAll as(String alias) {
        return new TransactionSetApprovalForAll(DSL.name(alias), this);
    }

    @Override
    public TransactionSetApprovalForAll as(Name alias) {
        return new TransactionSetApprovalForAll(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TransactionSetApprovalForAll rename(String name) {
        return new TransactionSetApprovalForAll(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TransactionSetApprovalForAll rename(Name name) {
        return new TransactionSetApprovalForAll(name, null);
    }
}
