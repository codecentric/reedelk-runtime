package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.model.TabPlacement;
import com.reedelk.module.descriptor.model.TypeDescriptor;
import com.reedelk.module.descriptor.model.TypeDynamicMapDescriptor;
import com.reedelk.runtime.api.annotation.TabGroup;
import io.github.classgraph.FieldInfo;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;

public class TypeDynamicMapFactory implements TypeDescriptorFactory {

    @Override
    public boolean test(Class<?> clazz, FieldInfo fieldInfo) {
        return isDynamicMap(clazz);
    }

    @Override
    public TypeDescriptor create(Class<?> clazz, FieldInfo fieldInfo) {
        String tabGroup = annotationValueOrDefaultFrom(fieldInfo, TabGroup.class, null);
        TabPlacement tabPlacement = tabPlacementOf(fieldInfo);
        TypeDynamicMapDescriptor descriptor = new TypeDynamicMapDescriptor();
        descriptor.setType(clazz);
        descriptor.setTabGroup(tabGroup);
        descriptor.setTabPlacement(tabPlacement);
        return descriptor;
    }
}
