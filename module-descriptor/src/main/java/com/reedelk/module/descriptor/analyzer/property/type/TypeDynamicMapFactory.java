package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.TabPlacement;
import com.reedelk.module.descriptor.model.TypeDescriptor;
import com.reedelk.module.descriptor.model.TypeDynamicMapDescriptor;
import com.reedelk.runtime.api.annotation.TabGroup;
import com.reedelk.runtime.api.commons.PlatformTypes;
import io.github.classgraph.FieldInfo;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;

public class TypeDynamicMapFactory implements TypeDescriptorFactory {

    @Override
    public boolean test(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        Class<?> clazz = clazzByFullyQualifiedName(fullyQualifiedClassName);
        return PlatformTypes.isSupported(fullyQualifiedClassName) &&
                isDynamicMap(clazz);
    }

    @Override
    public TypeDescriptor create(String fullyQualifiedClassName, FieldInfo fieldInfo, ComponentAnalyzerContext context) {
        Class<?> clazz = clazzByFullyQualifiedName(fullyQualifiedClassName);
        String tabGroup = annotationValueOrDefaultFrom(fieldInfo, TabGroup.class, null);
        TabPlacement tabPlacement = tabPlacementOf(fieldInfo);

        TypeDynamicMapDescriptor descriptor = new TypeDynamicMapDescriptor();
        descriptor.setTabPlacement(tabPlacement);
        descriptor.setTabGroup(tabGroup);
        descriptor.setType(clazz);
        return descriptor;
    }
}
