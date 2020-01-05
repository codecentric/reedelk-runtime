package com.reedelk.runtime.api.script.dynamicvalue;

import com.reedelk.runtime.api.commons.ModuleContext;

public class DynamicByteArray extends DynamicValue<byte[]> {

    private DynamicByteArray(Object body) {
        super(body);
    }

    private DynamicByteArray(Object body, ModuleContext context) {
        super(body, context);
    }

    public static DynamicByteArray from(Object body) {
        return new DynamicByteArray(body);
    }

    public static DynamicByteArray from(Object body, ModuleContext context) {
        return new DynamicByteArray(body, context);
    }

    @Override
    public byte[] value() {
        return body == null ?
                new byte[0] :
                ((String) body).getBytes();
    }

    @Override
    public Class<byte[]> getEvaluatedType() {
        return byte[].class;
    }
}
