package de.codecentric.reedelk.module.descriptor.analyzer;

import de.codecentric.reedelk.module.descriptor.model.property.*;
import de.codecentric.reedelk.runtime.api.script.dynamicmap.DynamicMap;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

import java.util.Map;

public class ObjectFactories {


    public static PrimitiveDescriptor createTypePrimitiveDescriptor(Class<?> clazzType) {
        PrimitiveDescriptor descriptor = new PrimitiveDescriptor();
        descriptor.setType(clazzType);
        return descriptor;
    }

    public static <T extends DynamicValue<?>> DynamicValueDescriptor createTypeDynamicValueDescriptor(Class<T> dynamicClazzType) {
        DynamicValueDescriptor descriptor = new DynamicValueDescriptor();
        descriptor.setType(dynamicClazzType);
        return descriptor;
    }

    public static EnumDescriptor createTypeEnumDescriptor(Map<String,String> valueAndDisplayMap) {
        EnumDescriptor descriptor = new EnumDescriptor();
        descriptor.setNameAndDisplayNameMap(valueAndDisplayMap);
        return descriptor;
    }

    public static ComboDescriptor createTypeComboDescriptor(boolean editable, String[] comboValues, String prototype) {
        ComboDescriptor descriptor = new ComboDescriptor();
        descriptor.setComboValues(comboValues);
        descriptor.setEditable(editable);
        descriptor.setPrototype(prototype);
        return descriptor;
    }

    public static MapDescriptor createTypeMapDescriptor(String tabGroup) {
        MapDescriptor descriptor = new MapDescriptor();
        descriptor.setTabGroup(tabGroup);
        return descriptor;
    }

    public static ListDescriptor createTypeListDescriptor(Class<?> valueType) {
        ListDescriptor descriptor = new ListDescriptor();
        descriptor.setValueType(createTypePrimitiveDescriptor(valueType));
        return descriptor;
    }

    public static <T extends DynamicMap<?>> DynamicMapDescriptor createTypeDynamicMapDescriptor(Class<T> dynamicMapType, String tabGroup) {
        DynamicMapDescriptor descriptor = new DynamicMapDescriptor();
        descriptor.setType(dynamicMapType);
        descriptor.setTabGroup(tabGroup);
        return descriptor;
    }
}
