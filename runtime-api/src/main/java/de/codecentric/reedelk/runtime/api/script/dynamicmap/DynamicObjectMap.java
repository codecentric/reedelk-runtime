package de.codecentric.reedelk.runtime.api.script.dynamicmap;

import de.codecentric.reedelk.runtime.api.commons.ModuleContext;

import java.util.Map;

public class DynamicObjectMap extends DynamicMap<Object> {

    DynamicObjectMap(Map<String, ?> from, ModuleContext context) {
        super(from, context);
    }

    public static DynamicObjectMap empty() {
        return new DynamicObjectMap(EMPTY_MAP, null);
    }

    public static DynamicObjectMap from(Map<String, ?> from, ModuleContext context) {
        return new DynamicObjectMap(from, context);
    }

    @Override
    public Class<Object> getEvaluatedType() {
        return Object.class;
    }
}
