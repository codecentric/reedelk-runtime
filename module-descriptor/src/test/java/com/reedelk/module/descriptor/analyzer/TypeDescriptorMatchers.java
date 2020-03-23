package com.reedelk.module.descriptor.analyzer;

import com.reedelk.module.descriptor.model.*;
import com.reedelk.runtime.api.script.dynamicmap.DynamicMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class TypeDescriptorMatchers {

    public static Matcher<TypeDescriptor> ofPrimitiveType(TypePrimitiveDescriptor expected) {
        return given -> {
            if (given instanceof TypePrimitiveDescriptor) {
                TypePrimitiveDescriptor actual = (TypePrimitiveDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static Matcher<TypeDescriptor> ofTypeEnum(TypeEnumDescriptor expected) {
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

    public static Matcher<TypeDescriptor> ofTypeResourceText(TypeResourceTextDescriptor expected) {
        return given -> {
            if (given instanceof TypeResourceTextDescriptor) {
                TypeResourceTextDescriptor actual = (TypeResourceTextDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static Matcher<TypeDescriptor> ofTypeResourceBinary(TypeResourceBinaryDescriptor expected) {
        return given -> {
            if (given instanceof TypeResourceBinaryDescriptor) {
                TypeResourceBinaryDescriptor actual = (TypeResourceBinaryDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static Matcher<TypeDescriptor> ofTypeCombo(TypeComboDescriptor expected) {
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

    public static Matcher<TypeDescriptor> ofTypeMap(TypeMapDescriptor expected) {
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

    public static Matcher<TypeDescriptor> ofTypeScript(TypeScriptDescriptor expected) {
        return given -> {
            if (given instanceof TypeScriptDescriptor) {
                TypeScriptDescriptor actual = (TypeScriptDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static <T extends DynamicValue<?>> Matcher<TypeDescriptor> ofDynamicType(TypeDynamicValueDescriptor expected) {
        return given -> {
            if (given instanceof TypeDynamicValueDescriptor) {
                TypeDynamicValueDescriptor actual = (TypeDynamicValueDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static <T extends DynamicMap<?>> Matcher<TypeDescriptor> ofDynamicMapType(TypeDynamicMapDescriptor expected) {
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

    private static boolean same(TypeDescriptor expected, TypeDescriptor actual) {
        Class<?> expectedClazzType = expected.getType();
        Class<?> actualClazzType = actual.getType();
        return expectedClazzType.equals(actualClazzType);
    }
}

