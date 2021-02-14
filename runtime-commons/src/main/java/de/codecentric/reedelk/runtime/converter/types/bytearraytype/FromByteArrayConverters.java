package de.codecentric.reedelk.runtime.converter.types.bytearraytype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FromByteArrayConverters {

    public static final Map<Class<?>, ValueConverter<?,?>> ALL;
    static {
        Map<Class<?>, ValueConverter<?, ?>> tmp = new HashMap<>();
        tmp.put(String.class, new AsString());
        tmp.put(Object.class, new AsObject());
        ALL = Collections.unmodifiableMap(tmp);
    }

    private FromByteArrayConverters() {
    }
}
