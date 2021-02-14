package de.codecentric.reedelk.runtime.api.script.dynamicmap;

import de.codecentric.reedelk.runtime.api.commons.ModuleContext;

import java.util.Map;

public class DynamicStringMap extends DynamicMap<String> {

    private DynamicStringMap(Map<String, ?> from, ModuleContext context) {
        super(from, context);
    }

    public static DynamicStringMap empty() {
        return new DynamicStringMap(EMPTY_MAP, null);
    }

    public static DynamicStringMap from(Map<String, ?> from, ModuleContext context) {
        return new DynamicStringMap(from, context);
    }

    @Override
    public Class<String> getEvaluatedType() {
        return String.class;
    }
}
