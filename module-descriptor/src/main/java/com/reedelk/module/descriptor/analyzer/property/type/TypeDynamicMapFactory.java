package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.TypeDescriptor;
import com.reedelk.module.descriptor.model.TypeDynamicMapDescriptor;
import com.reedelk.runtime.api.annotation.KeyName;
import com.reedelk.runtime.api.annotation.TabGroup;
import com.reedelk.runtime.api.annotation.ValueName;
import com.reedelk.runtime.api.commons.PlatformTypes;
import io.github.classgraph.FieldInfo;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;

public class TypeDynamicMapFactory implements TypeDescriptorFactory {

    @Override
    public boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        return clazzByFullyQualifiedName(fullyQualifiedClassName)
                .map(clazz -> PlatformTypes.isSupported(fullyQualifiedClassName) && isDynamicMap(clazz))
                .orElse(false);
    }

    @Override
    public TypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        String tabGroup = annotationValueOrDefaultFrom(fieldInfo, TabGroup.class, null);
        String keyName = annotationValueOrDefaultFrom(fieldInfo, KeyName.class, null);
        String valueName = annotationValueOrDefaultFrom(fieldInfo, ValueName.class, null);

        Class<?> clazz = clazzByFullyQualifiedNameOrThrow(fullyQualifiedClassName);

        TypeDynamicMapDescriptor descriptor = new TypeDynamicMapDescriptor();
        descriptor.setValueName(valueName);
        descriptor.setTabGroup(tabGroup);
        descriptor.setKeyName(keyName);
        descriptor.setType(clazz);
        return descriptor;
    }
}
