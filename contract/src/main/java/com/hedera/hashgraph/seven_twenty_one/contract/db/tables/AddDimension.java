/*
 * This file is generated by jOOQ.
 */
package com.hedera.hashgraph.seven_twenty_one.contract.db.tables;


import com.hedera.hashgraph.seven_twenty_one.contract.db.Public;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AddDimension extends TableImpl<Record> {

    private static final long serialVersionUID = 1593007283;

    /**
     * The reference instance of <code>public.add_dimension</code>
     */
    public static final AddDimension ADD_DIMENSION = new AddDimension();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>public.add_dimension.dimension_id</code>.
     */
    public final TableField<Record, Integer> DIMENSION_ID = createField(DSL.name("dimension_id"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.add_dimension.schema_name</code>.
     */
    public final TableField<Record, String> SCHEMA_NAME = createField(DSL.name("schema_name"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>public.add_dimension.table_name</code>.
     */
    public final TableField<Record, String> TABLE_NAME = createField(DSL.name("table_name"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>public.add_dimension.column_name</code>.
     */
    public final TableField<Record, String> COLUMN_NAME = createField(DSL.name("column_name"), org.jooq.impl.SQLDataType.VARCHAR, this, "");

    /**
     * The column <code>public.add_dimension.created</code>.
     */
    public final TableField<Record, Boolean> CREATED = createField(DSL.name("created"), org.jooq.impl.SQLDataType.BOOLEAN, this, "");

    /**
     * Create a <code>public.add_dimension</code> table reference
     */
    public AddDimension() {
        this(DSL.name("add_dimension"), null);
    }

    /**
     * Create an aliased <code>public.add_dimension</code> table reference
     */
    public AddDimension(String alias) {
        this(DSL.name(alias), ADD_DIMENSION);
    }

    /**
     * Create an aliased <code>public.add_dimension</code> table reference
     */
    public AddDimension(Name alias) {
        this(alias, ADD_DIMENSION);
    }

    private AddDimension(Name alias, Table<Record> aliased) {
        this(alias, aliased, new Field[6]);
    }

    private AddDimension(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.function());
    }

    public <O extends Record> AddDimension(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, ADD_DIMENSION);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public AddDimension as(String alias) {
        return new AddDimension(DSL.name(alias), this, parameters);
    }

    @Override
    public AddDimension as(Name alias) {
        return new AddDimension(alias, this, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public AddDimension rename(String name) {
        return new AddDimension(DSL.name(name), null, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public AddDimension rename(Name name) {
        return new AddDimension(name, null, parameters);
    }

    /**
     * Call this table-valued function
     */
    public AddDimension call(Object mainTable, String columnName, Integer numberPartitions, Object chunkTimeInterval, String partitioningFunc, Boolean ifNotExists) {
        return new AddDimension(DSL.name(getName()), null, new Field[] { 
              DSL.val(mainTable, org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"regclass\""))
            , DSL.val(columnName, org.jooq.impl.SQLDataType.VARCHAR)
            , DSL.val(numberPartitions, org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("NULL::integer", org.jooq.impl.SQLDataType.INTEGER)))
            , DSL.val(chunkTimeInterval, org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"anyelement\"").defaultValue(org.jooq.impl.DSL.field("NULL::bigint", org.jooq.impl.SQLDataType.OTHER)))
            , DSL.val(partitioningFunc, org.jooq.impl.SQLDataType.VARCHAR.defaultValue(org.jooq.impl.DSL.field("NULL::regproc", org.jooq.impl.SQLDataType.VARCHAR)))
            , DSL.val(ifNotExists, org.jooq.impl.SQLDataType.BOOLEAN.defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)))
        });
    }

    /**
     * Call this table-valued function
     */
    public AddDimension call(Field<Object> mainTable, Field<String> columnName, Field<Integer> numberPartitions, Field<Object> chunkTimeInterval, Field<String> partitioningFunc, Field<Boolean> ifNotExists) {
        return new AddDimension(DSL.name(getName()), null, new Field[] { 
              mainTable
            , columnName
            , numberPartitions
            , chunkTimeInterval
            , partitioningFunc
            , ifNotExists
        });
    }
}
