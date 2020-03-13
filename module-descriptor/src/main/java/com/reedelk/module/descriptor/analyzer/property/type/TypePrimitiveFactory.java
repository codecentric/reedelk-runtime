package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.TypeDescriptor;
import com.reedelk.module.descriptor.model.TypePrimitiveDescriptor;
import com.reedelk.runtime.api.commons.PlatformTypes;
import io.github.classgraph.FieldInfo;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.clazzByFullyQualifiedName;
import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.isEnumeration;

public class TypePrimitiveFactory implements TypeDescriptorFactory {

    @Override
    public boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return PlatformTypes.isSupported(fullyQualifiedClassName) &&
                // the Enum is handled by its own factory.
                !isEnumeration(fullyQualifiedClassName, context);
    }

    @Override
    public TypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        Class<?> primitiveTypeClazz = clazzByFullyQualifiedName(fullyQualifiedClassName);
        TypePrimitiveDescriptor descriptor = new TypePrimitiveDescriptor();
        descriptor.setType(primitiveTypeClazz);
        return descriptor;
    }
}
