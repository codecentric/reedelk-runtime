package de.codecentric.reedelk.runtime.converter.types.stringtype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FromStringConverters {

    public static final Map<Class<?>, ValueConverter<?, ?>> ALL;
    static {
        Map<Class<?>, ValueConverter<String, ?>> tmp = new HashMap<>();

        tmp.put(Boolean.class, new AsBoolean());
        tmp.put(boolean.class, new AsBoolean());
        tmp.put(Double.class, new AsDouble());
        tmp.put(double.class, new AsDouble());
        tmp.put(Float.class, new AsFloat());
        tmp.put(float.class, new AsFloat());
        tmp.put(Integer.class, new AsInteger());
        tmp.put(int.class, new AsInteger());
        tmp.put(byte[].class, new AsByteArray());
        tmp.put(long.class, new AsLong());
        tmp.put(Long.class, new AsLong());
        tmp.put(String.class, new AsString());
        tmp.put(BigInteger.class, new AsBigInteger());
        tmp.put(BigDecimal.class, new AsBigDecimal());
        tmp.put(Object.class, new AsObject());

        ALL = Collections.unmodifiableMap(tmp);
    }

    private FromStringConverters() {
    }
}
