package com.reedelk.runtime.api.script.dynamicvalue;

import com.reedelk.runtime.api.commons.ModuleContext;

public class DynamicObject extends DynamicValue<Object> {

    private DynamicObject(Object body) {
        super(body);
    }

    private DynamicObject(Object body, ModuleContext context) {
        super(body, context);
    }

    public static DynamicObject from(Object body) {
        return new DynamicObject(body);
    }

    public static DynamicObject from(Object body, ModuleContext context) {
        return new DynamicObject(body, context);
    }

    @Override
    public Class<Object> getEvaluatedType() {
        return Object.class;
    }
}
