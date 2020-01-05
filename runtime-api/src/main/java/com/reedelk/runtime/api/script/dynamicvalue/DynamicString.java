package com.reedelk.runtime.api.script.dynamicvalue;

import com.reedelk.runtime.api.commons.ModuleContext;

public class DynamicString extends DynamicValue<String> {

    private DynamicString(Object body) {
        super(body);
    }

    protected DynamicString(Object body, ModuleContext context) {
        super(body, context);
    }

    public static DynamicString from(Object body) {
        return new DynamicString(body);
    }

    public static DynamicString from(Object body, ModuleContext context) {
        return new DynamicString(body, context);
    }

    @Override
    public Class<String> getEvaluatedType() {
        return String.class;
    }
}
