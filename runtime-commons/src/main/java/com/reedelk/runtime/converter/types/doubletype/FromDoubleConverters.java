package com.reedelk.runtime.converter.types.doubletype;

import com.reedelk.runtime.converter.types.ValueConverter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FromDoubleConverters {

    public static final Map<Class<?>, ValueConverter<?, ?>> ALL;
    static {
        Map<Class<?>, ValueConverter<?, ?>> tmp = new HashMap<>();
        tmp.put(Boolean.class, new AsBoolean());
        tmp.put(byte[].class, new AsByteArray());
        tmp.put(Float.class, new AsFloat());
        tmp.put(Integer.class, new AsInteger());
        tmp.put(String.class, new AsString());
        tmp.put(Object.class, new AsObject());
        ALL = Collections.unmodifiableMap(tmp);
    }

    private FromDoubleConverters() {
    }
}
