package com.reedelk.runtime.api.script.dynamicvalue;

import com.reedelk.runtime.api.commons.ModuleContext;

public class DynamicBoolean extends DynamicValue<Boolean> {

    private DynamicBoolean(Object body) {
        super(body);
    }

    private DynamicBoolean(Object body, ModuleContext context) {
        super(body, context);
    }

    public static DynamicBoolean from(Object body) {
        return new DynamicBoolean(body);
    }

    public static DynamicBoolean from(Object body, ModuleContext context) {
        return new DynamicBoolean(body, context);
    }

    @Override
    public Class<Boolean> getEvaluatedType() {
        return Boolean.class;
    }
}
