package com.reedelk.runtime.api.script.dynamicvalue;

import com.reedelk.runtime.api.commons.ModuleContext;

public class DynamicDouble extends DynamicValue<Double> {

    private DynamicDouble(Object body) {
        super(body);
    }

    private DynamicDouble(Object body, ModuleContext context) {
        super(body, context);
    }

    public static DynamicDouble from(Object body) {
        return new DynamicDouble(body);
    }

    public static DynamicDouble from(Object body, ModuleContext context) {
        return new DynamicDouble(body, context);
    }

    @Override
    public Class<Double> getEvaluatedType() {
        return Double.class;
    }
}
