package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;
import com.reedelk.module.descriptor.model.property.TypePrimitiveDescriptor;
import com.reedelk.runtime.api.commons.PlatformTypes;
import io.github.classgraph.FieldInfo;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;

public class TypePrimitiveFactory implements TypeDescriptorFactory {

    @Override
    public boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return clazzByFullyQualifiedName(fullyQualifiedClassName).isPresent() &&
                PlatformTypes.isSupported(fullyQualifiedClassName) &&
                // the Enum is handled by its own factory.
                !isEnumeration(fullyQualifiedClassName, context);
    }

    @Override
    public PropertyTypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        Class<?> primitiveTypeClazz = clazzByFullyQualifiedNameOrThrow(fullyQualifiedClassName);
        TypePrimitiveDescriptor descriptor = new TypePrimitiveDescriptor();
        descriptor.setType(primitiveTypeClazz);
        return descriptor;
    }
}
