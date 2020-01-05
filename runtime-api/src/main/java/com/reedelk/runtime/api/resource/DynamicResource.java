package com.reedelk.runtime.api.resource;

import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.script.ScriptBlock;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;
import org.reactivestreams.Publisher;

public class DynamicResource extends DynamicValue<String> implements ScriptBlock {

    private DynamicResource(Object body, ModuleContext context) {
        super(body, context);
    }

    protected DynamicResource(DynamicResource original) {
        super(original);
    }

    public static DynamicResource from(Object body, ModuleContext context) {
        return new DynamicResource(body, context);
    }

    @Override
    public Class<String> getEvaluatedType() {
        return String.class;
    }

    public Publisher<byte[]> data(String evaluatedPath, int readBufferSize) {
        throw new UnsupportedOperationException("implemented in the proxy");
    }
}
