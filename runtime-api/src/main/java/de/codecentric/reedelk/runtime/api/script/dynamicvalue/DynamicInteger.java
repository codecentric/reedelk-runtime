package de.codecentric.reedelk.runtime.api.script.dynamicvalue;

import de.codecentric.reedelk.runtime.api.commons.ModuleContext;

public class DynamicInteger extends DynamicValue<Integer> {

    private DynamicInteger(Object body) {
        super(body);
    }

    private DynamicInteger(Object body, ModuleContext context) {
        super(body, context);
    }

    public static DynamicInteger from(Object body) {
        return new DynamicInteger(body);
    }

    public static DynamicInteger from(Object body, ModuleContext context) {
        return new DynamicInteger(body, context);
    }

    @Override
    public Class<Integer> getEvaluatedType() {
        return Integer.class;
    }
}
