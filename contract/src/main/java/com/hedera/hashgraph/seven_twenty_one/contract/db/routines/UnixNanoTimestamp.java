/*
 * This file is generated by jOOQ.
 */
package com.hedera.hashgraph.seven_twenty_one.contract.db.routines;


import com.hedera.hashgraph.seven_twenty_one.contract.db.Public;

import java.time.OffsetDateTime;

import org.jooq.Field;
import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.jooq.impl.Internal;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UnixNanoTimestamp extends AbstractRoutine<OffsetDateTime> {

    private static final long serialVersionUID = -889154986;

    /**
     * The parameter <code>public.unix_nano_timestamp.RETURN_VALUE</code>.
     */
    public static final Parameter<OffsetDateTime> RETURN_VALUE = Internal.createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.TIMESTAMPWITHTIMEZONE, false, false);

    /**
     * The parameter <code>public.unix_nano_timestamp.un</code>.
     */
    public static final Parameter<Long> UN = Internal.createParameter("un", org.jooq.impl.SQLDataType.BIGINT, false, false);

    /**
     * Create a new routine call instance
     */
    public UnixNanoTimestamp() {
        super("unix_nano_timestamp", Public.PUBLIC, org.jooq.impl.SQLDataType.TIMESTAMPWITHTIMEZONE);

        setReturnParameter(RETURN_VALUE);
        addInParameter(UN);
    }

    /**
     * Set the <code>un</code> parameter IN value to the routine
     */
    public void setUn(Long value) {
        setValue(UN, value);
    }

    /**
     * Set the <code>un</code> parameter to the function to be used with a {@link org.jooq.Select} statement
     */
    public void setUn(Field<Long> field) {
        setField(UN, field);
    }
}
