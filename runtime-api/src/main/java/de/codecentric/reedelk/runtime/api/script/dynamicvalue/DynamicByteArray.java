package de.codecentric.reedelk.runtime.api.script.dynamicvalue;

import de.codecentric.reedelk.runtime.api.commons.ModuleContext;

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

    // note that this method can be called if and only if the
    // dynamic value is NOT a script. If it is a script, the body()
    // method should be called instead. We can check if it is a script
    // by using isScript() method from the parent class.
    @Override
    public byte[] value() {
        if (body == null) {
            return new byte[0];
        } else if (body instanceof String) {
            // A non dynamic byte array is always a string. This is why we check if it
            // is a string. In this case we do the cast and then return its bytes.
            return ((String) body).getBytes();
        } else {
            throw new IllegalStateException("A non dynamic byte array can only have string type. " +
                    "If you called this method without checking isScript() before, you should change the implementation.");
        }
    }

    @Override
    public Class<byte[]> getEvaluatedType() {
        return byte[].class;
    }
}
