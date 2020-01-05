package com.reedelk.runtime.api.script.dynamicvalue;

import com.reedelk.runtime.api.commons.FunctionNameUUID;
import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.commons.ScriptUtils;
import com.reedelk.runtime.api.script.ScriptBlock;

/**
 * A dynamic value is a component property which
 * might be a simple text or an inline script.
 */
public abstract class DynamicValue<T> implements ScriptBlock {

    private final boolean isScript;
    private final ModuleContext context;
    private final String functionNameUUID;
    protected final Object body;

    /**
     * Constructor for a dynamic value whose body is NOT a script.
     * @param body the dynamic value body. Must not be a script.
     */
    protected DynamicValue(Object body) {
        if (ScriptUtils.isScript(body)) {
            throw new IllegalStateException(String.format("Script body [%s] was not expected.", body));
        }
        this.body = body;
        this.context = null;
        this.isScript = false;
        this.functionNameUUID = null;
    }

    protected DynamicValue(Object body, ModuleContext context) {
        this.body = body;
        this.context = context;
        this.isScript = ScriptUtils.isScript(body);
        this.functionNameUUID = FunctionNameUUID.generate(context);
    }

    protected DynamicValue(DynamicValue original) {
        this.body = original.body;
        this.context = original.context;
        this.isScript = original.isScript;
        this.functionNameUUID = original.functionNameUUID;
    }

    @Override
    public String functionName() {
        return functionNameUUID;
    }

    @Override
    public String body() {
        if (this.isScript) {
            return (String) body;
        } else {
            throw new IllegalStateException("The Dynamic value is not a script");
        }
    }

    @Override
    public boolean isEmpty() {
        if (this.isScript) {
            if (body == null) return true;
            return ScriptUtils.isEmpty((String) body);
        } else {
            throw new IllegalStateException("The Dynamic value is not a script");
        }
    }

    @Override
    public ModuleContext getContext() {
        if (!this.isScript) {
            throw new IllegalStateException("ScriptContext is only availabl for scripts values");
        }
        return context;
    }

    @Override
    public String toString() {
        return "DynamicValue{" +
                "functionNameUUID='" + functionNameUUID + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    @SuppressWarnings("unchecked")
    public T value() {
        return (T) body;
    }

    public boolean isScript() {
        return isScript;
    }
    
    public boolean isNotNull() {
        return body instanceof String || body != null;
    }

    public abstract Class<T> getEvaluatedType();

}
