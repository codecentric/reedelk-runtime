package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.analyzer.component.UnsupportedType;
import io.github.classgraph.FieldInfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TypeDescriptorFactoryProvider {

    private static final List<TypeDescriptorFactory> factories = Collections.unmodifiableList(Arrays.asList(
            new TypeEnumFactory(),
            new TypeComboFactory(),
            new TypeDynamicFactory(),
            new TypeDynamicMapFactory(),
            new TypeMapFactory(),
            new TypeMimeTypeComboFactory(),
            new TypePasswordFactory(),
            new TypeResourceBinaryFactory(),
            new TypeResourceTextFactory(),
            new TypeScriptFactory(),
            new TypePrimitiveFactory(),
            new TypeObjectFactory()));

    public static TypeDescriptorFactory from(String fullyQualifiedName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return factories
                .stream()
                .filter(typeDescriptorFactory -> typeDescriptorFactory.test(fullyQualifiedName, fieldInfo, context))
                .findFirst()
                .orElseThrow(() -> new UnsupportedType(fullyQualifiedName));
    }
}
