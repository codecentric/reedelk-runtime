package de.codecentric.reedelk.module.descriptor.analyzer.property.type;

import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.model.property.DynamicMapDescriptor;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.KeyName;
import de.codecentric.reedelk.runtime.api.annotation.TabGroup;
import de.codecentric.reedelk.runtime.api.annotation.ValueName;
import de.codecentric.reedelk.runtime.api.commons.PlatformTypes;
import io.github.classgraph.FieldInfo;

import static de.codecentric.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;

public class DynamicMapFactory implements DescriptorFactory {

    @Override
    public boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return clazzByFullyQualifiedName(fullyQualifiedClassName)
                .map(clazz -> PlatformTypes.isSupported(fullyQualifiedClassName) && isDynamicMap(clazz))
                .orElse(false);
    }

    @Override
    public PropertyTypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        String tabGroup = annotationValueFrom(fieldInfo, TabGroup.class, null);
        String keyName = annotationValueFrom(fieldInfo, KeyName.class, null);
        String valueName = annotationValueFrom(fieldInfo, ValueName.class, null);

        Class<?> clazz = clazzByFullyQualifiedNameOrThrow(fullyQualifiedClassName);

        DynamicMapDescriptor descriptor = new DynamicMapDescriptor();
        descriptor.setValueName(valueName);
        descriptor.setTabGroup(tabGroup);
        descriptor.setKeyName(keyName);
        descriptor.setType(clazz);
        return descriptor;
    }
}
