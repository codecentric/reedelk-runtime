package com.reedelk.module.descriptor.analyzer;

import com.reedelk.module.descriptor.model.*;
import com.reedelk.runtime.api.script.dynamicmap.DynamicMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class TypeDescriptorMatchers {

    public interface TypeDescriptorMatcher {
        boolean matches(TypeDescriptor actual);
    }

    public static TypeDescriptorMatcher ofPrimitiveType(TypePrimitiveDescriptor expected) {
        return given -> {
            if (given instanceof TypePrimitiveDescriptor) {
                TypePrimitiveDescriptor actual = (TypePrimitiveDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static TypeDescriptorMatcher ofTypeEnum(TypeEnumDescriptor expected) {
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

    public static TypeDescriptorMatcher ofTypeResourceText(TypeResourceTextDescriptor expected) {
        return given -> {
            if (given instanceof TypeResourceTextDescriptor) {
                TypeResourceTextDescriptor actual = (TypeResourceTextDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static TypeDescriptorMatcher ofTypeResourceBinary(TypeResourceBinaryDescriptor expected) {
        return given -> {
            if (given instanceof TypeResourceBinaryDescriptor) {
                TypeResourceBinaryDescriptor actual = (TypeResourceBinaryDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static TypeDescriptorMatcher ofTypeCombo(TypeComboDescriptor expected) {
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
                        same(expectedPrototype, actualPrototype) &&
                        expectedEditable == actualEditable &&
                        Arrays.equals(expectedComboValues, actualComboValues);
            }
            return false;
        };
    }

    public static TypeDescriptorMatcher ofTypeMap(TypeMapDescriptor expected) {
        return given -> {
            if (given instanceof TypeMapDescriptor) {
                TypeMapDescriptor actual = (TypeMapDescriptor) given;
                String expectedTabGroup = expected.getTabGroup();
                String actualTabGroup = actual.getTabGroup();
                TabPlacement expectedTabPlacement = expected.getTabPlacement();
                TabPlacement actualTabPlacement = actual.getTabPlacement();
                return same(expected, actual) &&
                        same(expectedTabGroup,actualTabGroup) &&
                        same(expectedTabPlacement, actualTabPlacement);
            }
            return false;
        };
    }

    public static TypeDescriptorMatcher ofTypeScript(TypeScriptDescriptor expected) {
        return given -> {
            if (given instanceof TypeScriptDescriptor) {
                TypeScriptDescriptor actual = (TypeScriptDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static <T extends DynamicValue<?>> TypeDescriptorMatcher ofDynamicType(TypeDynamicValueDescriptor expected) {
        return given -> {
            if (given instanceof TypeDynamicValueDescriptor) {
                TypeDynamicValueDescriptor actual = (TypeDynamicValueDescriptor) given;
                return same(expected, actual);
            }
            return false;
        };
    }

    public static <T extends DynamicMap<?>> TypeDescriptorMatcher ofDynamicMapType(TypeDynamicMapDescriptor expected) {
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

    private static boolean same(Object expected, Object actual) {
        return Objects.equals(expected, actual);
    }
}

