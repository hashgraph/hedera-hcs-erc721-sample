/*
 * This file is generated by jOOQ.
 */
package com.hedera.hashgraph.seven_twenty_one.contract.db.routines;


import com.hedera.hashgraph.seven_twenty_one.contract.db.Public;

import org.jooq.Field;
import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.jooq.impl.Internal;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GetTelemetryReport extends AbstractRoutine<String> {

    private static final long serialVersionUID = -1282011960;

    /**
     * The parameter <code>public.get_telemetry_report.RETURN_VALUE</code>.
     */
    public static final Parameter<String> RETURN_VALUE = Internal.createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.CLOB, false, false);

    /**
     * The parameter <code>public.get_telemetry_report.always_display_report</code>.
     */
    public static final Parameter<Boolean> ALWAYS_DISPLAY_REPORT = Internal.createParameter("always_display_report", org.jooq.impl.SQLDataType.BOOLEAN.defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), true, false);

    /**
     * Create a new routine call instance
     */
    public GetTelemetryReport() {
        super("get_telemetry_report", Public.PUBLIC, org.jooq.impl.SQLDataType.CLOB);

        setReturnParameter(RETURN_VALUE);
        addInParameter(ALWAYS_DISPLAY_REPORT);
    }

    /**
     * Set the <code>always_display_report</code> parameter IN value to the routine
     */
    public void setAlwaysDisplayReport(Boolean value) {
        setValue(ALWAYS_DISPLAY_REPORT, value);
    }

    /**
     * Set the <code>always_display_report</code> parameter to the function to be used with a {@link org.jooq.Select} statement
     */
    public void setAlwaysDisplayReport(Field<Boolean> field) {
        setField(ALWAYS_DISPLAY_REPORT, field);
    }
}
