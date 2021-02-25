package de.codecentric.reedelk.runtime.api.commons;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ImmutableMap {

    public static <KeyType,ValueType> Map<KeyType,ValueType> of() {
        return Collections.unmodifiableMap(new HashMap<>());
    }

    public static <KeyType,ValueType> Map<KeyType,ValueType> of(KeyType k1, ValueType v1) {
        Map<KeyType,ValueType> map = new HashMap<>();
        map.put(k1, v1);
        return Collections.unmodifiableMap(map);
    }

    public static <KeyType,ValueType> Map<KeyType,ValueType> of(KeyType k1, ValueType v1, KeyType k2, ValueType v2) {
        Map<KeyType,ValueType> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return Collections.unmodifiableMap(map);
    }

    public static <KeyType,ValueType> Map<KeyType,ValueType> of(KeyType k1, ValueType v1,
                                                                KeyType k2, ValueType v2,
                                                                KeyType k3, ValueType v3) {
        Map<KeyType,ValueType> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return Collections.unmodifiableMap(map);
    }
}

