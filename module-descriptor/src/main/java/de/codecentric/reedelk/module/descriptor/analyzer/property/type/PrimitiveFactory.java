package de.codecentric.reedelk.module.descriptor.analyzer.property.type;

import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.model.property.PrimitiveDescriptor;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;
import de.codecentric.reedelk.runtime.api.commons.PlatformTypes;
import io.github.classgraph.FieldInfo;

import static de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;

public class PrimitiveFactory implements DescriptorFactory {

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
        PrimitiveDescriptor descriptor = new PrimitiveDescriptor();
        descriptor.setType(primitiveTypeClazz);
        return descriptor;
    }
}
