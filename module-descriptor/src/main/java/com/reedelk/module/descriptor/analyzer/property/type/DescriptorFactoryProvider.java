package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.analyzer.component.UnsupportedType;
import io.github.classgraph.FieldInfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DescriptorFactoryProvider {

    private static final List<DescriptorFactory> factories = Collections.unmodifiableList(Arrays.asList(
            new EnumFactory(),
            new ComboFactory(),
            new DynamicValueFactory(),
            new DynamicMapFactory(),
            new MapFactory(),
            new ListFactory(),
            new MimeTypeComboFactory(),
            new PasswordFactory(),
            new ResourceBinaryFactory(),
            new ResourceTextFactory(),
            new ScriptFactory(),
            new PrimitiveFactory(),
            new ObjectFactory()));

    public static DescriptorFactory from(String fullyQualifiedName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return factories
                .stream()
                .filter(typeDescriptorFactory -> typeDescriptorFactory.test(fullyQualifiedName, fieldInfo, context))
                .findFirst()
                .orElseThrow(() -> new UnsupportedType(fullyQualifiedName));
    }

    private DescriptorFactoryProvider() {
    }
}
