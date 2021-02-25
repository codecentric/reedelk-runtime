package de.codecentric.reedelk.runtime.api.script.dynamicvalue;

import de.codecentric.reedelk.runtime.api.commons.ModuleContext;

public class DynamicFloat extends DynamicValue<Float> {

    private DynamicFloat(Object body) {
        super(body);
    }

    private DynamicFloat(Object body, ModuleContext context) {
        super(body, context);
    }

    public static DynamicFloat from(Object body) {
        return new DynamicFloat(body);
    }

    public static DynamicFloat from(Object body, ModuleContext context) {
        return new DynamicFloat(body, context);
    }

    @Override
    public Class<Float> getEvaluatedType() {
        return Float.class;
    }
}
