package de.codecentric.reedelk.runtime.api.script.dynamicmap;

import de.codecentric.reedelk.runtime.api.commons.FunctionNameUUID;
import de.codecentric.reedelk.runtime.api.commons.ModuleContext;
import de.codecentric.reedelk.runtime.api.script.ScriptBlock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class DynamicMap<T> extends HashMap<String, Object> implements ScriptBlock {

    static final Map<String,?> EMPTY_MAP = Collections.unmodifiableMap(new HashMap<>());

    private final ModuleContext context;
    private final String functionNameUUID;

    DynamicMap(Map<String, ?> from, ModuleContext context) {
        super(from);
        if (from.isEmpty() && context != null) {
            throw new IllegalStateException("If the map is empty the context must be null");
        }
        if (!from.isEmpty()) {
            this.context = context;
            // functionNameUUID because each dynamic map is a compiled script in the script engine.
            this.functionNameUUID = FunctionNameUUID.generate(context);
        } else {
            this.context = null;
            this.functionNameUUID = null;
        }
    }

    @Override
    public String functionName() {
        return functionNameUUID;
    }

    @Override
    public ModuleContext getContext() {
        if (isEmpty()) {
            throw new IllegalStateException("Context not available if a map is empty!");
        }
        return context;
    }

    @Override
    public String body() {
        return super.toString();
    }

    public abstract Class<T> getEvaluatedType();

}
