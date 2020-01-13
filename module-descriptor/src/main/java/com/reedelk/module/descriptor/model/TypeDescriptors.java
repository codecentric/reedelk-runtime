package com.reedelk.module.descriptor.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TypeDescriptors {

    private static final Map<String, Class<? extends TypeDescriptor>> ALL_TYPE_DESCRIPTORS;
    static {
        Map<String, Class<? extends TypeDescriptor>> tmp = new HashMap<>();
        tmp.put(TypeComboDescriptor.class.getName(), TypeComboDescriptor.class);
        tmp.put(TypeDynamicMapDescriptor.class.getName(), TypeDynamicMapDescriptor.class);
        tmp.put(TypeDynamicValueDescriptor.class.getName(), TypeDynamicValueDescriptor.class);
        tmp.put(TypeEnumDescriptor.class.getName(), TypeEnumDescriptor.class);
        tmp.put(TypeMapDescriptor.class.getName(), TypeMapDescriptor.class);
        tmp.put(TypeObjectDescriptor.class.getName(), TypeObjectDescriptor.class);
        tmp.put(TypePasswordDescriptor.class.getName(), TypePasswordDescriptor.class);
        tmp.put(TypePrimitiveDescriptor.class.getName(), TypePrimitiveDescriptor.class);
        tmp.put(TypeResourceBinaryDescriptor.class.getName(), TypeResourceBinaryDescriptor.class);
        tmp.put(TypeResourceTextDescriptor.class.getName(), TypeResourceTextDescriptor.class);
        tmp.put(TypeScriptDescriptor.class.getName(), TypeScriptDescriptor.class);
        ALL_TYPE_DESCRIPTORS = Collections.unmodifiableMap(tmp);
    }

    private TypeDescriptors() {
    }

    public static Class<? extends TypeDescriptor> from(String typeDescriptorFullyQualifiedName) {
        return ALL_TYPE_DESCRIPTORS.get(typeDescriptorFullyQualifiedName);
    }
}
