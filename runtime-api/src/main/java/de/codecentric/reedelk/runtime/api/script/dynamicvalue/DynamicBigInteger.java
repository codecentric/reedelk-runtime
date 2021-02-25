package de.codecentric.reedelk.runtime.api.script.dynamicvalue;

import de.codecentric.reedelk.runtime.api.commons.ModuleContext;

import java.math.BigInteger;

public class DynamicBigInteger extends DynamicValue<BigInteger> {

    private DynamicBigInteger(Object body) {
        super(body);
    }

    private DynamicBigInteger(Object body, ModuleContext context) {
        super(body, context);
    }

    public static DynamicBigInteger from(Object body) {
        return new DynamicBigInteger(body);
    }

    public static DynamicBigInteger from(Object body, ModuleContext context) {
        return new DynamicBigInteger(body, context);
    }

    @Override
    public Class<BigInteger> getEvaluatedType() {
        return BigInteger.class;
    }
}
