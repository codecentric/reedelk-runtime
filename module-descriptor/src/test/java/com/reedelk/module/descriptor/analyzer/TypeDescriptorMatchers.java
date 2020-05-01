package com.reedelk.module.descriptor.analyzer;

import com.reedelk.module.descriptor.model.property.*;
import com.reedelk.runtime.api.script.dynamicmap.DynamicMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class TypeDescriptorMatchers {

    public static Matcher<PropertyTypeDescriptor> ofPrimitiveType(PrimitiveDescriptor expected) {
        return given -> {
            if (given instanceof PrimitiveDescriptor) {
                PrimitiveDescriptor actual = (PrimitiveDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static Matcher<PropertyTypeDescriptor> ofTypeEnum(EnumDescriptor expected) {
        return given -> {
            if (given instanceof EnumDescriptor) {
                EnumDescriptor actual = (EnumDescriptor) given;
                Map<String, String> expectedValueAndDisplayMap = expected.getNameAndDisplayNameMap();
                Map<String, String> actualValueAndDisplayMap = actual.getNameAndDisplayNameMap();
                return same(expected, actual) &&
                        expectedValueAndDisplayMap.equals(actualValueAndDisplayMap);
            }
            return false;
        };
    }

    public static Matcher<PropertyTypeDescriptor> ofTypeResourceText(ResourceTextDescriptor expected) {
        return given -> {
            if (given instanceof ResourceTextDescriptor) {
                ResourceTextDescriptor actual = (ResourceTextDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static Matcher<PropertyTypeDescriptor> ofTypeResourceBinary(ResourceBinaryDescriptor expected) {
        return given -> {
            if (given instanceof ResourceBinaryDescriptor) {
                ResourceBinaryDescriptor actual = (ResourceBinaryDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static Matcher<PropertyTypeDescriptor> ofTypeCombo(ComboDescriptor expected) {
        return given -> {
            if (given instanceof ComboDescriptor) {
                ComboDescriptor actual = (ComboDescriptor) given;
                boolean expectedEditable = expected.isEditable();
                boolean actualEditable = actual.isEditable();
                String[] expectedComboValues = expected.getComboValues();
                String[] actualComboValues = actual.getComboValues();
                String expectedPrototype = expected.getPrototype();
                String actualPrototype = actual.getPrototype();
                return same(expected, actual) &&
                        Objects.equals(expectedPrototype, actualPrototype) &&
                        expectedEditable == actualEditable &&
                        Arrays.equals(expectedComboValues, actualComboValues);
            }
            return false;
        };
    }

    public static Matcher<PropertyTypeDescriptor> ofTypeMap(MapDescriptor expected) {
        return given -> {
            if (given instanceof MapDescriptor) {
                MapDescriptor actual = (MapDescriptor) given;
                String expectedTabGroup = expected.getTabGroup();
                String actualTabGroup = actual.getTabGroup();
                return same(expected, actual) &&
                        Objects.equals(expectedTabGroup,actualTabGroup);
            }
            return false;
        };
    }

    public static Matcher<PropertyTypeDescriptor> ofTypeList(ListDescriptor expected) {
        return given -> {
            if (given instanceof ListDescriptor) {
                ListDescriptor actual = (ListDescriptor) given;
                PropertyTypeDescriptor expectedValueType = expected.getValueType();
                PropertyTypeDescriptor actualValueType = actual.getValueType();
                return Objects.equals(expectedValueType.getType(), actualValueType.getType());
            }
            return false;
        };
    }

    public static Matcher<PropertyTypeDescriptor> ofTypeScript(ScriptDescriptor expected) {
        return given -> {
            if (given instanceof ScriptDescriptor) {
                ScriptDescriptor actual = (ScriptDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static <T extends DynamicValue<?>> Matcher<PropertyTypeDescriptor> ofDynamicType(DynamicValueDescriptor expected) {
        return given -> {
            if (given instanceof DynamicValueDescriptor) {
                DynamicValueDescriptor actual = (DynamicValueDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static <T extends DynamicMap<?>> Matcher<PropertyTypeDescriptor> ofDynamicMapType(DynamicMapDescriptor expected) {
        return given -> {
            if (given instanceof DynamicMapDescriptor) {
                DynamicMapDescriptor actual = (DynamicMapDescriptor) given;
                String expectedTabGroup = expected.getTabGroup();
                String actualTabGroup = actual.getTabGroup();
                return same(expected, actual) && expectedTabGroup.equals(actualTabGroup);
            }
            return false;
        };
    }

    private static boolean same(PropertyTypeDescriptor expected, PropertyTypeDescriptor actual) {
        Class<?> expectedClazzType = expected.getType();
        Class<?> actualClazzType = actual.getType();
        return expectedClazzType.equals(actualClazzType);
    }
}

