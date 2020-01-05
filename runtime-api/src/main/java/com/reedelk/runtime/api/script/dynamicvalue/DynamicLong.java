package com.reedelk.runtime.api.script.dynamicvalue;

import com.reedelk.runtime.api.commons.ModuleContext;

public class DynamicLong extends DynamicValue<Long> {

    private DynamicLong(Object body) {
        super(body);
    }

    private DynamicLong(Object body, ModuleContext context) {
        super(body, context);
    }

    public static DynamicLong from(Object body) {
        return new DynamicLong(body);
    }

    public static DynamicLong from(Object body, ModuleContext context) {
        return new DynamicLong(body, context);
    }

    @Override
    public Class<Long> getEvaluatedType() {
        return Long.class;
    }
}
