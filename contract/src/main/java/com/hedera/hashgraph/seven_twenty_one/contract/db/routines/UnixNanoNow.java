/*
 * This file is generated by jOOQ.
 */
package com.hedera.hashgraph.seven_twenty_one.contract.db.routines;


import com.hedera.hashgraph.seven_twenty_one.contract.db.Public;

import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.jooq.impl.Internal;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UnixNanoNow extends AbstractRoutine<Long> {

    private static final long serialVersionUID = 2014875687;

    /**
     * The parameter <code>public.unix_nano_now.RETURN_VALUE</code>.
     */
    public static final Parameter<Long> RETURN_VALUE = Internal.createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.BIGINT, false, false);

    /**
     * Create a new routine call instance
     */
    public UnixNanoNow() {
        super("unix_nano_now", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT);

        setReturnParameter(RETURN_VALUE);
    }
}