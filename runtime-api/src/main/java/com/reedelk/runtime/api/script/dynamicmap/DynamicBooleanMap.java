package com.reedelk.runtime.api.script.dynamicmap;

import com.reedelk.runtime.api.commons.ModuleContext;

import java.util.Map;

public class DynamicBooleanMap extends DynamicMap<Boolean> {

    DynamicBooleanMap(Map<String, ?> from, ModuleContext context) {
        super(from, context);
    }

    public static DynamicBooleanMap empty() {
        return new DynamicBooleanMap(EMPTY_MAP, null);
    }

    public static DynamicBooleanMap from(Map<String, ?> from, ModuleContext context) {
        return new DynamicBooleanMap(from, context);
    }

    @Override
    public Class<Boolean> getEvaluatedType() {
        return Boolean.class;
    }
}
