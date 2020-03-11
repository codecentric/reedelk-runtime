package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.model.TypeDescriptor;
import io.github.classgraph.FieldInfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KnownTypeDescriptor {

    private static final TypeDescriptorFactory defaultFactory = new TypePrimitiveFactory();
    private static final List<TypeDescriptorFactory> factories = Collections.unmodifiableList(Arrays.asList(
            new TypeComboFactory(),
            new TypeDynamicFactory(),
            new TypeDynamicMapFactory(),
            new TypeMapFactory(),
            new TypeMimeTypeComboFactory(),
            new TypePasswordFactory(),
            new TypeResourceBinaryFactory(),
            new TypeResourceTextFactory(),
            new TypeScriptFactory()));

    public static TypeDescriptor from(Class<?> clazz, FieldInfo fieldInfo) {
        return factories
                .stream()
                .filter(typeDescriptorFactory -> typeDescriptorFactory.test(clazz, fieldInfo))
                .findFirst()
                .orElse(defaultFactory)
                .create(clazz, fieldInfo);
    }
}
