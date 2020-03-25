package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.annotation.Combo;
import com.reedelk.runtime.api.annotation.Password;
import com.reedelk.runtime.api.resource.DynamicResource;
import com.reedelk.runtime.api.resource.ResourceBinary;
import com.reedelk.runtime.api.resource.ResourceText;
import com.reedelk.runtime.api.script.Script;
import com.reedelk.runtime.api.script.dynamicmap.DynamicBooleanMap;
import com.reedelk.runtime.api.script.dynamicmap.DynamicObjectMap;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import com.reedelk.runtime.api.script.dynamicvalue.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A platform type represent all the types of the fields which can be used within
 * a Component implementation. In a Component only the types listed here can be used
 * to configure the component.
 */
public class PlatformTypes {

    private static final Map<String, Class<?>> KNOWN_TYPES;
    static {
        Map<String, Class<?>> tmp = new HashMap<>();

        // Primitive type
        tmp.put(Boolean.class.getName(), Boolean.class);
        tmp.put(boolean.class.getName(), boolean.class);
        tmp.put(Double.class.getName(), Double.class);
        tmp.put(double.class.getName(), double.class);
        tmp.put(Float.class.getName(), Float.class);
        tmp.put(float.class.getName(), float.class);
        tmp.put(Integer.class.getName(), Integer.class);
        tmp.put(int.class.getName(), int.class);
        tmp.put(Long.class.getName(), Long.class);
        tmp.put(long.class.getName(), long.class);
        tmp.put(String.class.getName(), String.class);
        tmp.put(BigInteger.class.getName(), BigInteger.class);
        tmp.put(BigDecimal.class.getName(), BigDecimal.class);
        tmp.put(Map.class.getName(), Map.class);
        tmp.put(List.class.getName(), List.class);
        tmp.put(Enum.class.getName(), Enum.class);

        // Dynamic value type
        tmp.put(DynamicLong.class.getName(), DynamicLong.class);
        tmp.put(DynamicFloat.class.getName(), DynamicFloat.class);
        tmp.put(DynamicDouble.class.getName(), DynamicDouble.class);
        tmp.put(DynamicObject.class.getName(), DynamicObject.class);
        tmp.put(DynamicString.class.getName(), DynamicString.class);
        tmp.put(DynamicBoolean.class.getName(), DynamicBoolean.class);
        tmp.put(DynamicInteger.class.getName(), DynamicInteger.class);
        tmp.put(DynamicResource.class.getName(), DynamicResource.class);
        tmp.put(DynamicByteArray.class.getName(), DynamicByteArray.class);
        tmp.put(DynamicBigInteger.class.getName(), DynamicBigInteger.class);
        tmp.put(DynamicBigDecimal.class.getName(), DynamicBigDecimal.class);
        tmp.put(DynamicBooleanMap.class.getName(), DynamicBooleanMap.class);
        tmp.put(DynamicStringMap.class.getName(), DynamicStringMap.class);
        tmp.put(DynamicObjectMap.class.getName(), DynamicObjectMap.class);

        tmp.put(Script.class.getName(), Script.class);
        tmp.put(Combo.class.getName(), Combo.class);
        tmp.put(Password.class.getName(), Password.class);
        tmp.put(ResourceText.class.getName(), ResourceText.class);
        tmp.put(ResourceBinary.class.getName(), ResourceBinary.class);

        KNOWN_TYPES = Collections.unmodifiableMap(tmp);
    }

    public static boolean isSupported(String fullyQualifiedName) {
        return KNOWN_TYPES.containsKey(fullyQualifiedName);
    }

    public static int size() {
        return KNOWN_TYPES.size();
    }

    public static Class<?> from(String fullyQualifiedName) {
        return KNOWN_TYPES.get(fullyQualifiedName);
    }
}
