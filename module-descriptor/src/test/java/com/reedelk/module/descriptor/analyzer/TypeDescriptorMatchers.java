package com.reedelk.module.descriptor.analyzer;

import com.reedelk.module.descriptor.model.property.*;
import com.reedelk.runtime.api.script.dynamicmap.DynamicMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class TypeDescriptorMatchers {

    public static Matcher<PropertyTypeDescriptor> ofPrimitiveType(TypePrimitiveDescriptor expected) {
        return given -> {
            if (given instanceof TypePrimitiveDescriptor) {
                TypePrimitiveDescriptor actual = (TypePrimitiveDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static Matcher<PropertyTypeDescriptor> ofTypeEnum(TypeEnumDescriptor expected) {
        return given -> {
            if (given instanceof TypeEnumDescriptor) {
                TypeEnumDescriptor actual = (TypeEnumDescriptor) given;
                Map<String, String> expectedValueAndDisplayMap = expected.getNameAndDisplayNameMap();
                Map<String, String> actualValueAndDisplayMap = actual.getNameAndDisplayNameMap();
                return same(expected, actual) &&
                        expectedValueAndDisplayMap.equals(actualValueAndDisplayMap);
            }
            return false;
        };
    }

    public static Matcher<PropertyTypeDescriptor> ofTypeResourceText(TypeResourceTextDescriptor expected) {
        return given -> {
            if (given instanceof TypeResourceTextDescriptor) {
                TypeResourceTextDescriptor actual = (TypeResourceTextDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static Matcher<PropertyTypeDescriptor> ofTypeResourceBinary(TypeResourceBinaryDescriptor expected) {
        return given -> {
            if (given instanceof TypeResourceBinaryDescriptor) {
                TypeResourceBinaryDescriptor actual = (TypeResourceBinaryDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static Matcher<PropertyTypeDescriptor> ofTypeCombo(TypeComboDescriptor expected) {
        return given -> {
            if (given instanceof TypeComboDescriptor) {
                TypeComboDescriptor actual = (TypeComboDescriptor) given;
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

    public static Matcher<PropertyTypeDescriptor> ofTypeMap(TypeMapDescriptor expected) {
        return given -> {
            if (given instanceof TypeMapDescriptor) {
                TypeMapDescriptor actual = (TypeMapDescriptor) given;
                String expectedTabGroup = expected.getTabGroup();
                String actualTabGroup = actual.getTabGroup();
                return same(expected, actual) &&
                        Objects.equals(expectedTabGroup,actualTabGroup);
            }
            return false;
        };
    }

    public static Matcher<PropertyTypeDescriptor> ofTypeList(TypeListDescriptor expected) {
        return given -> {
            if (given instanceof TypeListDescriptor) {
                TypeListDescriptor actual = (TypeListDescriptor) given;
                PropertyTypeDescriptor expectedValueType = expected.getValueType();
                PropertyTypeDescriptor actualValueType = actual.getValueType();
                return Objects.equals(expectedValueType.getType(), actualValueType.getType());
            }
            return false;
        };
    }

    public static Matcher<PropertyTypeDescriptor> ofTypeScript(TypeScriptDescriptor expected) {
        return given -> {
            if (given instanceof TypeScriptDescriptor) {
                TypeScriptDescriptor actual = (TypeScriptDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static <T extends DynamicValue<?>> Matcher<PropertyTypeDescriptor> ofDynamicType(TypeDynamicValueDescriptor expected) {
        return given -> {
            if (given instanceof TypeDynamicValueDescriptor) {
                TypeDynamicValueDescriptor actual = (TypeDynamicValueDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static <T extends DynamicMap<?>> Matcher<PropertyTypeDescriptor> ofDynamicMapType(TypeDynamicMapDescriptor expected) {
        return given -> {
            if (given instanceof TypeDynamicMapDescriptor) {
                TypeDynamicMapDescriptor actual = (TypeDynamicMapDescriptor) given;
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

