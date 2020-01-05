package com.reedelk.runtime.api.script.dynamicvalue;

import com.reedelk.runtime.api.commons.ModuleContext;

import java.math.BigDecimal;

public class DynamicBigDecimal extends DynamicValue<BigDecimal> {

    private DynamicBigDecimal(Object body) {
        super(body);
    }

    private DynamicBigDecimal(Object body, ModuleContext context) {
        super(body, context);
    }

    public static DynamicBigDecimal from(Object body) {
        return new DynamicBigDecimal(body);
    }

    public static DynamicBigDecimal from(Object body, ModuleContext context) {
        return new DynamicBigDecimal(body, context);
    }

    @Override
    public Class<BigDecimal> getEvaluatedType() {
        return BigDecimal.class;
    }
}
