package com.reedelk.module.descriptor.fixture;

import com.reedelk.module.descriptor.model.ComponentPropertyDescriptor;
import com.reedelk.runtime.api.annotation.InitValue;

public class ComponentProperties {

    private ComponentProperties() {
    }

    public static ComponentPropertyDescriptor propertyBooleanObject =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyBooleanObject")
                    .initValue("true")
                    .displayName("Property Boolean Object")
                    .hintValue("true") // In the UI the hint is not applied because it is a checkbox. But we  test it anyway.
                    .propertyInfo("Property Boolean Object Info")
                    .type(TypeDescriptors.typeDescriptorBooleanObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .when(WhenDescriptors.whenDescriptorAlternative)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyBoolean =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyBoolean")
                    .initValue("true")
                    .displayName("Property Boolean")
                    .hintValue("true") // In the UI the hint is not applied because it is a checkbox. But we  test it anyway.
                    .propertyInfo("Property Boolean Info")
                    .type(TypeDescriptors.typeDescriptorBoolean)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyDoubleObject =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyDoubleObject")
                    .initValue("234.54223")
                    .displayName("Property Double Object")
                    .hintValue("1111.2222")
                    .propertyInfo("Property Double Object Info")
                    .type(TypeDescriptors.typeDescriptorDoubleObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyDouble =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyDouble")
                    .initValue("333.8764")
                    .displayName("Property Double")
                    .hintValue("771.32212")
                    .propertyInfo("Property Double Info")
                    .type(TypeDescriptors.typeDescriptorDouble)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyFloatObject =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyFloatObject")
                    .initValue("3.1f")
                    .displayName("Property Float Object")
                    .hintValue("1.2f")
                    .propertyInfo("Property Float Object Info")
                    .type(TypeDescriptors.typeDescriptorFloatObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyFloat =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyFloat")
                    .initValue("1.8f")
                    .displayName("Property Float")
                    .hintValue("7.2f")
                    .propertyInfo("Property Float Info")
                    .type(TypeDescriptors.typeDescriptorFloat)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyIntegerObject =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyIntegerObject")
                    .initValue("122")
                    .displayName("Property Integer Object")
                    .hintValue("809")
                    .propertyInfo("Property Integer Object Info")
                    .type(TypeDescriptors.typeDescriptorIntegerObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyInteger =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyInteger")
                    .initValue("544")
                    .displayName("Property Integer")
                    .hintValue("764")
                    .propertyInfo("Property Integer Info")
                    .type(TypeDescriptors.typeDescriptorInteger)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyLongObject =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyLongObject")
                    .initValue("892L")
                    .displayName("Property Long Object")
                    .hintValue("444L")
                    .propertyInfo("Property Long Object Info")
                    .type(TypeDescriptors.typeDescriptorLongObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyLong =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyLong")
                    .initValue("1L")
                    .displayName("Property Long")
                    .hintValue("4")
                    .propertyInfo("Property Long Info")
                    .type(TypeDescriptors.typeDescriptorLong)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyString =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyString")
                    .initValue("Test property string")
                    .displayName("Property String")
                    .hintValue("Test property string hint")
                    .propertyInfo("Property String Info")
                    .type(TypeDescriptors.typeDescriptorString)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyBigInteger =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyBigInteger")
                    .initValue("4222222")
                    .displayName("Property BigInteger")
                    .hintValue("1123")
                    .propertyInfo("Property Big Integer Info")
                    .type(TypeDescriptors.typeDescriptorBigInteger)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyBigDecimal =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyBigDecimal")
                    .initValue("234.2343")
                    .displayName("Property BigDecimal")
                    .hintValue("11.2343")
                    .propertyInfo("Property Big Decimal Info")
                    .type(TypeDescriptors.typeDescriptorBigDecimal)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyMap =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyMap")
                    .initValue("{'key1':'value1','key2':'value2'}")
                    .displayName("Property Map")
                    .hintValue("{'key1':'value1','key2':'value2'}")
                    .propertyInfo("Property Map Info")
                    .type(TypeDescriptors.typeDescriptorMap)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyEnum =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyEnum")
                    .initValue("STREAM")
                    .displayName("Property Enum")
                    .hintValue("AUTO")
                    .propertyInfo("Property Enum Info")
                    .type(TypeDescriptors.typeDescriptorEnum)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyDynamicLong =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyDynamicLong")
                    .initValue("432L")
                    .displayName("Property Dynamic Long")
                    .hintValue("8L")
                    .propertyInfo("Property Dynamic Long Info")
                    .type(TypeDescriptors.typeDescriptorDynamicLong)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyDynamicFloat =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyDynamicFloat")
                    .initValue("432.23f")
                    .displayName("Property Dynamic Float")
                    .hintValue("8.1f")
                    .propertyInfo("Property Dynamic Float Info")
                    .type(TypeDescriptors.typeDescriptorDynamicFloat)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyDynamicDouble =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyDynamicDouble")
                    .initValue("1432.23d")
                    .displayName("Property Dynamic Double")
                    .hintValue("18.1d")
                    .propertyInfo("Property Dynamic Double Info")
                    .type(TypeDescriptors.typeDescriptorDynamicDouble)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyDynamicObject =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyDynamicObject")
                    .initValue("Test")
                    .displayName("Property Dynamic Object")
                    .hintValue("Test hint")
                    .propertyInfo("Property Dynamic Object Info")
                    .type(TypeDescriptors.typeDescriptorDynamicObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyDynamicString =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyDynamicString")
                    .initValue("Test string")
                    .displayName("Property Dynamic String")
                    .hintValue("Test string hint")
                    .propertyInfo("Property Dynamic String Info")
                    .type(TypeDescriptors.typeDescriptorDynamicString)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyDynamicBoolean =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyDynamicBoolean")
                    .initValue("true")
                    .displayName("Property Dynamic Boolean")
                    .hintValue("Test boolean hint")
                    .propertyInfo("Property Dynamic Boolean Info")
                    .type(TypeDescriptors.typeDescriptorDynamicBoolean)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyDynamicInteger =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyDynamicInteger")
                    .initValue("4")
                    .displayName("Property Dynamic Integer")
                    .hintValue("Test integer hint")
                    .propertyInfo("Property Dynamic Integer Info")
                    .type(TypeDescriptors.typeDescriptorDynamicInteger)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyDynamicResource =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyDynamicResource")
                    .initValue("#['my-resource.png']")
                    .displayName("Property Dynamic Resource")
                    .hintValue("Test resource hint")
                    .propertyInfo("Property Dynamic Resource Info")
                    .type(TypeDescriptors.typeDescriptorDynamicResource)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyDynamicByteArray =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyDynamicByteArray")
                    .initValue("Test Dynamic Byte Array")
                    .displayName("Property Dynamic byte array")
                    .hintValue("Test byte array hint")
                    .propertyInfo("Property Dynamic Byte Array Info")
                    .type(TypeDescriptors.typeDescriptorDynamicByteArray)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyDynamicBigInteger =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyDynamicBigInteger")
                    .initValue("2348111")
                    .displayName("Property Dynamic Big Integer")
                    .hintValue("1199888")
                    .propertyInfo("Property Dynamic Big Integer Info")
                    .type(TypeDescriptors.typeDescriptorDynamicBigInteger)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyDynamicBigDecimal =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyDynamicBigDecimal")
                    .initValue("234.8111")
                    .displayName("Property Dynamic Big Decimal")
                    .hintValue("1.199888")
                    .propertyInfo("Property Dynamic Big Decimal Info")
                    .type(TypeDescriptors.typeDescriptorDynamicBigDecimal)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyDynamicStringMap =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyDynamicStringMap")
                    .initValue("{'key1':'#['value1']','key2':'#['value2']'}")
                    .displayName("Property Dynamic String Map")
                    .hintValue("{'key1':'#['value1']'}")
                    .propertyInfo("Property Dynamic String Map Info")
                    .type(TypeDescriptors.typeDescriptorDynamicStringMap)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyScript =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyScript")
                    .initValue("#['Test']")
                    .displayName("Property Script")
                    .hintValue("#['Test Hint']")
                    .propertyInfo("Property Script Info")
                    .type(TypeDescriptors.typeDescriptorScript)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyCombo =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyCombo")
                    .initValue("Item1")
                    .displayName("Property Combo")
                    .hintValue("Item2")
                    .propertyInfo("Property Combo Info")
                    .type(TypeDescriptors.typeDescriptorCombo)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyPassword =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyPassword")
                    .initValue("my password")
                    .displayName("Property Password")
                    .hintValue("my password hint")
                    .propertyInfo("Property Password Info")
                    .type(TypeDescriptors.typeDescriptorPassword)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyResourceText =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyResourceText")
                    .initValue("assets/my-text.txt")
                    .displayName("Property Resource Text")
                    .hintValue("assets/images/my-text.txt")
                    .propertyInfo("Property Resource Text Info")
                    .type(TypeDescriptors.typeDescriptorResourceText)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyResourceBinary =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyResourceBinary")
                    .initValue("assets/my-image.png")
                    .displayName("Property Resource Binary")
                    .hintValue("assets/images/my-image.png")
                    .propertyInfo("Property Resource Binary Info")
                    .type(TypeDescriptors.typeDescriptorResourceBinary)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyTypeObject =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyTypeObject")
                    .displayName("Property Type Object")
                    .propertyInfo("Property Type Object Info")
                    .type(TypeDescriptors.typeObjectDescriptor)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static ComponentPropertyDescriptor propertyStringWithInitValue =
            ComponentPropertyDescriptor.builder()
                    .propertyName("propertyStringWithInitValue")
                    .initValue(InitValue.USE_DEFAULT_VALUE)
                    .displayName("Property String With Init Value")
                    .hintValue("Test string with default hint")
                    .propertyInfo("Property String With Default Info")
                    .type(TypeDescriptors.typeDescriptorString)
                    .build();
}
