package com.reedelk.module.descriptor.fixture;

import com.reedelk.module.descriptor.model.*;
import com.reedelk.runtime.api.commons.ImmutableMap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class TypeDescriptors {

    private TypeDescriptors() {
    }

    public static final TypeDescriptor typeDescriptorBooleanObject = newTypePrimitiveDescriptor(Boolean.class);
    public static final TypeDescriptor typeDescriptorBoolean = newTypePrimitiveDescriptor(boolean.class);
    public static final TypeDescriptor typeDescriptorDoubleObject = newTypePrimitiveDescriptor(Double.class);
    public static final TypeDescriptor typeDescriptorDouble = newTypePrimitiveDescriptor(double.class);
    public static final TypeDescriptor typeDescriptorFloatObject = newTypePrimitiveDescriptor(Float.class);
    public static final TypeDescriptor typeDescriptorFloat = newTypePrimitiveDescriptor(float.class);
    public static final TypeDescriptor typeDescriptorIntegerObject = newTypePrimitiveDescriptor(Integer.class);
    public static final TypeDescriptor typeDescriptorInteger = newTypePrimitiveDescriptor(int.class);
    public static final TypeDescriptor typeDescriptorLongObject = newTypePrimitiveDescriptor(Long.class);
    public static final TypeDescriptor typeDescriptorLong = newTypePrimitiveDescriptor(long.class);
    public static final TypeDescriptor typeDescriptorString = newTypePrimitiveDescriptor(String.class);
    public static final TypeDescriptor typeDescriptorBigInteger = newTypePrimitiveDescriptor(BigInteger.class);
    public static final TypeDescriptor typeDescriptorBigDecimal = newTypePrimitiveDescriptor(BigDecimal.class);
    public static final TypeDescriptor typeDescriptorMap = newTypeMapDescriptor("myTestGroup1");
    public static final TypeDescriptor typeDescriptorEnum = newTypeEnumDescriptor(ImmutableMap.of("STREAM", "Stream", "AUTO", "Auto"));
    public static final TypeDescriptor typeDescriptorDynamicLong = newTypeDynamicValueDescriptor(Long.class);
    public static final TypeDescriptor typeDescriptorDynamicFloat = newTypeDynamicValueDescriptor(Float.class);
    public static final TypeDescriptor typeDescriptorDynamicDouble = newTypeDynamicValueDescriptor(Double.class);
    public static final TypeDescriptor typeDescriptorDynamicObject = newTypeDynamicValueDescriptor(Object.class);
    public static final TypeDescriptor typeDescriptorDynamicString = newTypeDynamicValueDescriptor(String.class);
    public static final TypeDescriptor typeDescriptorDynamicBoolean = newTypeDynamicValueDescriptor(Boolean.class);
    public static final TypeDescriptor typeDescriptorDynamicInteger = newTypeDynamicValueDescriptor(Integer.class);
    public static final TypeDescriptor typeDescriptorDynamicResource = newTypeDynamicValueDescriptor(String.class);
    public static final TypeDescriptor typeDescriptorDynamicByteArray = newTypeDynamicValueDescriptor(byte[].class);
    public static final TypeDescriptor typeDescriptorDynamicBigInteger = newTypeDynamicValueDescriptor(BigInteger.class);
    public static final TypeDescriptor typeDescriptorDynamicBigDecimal = newTypeDynamicValueDescriptor(BigDecimal.class);
    public static final TypeDescriptor typeDescriptorDynamicStringMap = newTypeDynamicMapDescriptor(String.class, "myTestGroup2");
    public static final TypeDescriptor typeDescriptorScript = newTypeScriptDescriptor();
    public static final TypeDescriptor typeDescriptorCombo = newTypeDescriptorCombo(true, "XX", "Item1", "Item2", "Item3");
    public static final TypeDescriptor typeDescriptorPassword = newTypeDescriptorPassword();
    public static final TypeDescriptor typeDescriptorResourceText = newTypeDescriptorResourceText();
    public static final TypeDescriptor typeDescriptorResourceBinary = newTypeDescriptorResourceBinary();
    public static final TypeDescriptor typeObjectDescriptor = newTypeDescriptorObject(
            "com.reedelk.component.TestImplementor",
            asList(PropertyDescriptor.builder()
                            .name("nestedProperty1")
                            .initValue("my nested property 1")
                            .displayName("Nested Property 1")
                            .hintValue("nested property 1 hint")
                            .description("Property Nested 1 Info")
                            .type(TypeDescriptors.typeDescriptorString)
                            .when(WhenDescriptors.whenDescriptorDefault)
                            .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                            .build(),
                    PropertyDescriptor.builder()
                            .name("nestedProperty2")
                            .initValue("5")
                            .displayName("Nested Property 2")
                            .hintValue("12")
                            .description("Property Nested 2 Info")
                            .type(TypeDescriptors.typeDescriptorInteger)
                            .when(WhenDescriptors.whenDescriptorDefault)
                            .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                            .build()),
            Collapsible.YES,
            Shared.YES);

    private static TypeDescriptor newTypePrimitiveDescriptor(Class<?> clazz) {
        TypeDescriptor descriptor = new TypePrimitiveDescriptor();
        descriptor.setType(clazz);
        return descriptor;
    }

    private static TypeDescriptor newTypeDynamicValueDescriptor(Class<?> clazz) {
        TypeDynamicValueDescriptor descriptor = new TypeDynamicValueDescriptor();
        descriptor.setType(clazz);
        return descriptor;
    }

    private static TypeDescriptor newTypeMapDescriptor(String tabGroup) {
        TypeMapDescriptor descriptor = new TypeMapDescriptor();
        descriptor.setTabGroup(tabGroup);
        return descriptor;
    }

    private static TypeDescriptor newTypeDynamicMapDescriptor(Class<?> clazz, String tabGroup) {
        TypeDynamicMapDescriptor descriptor = new TypeDynamicMapDescriptor();
        descriptor.setType(clazz);
        descriptor.setTabGroup(tabGroup);
        return descriptor;
    }

    private static TypeDescriptor newTypeEnumDescriptor(Map<String, String> nameAndDisplayNameMap) {
        TypeEnumDescriptor descriptor = new TypeEnumDescriptor();
        descriptor.setNameAndDisplayNameMap(nameAndDisplayNameMap);
        return descriptor;
    }

    private static TypeDescriptor newTypeDescriptorObject(String fullyQualifiedName, List<PropertyDescriptor> properties, Collapsible collapsible, Shared shared) {
        TypeObjectDescriptor descriptor = new TypeObjectDescriptor();
        descriptor.setShared(shared);
        descriptor.setCollapsible(collapsible);
        descriptor.setObjectProperties(properties);
        descriptor.setTypeFullyQualifiedName(fullyQualifiedName);
        return descriptor;
    }

    private static TypeDescriptor newTypeDescriptorResourceBinary() {
        return new TypeResourceBinaryDescriptor();
    }

    private static TypeDescriptor newTypeDescriptorResourceText() {
        return new TypeResourceTextDescriptor();
    }

    private static TypeDescriptor newTypeDescriptorPassword() {
        return new TypePasswordDescriptor();
    }

    private static TypeDescriptor newTypeDescriptorCombo(boolean editable, String prototype, String... comboValues) {
        TypeComboDescriptor descriptor = new TypeComboDescriptor();
        descriptor.setEditable(editable);
        descriptor.setPrototype(prototype);
        descriptor.setComboValues(comboValues);
        return descriptor;
    }

    private static TypeDescriptor newTypeScriptDescriptor() {
        return new TypeScriptDescriptor();
    }
}
