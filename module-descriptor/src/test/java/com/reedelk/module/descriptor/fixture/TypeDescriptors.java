package com.reedelk.module.descriptor.fixture;

import com.reedelk.module.descriptor.model.property.*;
import com.reedelk.runtime.api.commons.ImmutableMap;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class TypeDescriptors {

    private TypeDescriptors() {
    }

    public static final PropertyTypeDescriptor typeDescriptorBooleanObject = newTypePrimitiveDescriptor(Boolean.class);
    public static final PropertyTypeDescriptor typeDescriptorBoolean = newTypePrimitiveDescriptor(boolean.class);
    public static final PropertyTypeDescriptor typeDescriptorDoubleObject = newTypePrimitiveDescriptor(Double.class);
    public static final PropertyTypeDescriptor typeDescriptorDouble = newTypePrimitiveDescriptor(double.class);
    public static final PropertyTypeDescriptor typeDescriptorFloatObject = newTypePrimitiveDescriptor(Float.class);
    public static final PropertyTypeDescriptor typeDescriptorFloat = newTypePrimitiveDescriptor(float.class);
    public static final PropertyTypeDescriptor typeDescriptorIntegerObject = newTypePrimitiveDescriptor(Integer.class);
    public static final PropertyTypeDescriptor typeDescriptorInteger = newTypePrimitiveDescriptor(int.class);
    public static final PropertyTypeDescriptor typeDescriptorLongObject = newTypePrimitiveDescriptor(Long.class);
    public static final PropertyTypeDescriptor typeDescriptorLong = newTypePrimitiveDescriptor(long.class);
    public static final PropertyTypeDescriptor typeDescriptorString = newTypePrimitiveDescriptor(String.class);
    public static final PropertyTypeDescriptor typeDescriptorBigInteger = newTypePrimitiveDescriptor(BigInteger.class);
    public static final PropertyTypeDescriptor typeDescriptorBigDecimal = newTypePrimitiveDescriptor(BigDecimal.class);
    public static final PropertyTypeDescriptor typeDescriptorMap = newTypeMapDescriptor("myTestGroup1");
    public static final PropertyTypeDescriptor typeDescriptorEnum = newTypeEnumDescriptor(ImmutableMap.of("STREAM", "Stream", "AUTO", "Auto"));
    public static final PropertyTypeDescriptor typeDescriptorDynamicLong = newTypeDynamicValueDescriptor(Long.class);
    public static final PropertyTypeDescriptor typeDescriptorDynamicFloat = newTypeDynamicValueDescriptor(Float.class);
    public static final PropertyTypeDescriptor typeDescriptorDynamicDouble = newTypeDynamicValueDescriptor(Double.class);
    public static final PropertyTypeDescriptor typeDescriptorDynamicObject = newTypeDynamicValueDescriptor(Object.class);
    public static final PropertyTypeDescriptor typeDescriptorDynamicString = newTypeDynamicValueDescriptor(String.class);
    public static final PropertyTypeDescriptor typeDescriptorDynamicBoolean = newTypeDynamicValueDescriptor(Boolean.class);
    public static final PropertyTypeDescriptor typeDescriptorDynamicInteger = newTypeDynamicValueDescriptor(Integer.class);
    public static final PropertyTypeDescriptor typeDescriptorDynamicResource = newTypeDynamicValueDescriptor(String.class);
    public static final PropertyTypeDescriptor typeDescriptorDynamicByteArray = newTypeDynamicValueDescriptor(byte[].class);
    public static final PropertyTypeDescriptor typeDescriptorDynamicBigInteger = newTypeDynamicValueDescriptor(BigInteger.class);
    public static final PropertyTypeDescriptor typeDescriptorDynamicBigDecimal = newTypeDynamicValueDescriptor(BigDecimal.class);
    public static final PropertyTypeDescriptor typeDescriptorDynamicStringMap = newTypeDynamicMapDescriptor(String.class, "myTestGroup2");
    public static final PropertyTypeDescriptor typeDescriptorScript = newTypeScriptDescriptor();
    public static final PropertyTypeDescriptor typeDescriptorCombo = newTypeDescriptorCombo(true, "XX", "Item1", "Item2", "Item3");
    public static final PropertyTypeDescriptor typeDescriptorPassword = newTypeDescriptorPassword();
    public static final PropertyTypeDescriptor typeDescriptorResourceText = newTypeDescriptorResourceText();
    public static final PropertyTypeDescriptor typeDescriptorResourceBinary = newTypeDescriptorResourceBinary();
    public static final PropertyTypeDescriptor typeObjectDescriptor = newTypeDescriptorObject(
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

    private static PropertyTypeDescriptor newTypePrimitiveDescriptor(Class<?> clazz) {
        PropertyTypeDescriptor descriptor = new PrimitiveDescriptor();
        descriptor.setType(clazz);
        return descriptor;
    }

    private static PropertyTypeDescriptor newTypeDynamicValueDescriptor(Class<?> clazz) {
        DynamicValueDescriptor descriptor = new DynamicValueDescriptor();
        descriptor.setType(clazz);
        return descriptor;
    }

    private static PropertyTypeDescriptor newTypeMapDescriptor(String tabGroup) {
        MapDescriptor descriptor = new MapDescriptor();
        descriptor.setTabGroup(tabGroup);
        return descriptor;
    }

    private static PropertyTypeDescriptor newTypeDynamicMapDescriptor(Class<?> clazz, String tabGroup) {
        DynamicMapDescriptor descriptor = new DynamicMapDescriptor();
        descriptor.setType(clazz);
        descriptor.setTabGroup(tabGroup);
        return descriptor;
    }

    private static PropertyTypeDescriptor newTypeEnumDescriptor(Map<String, String> nameAndDisplayNameMap) {
        EnumDescriptor descriptor = new EnumDescriptor();
        descriptor.setNameAndDisplayNameMap(nameAndDisplayNameMap);
        return descriptor;
    }

    private static PropertyTypeDescriptor newTypeDescriptorObject(String fullyQualifiedName, List<PropertyDescriptor> properties, Collapsible collapsible, Shared shared) {
        ObjectDescriptor descriptor = new ObjectDescriptor();
        descriptor.setShared(shared);
        descriptor.setCollapsible(collapsible);
        descriptor.setObjectProperties(properties);
        descriptor.setTypeFullyQualifiedName(fullyQualifiedName);
        return descriptor;
    }

    private static PropertyTypeDescriptor newTypeDescriptorResourceBinary() {
        return new ResourceBinaryDescriptor();
    }

    private static PropertyTypeDescriptor newTypeDescriptorResourceText() {
        return new ResourceTextDescriptor();
    }

    private static PropertyTypeDescriptor newTypeDescriptorPassword() {
        return new PasswordDescriptor();
    }

    private static PropertyTypeDescriptor newTypeDescriptorCombo(boolean editable, String prototype, String... comboValues) {
        ComboDescriptor descriptor = new ComboDescriptor();
        descriptor.setEditable(editable);
        descriptor.setPrototype(prototype);
        descriptor.setComboValues(comboValues);
        return descriptor;
    }

    private static PropertyTypeDescriptor newTypeScriptDescriptor() {
        return new ScriptDescriptor();
    }
}
