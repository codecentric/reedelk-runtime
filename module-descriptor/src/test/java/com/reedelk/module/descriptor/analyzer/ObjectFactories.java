package com.reedelk.module.descriptor.analyzer;

import com.reedelk.module.descriptor.model.*;
import com.reedelk.runtime.api.script.dynamicmap.DynamicMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

import java.util.List;
import java.util.Map;

public class ObjectFactories {

    public static TypeObjectDescriptor createTypeObjectDescriptor(String fullyQualifiedName, List<ComponentPropertyDescriptor> propertiesDescriptor, Shared shared) {
        TypeObjectDescriptor descriptor = new TypeObjectDescriptor();
        descriptor.setTypeFullyQualifiedName(fullyQualifiedName);
        descriptor.setCollapsible(Collapsible.NO);
        descriptor.setShared(shared);
        descriptor.setObjectProperties(propertiesDescriptor);
        return descriptor;
    }

    public static TypeObjectDescriptor createTypeObjectDescriptor(String fullyQualifiedName, List<ComponentPropertyDescriptor> propertiesDescriptor) {
        return createTypeObjectDescriptor(fullyQualifiedName, propertiesDescriptor, Shared.NO);
    }

    public static TypePrimitiveDescriptor createTypePrimitiveDescriptor(Class<?> clazzType) {
        TypePrimitiveDescriptor descriptor = new TypePrimitiveDescriptor();
        descriptor.setType(clazzType);
        return descriptor;
    }

    public static <T extends DynamicValue<?>> TypeDynamicValueDescriptor createTypeDynamicValueDescriptor(Class<T> dynamicClazzType) {
        TypeDynamicValueDescriptor descriptor = new TypeDynamicValueDescriptor();
        descriptor.setType(dynamicClazzType);
        return descriptor;
    }

    public static TypeEnumDescriptor createTypeEnumDescriptor(Map<String,String> valueAndDisplayMap) {
        TypeEnumDescriptor descriptor = new TypeEnumDescriptor();
        descriptor.setNameAndDisplayNameMap(valueAndDisplayMap);
        return descriptor;
    }

    public static TypeComboDescriptor createTypeComboDescriptor(boolean editable, String[] comboValues, String prototype) {
        TypeComboDescriptor descriptor = new TypeComboDescriptor();
        descriptor.setComboValues(comboValues);
        descriptor.setEditable(editable);
        descriptor.setPrototype(prototype);
        return descriptor;
    }

    public static TypeMapDescriptor createTypeMapDescriptor(String tabGroup, TabPlacement tabPlacement) {
        TypeMapDescriptor descriptor = new TypeMapDescriptor();
        descriptor.setTabGroup(tabGroup);
        descriptor.setTabPlacement(tabPlacement);
        return descriptor;
    }

    public static <T extends DynamicMap<?>> TypeDynamicMapDescriptor createTypeDynamicMapDescriptor(Class<T> dynamicMapType, String tabGroup) {
        TypeDynamicMapDescriptor descriptor = new TypeDynamicMapDescriptor();
        descriptor.setType(dynamicMapType);
        descriptor.setTabGroup(tabGroup);
        return descriptor;
    }
}