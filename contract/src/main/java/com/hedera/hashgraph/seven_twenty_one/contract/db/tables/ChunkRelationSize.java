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
public class ChunkRelationSize extends TableImpl<Record> {

    private static final long serialVersionUID = -1013992512;

    /**
     * The reference instance of <code>public.chunk_relation_size</code>
     */
    public static final ChunkRelationSize CHUNK_RELATION_SIZE = new ChunkRelationSize();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>public.chunk_relation_size.chunk_id</code>.
     */
    public final TableField<Record, Integer> CHUNK_ID = createField(DSL.name("chunk_id"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.chunk_relation_size.chunk_table</code>.
     */
    public final TableField<Record, String> CHUNK_TABLE = createField(DSL.name("chunk_table"), org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.chunk_relation_size.partitioning_columns</code>.
     */
    public final TableField<Record, String[]> PARTITIONING_COLUMNS = createField(DSL.name("partitioning_columns"), org.jooq.impl.SQLDataType.VARCHAR.getArrayDataType(), this, "");

    /**
     * The column <code>public.chunk_relation_size.partitioning_column_types</code>.
     */
    public final TableField<Record, Object[]> PARTITIONING_COLUMN_TYPES = createField(DSL.name("partitioning_column_types"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"regtype\"").getArrayDataType(), this, "");

    /**
     * The column <code>public.chunk_relation_size.partitioning_hash_functions</code>.
     */
    public final TableField<Record, String[]> PARTITIONING_HASH_FUNCTIONS = createField(DSL.name("partitioning_hash_functions"), org.jooq.impl.SQLDataType.CLOB.getArrayDataType(), this, "");

    /**
     * The column <code>public.chunk_relation_size.ranges</code>.
     */
    public final TableField<Record, Object[]> RANGES = createField(DSL.name("ranges"), org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"int8range\"").getArrayDataType(), this, "");

    /**
     * The column <code>public.chunk_relation_size.table_bytes</code>.
     */
    public final TableField<Record, Long> TABLE_BYTES = createField(DSL.name("table_bytes"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.chunk_relation_size.index_bytes</code>.
     */
    public final TableField<Record, Long> INDEX_BYTES = createField(DSL.name("index_bytes"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.chunk_relation_size.toast_bytes</code>.
     */
    public final TableField<Record, Long> TOAST_BYTES = createField(DSL.name("toast_bytes"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.chunk_relation_size.total_bytes</code>.
     */
    public final TableField<Record, Long> TOTAL_BYTES = createField(DSL.name("total_bytes"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * Create a <code>public.chunk_relation_size</code> table reference
     */
    public ChunkRelationSize() {
        this(DSL.name("chunk_relation_size"), null);
    }

    /**
     * Create an aliased <code>public.chunk_relation_size</code> table reference
     */
    public ChunkRelationSize(String alias) {
        this(DSL.name(alias), CHUNK_RELATION_SIZE);
    }

    /**
     * Create an aliased <code>public.chunk_relation_size</code> table reference
     */
    public ChunkRelationSize(Name alias) {
        this(alias, CHUNK_RELATION_SIZE);
    }

    private ChunkRelationSize(Name alias, Table<Record> aliased) {
        this(alias, aliased, new Field[1]);
    }

    private ChunkRelationSize(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.function());
    }

    public <O extends Record> ChunkRelationSize(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, CHUNK_RELATION_SIZE);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public ChunkRelationSize as(String alias) {
        return new ChunkRelationSize(DSL.name(alias), this, parameters);
    }

    @Override
    public ChunkRelationSize as(Name alias) {
        return new ChunkRelationSize(alias, this, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public ChunkRelationSize rename(String name) {
        return new ChunkRelationSize(DSL.name(name), null, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public ChunkRelationSize rename(Name name) {
        return new ChunkRelationSize(name, null, parameters);
    }

    /**
     * Call this table-valued function
     */
    public ChunkRelationSize call(Object mainTable) {
        return new ChunkRelationSize(DSL.name(getName()), null, new Field[] { 
              DSL.val(mainTable, org.jooq.impl.DefaultDataType.getDefaultDataType("\"pg_catalog\".\"regclass\""))
        });
    }

    /**
     * Call this table-valued function
     */
    public ChunkRelationSize call(Field<Object> mainTable) {
        return new ChunkRelationSize(DSL.name(getName()), null, new Field[] { 
              mainTable
        });
    }
}
