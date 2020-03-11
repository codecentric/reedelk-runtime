package com.reedelk.module.descriptor.analyzer.property.type;

import com.reedelk.module.descriptor.model.TabPlacement;
import com.reedelk.module.descriptor.model.TypeDescriptor;
import com.reedelk.module.descriptor.model.TypeMapDescriptor;
import com.reedelk.runtime.api.annotation.TabGroup;
import io.github.classgraph.ClassRefTypeSignature;
import io.github.classgraph.FieldInfo;
import io.github.classgraph.TypeArgument;

import java.util.List;

import static com.reedelk.module.descriptor.analyzer.commons.ScannerUtils.*;

public class TypeMapFactory implements TypeDescriptorFactory {

    @Override
    public boolean test(Class<?> clazz, FieldInfo fieldInfo) {
        return isMap(clazz);
    }

    @Override
    public TypeDescriptor create(Class<?> clazz, FieldInfo fieldInfo) {
        ClassRefTypeSignature classRefTypeSignature = (ClassRefTypeSignature) fieldInfo.getTypeSignature();
        List<TypeArgument> typeArguments = classRefTypeSignature.getTypeArguments();
        // The second type is the map value types.
        TypeArgument typeArgument = typeArguments.get(1);
        String valueTypeFullyQualifiedName = typeArgument.toString();
        String tabGroup = annotationValueOrDefaultFrom(fieldInfo, TabGroup.class, null);
        TabPlacement tabPlacement = tabPlacementOf(fieldInfo);

        TypeMapDescriptor descriptor = new TypeMapDescriptor();
        descriptor.setTabGroup(tabGroup);
        descriptor.setTabPlacement(tabPlacement);
        descriptor.setValueFullyQualifiedName(valueTypeFullyQualifiedName);
        return descriptor;
    }
}
