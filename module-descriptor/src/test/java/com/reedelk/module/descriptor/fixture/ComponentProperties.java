package com.reedelk.module.descriptor.fixture;

import com.reedelk.module.descriptor.model.PropertyDescriptor;
import com.reedelk.runtime.api.annotation.InitValue;

public class ComponentProperties {

    private ComponentProperties() {
    }

    public static PropertyDescriptor propertyBooleanObject =
            PropertyDescriptor.builder()
                    .propertyName("propertyBooleanObject")
                    .initValue("true")
                    .example("false")
                    .displayName("Property Boolean Object")
                    .hintValue("true") // In the UI the hint is not applied because it is a checkbox. But we  test it anyway.
                    .propertyDescription("Property Boolean Object Info")
                    .type(TypeDescriptors.typeDescriptorBooleanObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .when(WhenDescriptors.whenDescriptorAlternative)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyBoolean =
            PropertyDescriptor.builder()
                    .propertyName("propertyBoolean")
                    .initValue("true")
                    .displayName("Property Boolean")
                    .hintValue("true") // In the UI the hint is not applied because it is a checkbox. But we  test it anyway.
                    .propertyDescription("Property Boolean Info")
                    .type(TypeDescriptors.typeDescriptorBoolean)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDoubleObject =
            PropertyDescriptor.builder()
                    .propertyName("propertyDoubleObject")
                    .initValue("234.54223")
                    .example("3342.543111")
                    .defaultValue("11.32")
                    .displayName("Property Double Object")
                    .hintValue("1111.2222")
                    .propertyDescription("Property Double Object Info")
                    .type(TypeDescriptors.typeDescriptorDoubleObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDouble =
            PropertyDescriptor.builder()
                    .propertyName("propertyDouble")
                    .initValue("333.8764")
                    .displayName("Property Double")
                    .hintValue("771.32212")
                    .propertyDescription("Property Double Info")
                    .type(TypeDescriptors.typeDescriptorDouble)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyFloatObject =
            PropertyDescriptor.builder()
                    .propertyName("propertyFloatObject")
                    .initValue("3.1f")
                    .displayName("Property Float Object")
                    .hintValue("1.2f")
                    .propertyDescription("Property Float Object Info")
                    .type(TypeDescriptors.typeDescriptorFloatObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyFloat =
            PropertyDescriptor.builder()
                    .propertyName("propertyFloat")
                    .initValue("1.8f")
                    .displayName("Property Float")
                    .hintValue("7.2f")
                    .propertyDescription("Property Float Info")
                    .type(TypeDescriptors.typeDescriptorFloat)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyIntegerObject =
            PropertyDescriptor.builder()
                    .propertyName("propertyIntegerObject")
                    .initValue("122")
                    .displayName("Property Integer Object")
                    .hintValue("809")
                    .propertyDescription("Property Integer Object Info")
                    .type(TypeDescriptors.typeDescriptorIntegerObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyInteger =
            PropertyDescriptor.builder()
                    .propertyName("propertyInteger")
                    .initValue("544")
                    .displayName("Property Integer")
                    .hintValue("764")
                    .propertyDescription("Property Integer Info")
                    .type(TypeDescriptors.typeDescriptorInteger)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyLongObject =
            PropertyDescriptor.builder()
                    .propertyName("propertyLongObject")
                    .initValue("892L")
                    .displayName("Property Long Object")
                    .hintValue("444L")
                    .propertyDescription("Property Long Object Info")
                    .type(TypeDescriptors.typeDescriptorLongObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyLong =
            PropertyDescriptor.builder()
                    .propertyName("propertyLong")
                    .initValue("1L")
                    .defaultValue("2L")
                    .example("711222L")
                    .displayName("Property Long")
                    .hintValue("4")
                    .propertyDescription("Property Long Info")
                    .type(TypeDescriptors.typeDescriptorLong)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyString =
            PropertyDescriptor.builder()
                    .propertyName("propertyString")
                    .initValue("Test property string")
                    .displayName("Property String")
                    .hintValue("Test property string hint")
                    .propertyDescription("Property String Info")
                    .type(TypeDescriptors.typeDescriptorString)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyBigInteger =
            PropertyDescriptor.builder()
                    .propertyName("propertyBigInteger")
                    .initValue("4222222")
                    .displayName("Property BigInteger")
                    .hintValue("1123")
                    .propertyDescription("Property Big Integer Info")
                    .type(TypeDescriptors.typeDescriptorBigInteger)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyBigDecimal =
            PropertyDescriptor.builder()
                    .propertyName("propertyBigDecimal")
                    .initValue("234.2343")
                    .displayName("Property BigDecimal")
                    .hintValue("11.2343")
                    .propertyDescription("Property Big Decimal Info")
                    .type(TypeDescriptors.typeDescriptorBigDecimal)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyMap =
            PropertyDescriptor.builder()
                    .propertyName("propertyMap")
                    .initValue("{'key1':'value1','key2':'value2'}")
                    .displayName("Property Map")
                    .hintValue("{'key1':'value1','key2':'value2'}")
                    .propertyDescription("Property Map Info")
                    .type(TypeDescriptors.typeDescriptorMap)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyEnum =
            PropertyDescriptor.builder()
                    .propertyName("propertyEnum")
                    .initValue("STREAM")
                    .displayName("Property Enum")
                    .hintValue("AUTO")
                    .propertyDescription("Property Enum Info")
                    .type(TypeDescriptors.typeDescriptorEnum)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicLong =
            PropertyDescriptor.builder()
                    .propertyName("propertyDynamicLong")
                    .initValue("432L")
                    .displayName("Property Dynamic Long")
                    .hintValue("8L")
                    .propertyDescription("Property Dynamic Long Info")
                    .type(TypeDescriptors.typeDescriptorDynamicLong)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicFloat =
            PropertyDescriptor.builder()
                    .propertyName("propertyDynamicFloat")
                    .initValue("432.23f")
                    .displayName("Property Dynamic Float")
                    .hintValue("8.1f")
                    .propertyDescription("Property Dynamic Float Info")
                    .type(TypeDescriptors.typeDescriptorDynamicFloat)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicDouble =
            PropertyDescriptor.builder()
                    .propertyName("propertyDynamicDouble")
                    .initValue("1432.23d")
                    .displayName("Property Dynamic Double")
                    .hintValue("18.1d")
                    .propertyDescription("Property Dynamic Double Info")
                    .type(TypeDescriptors.typeDescriptorDynamicDouble)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicObject =
            PropertyDescriptor.builder()
                    .propertyName("propertyDynamicObject")
                    .initValue("Test")
                    .displayName("Property Dynamic Object")
                    .hintValue("Test hint")
                    .propertyDescription("Property Dynamic Object Info")
                    .type(TypeDescriptors.typeDescriptorDynamicObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicString =
            PropertyDescriptor.builder()
                    .propertyName("propertyDynamicString")
                    .initValue("Test string")
                    .displayName("Property Dynamic String")
                    .hintValue("Test string hint")
                    .propertyDescription("Property Dynamic String Info")
                    .type(TypeDescriptors.typeDescriptorDynamicString)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicBoolean =
            PropertyDescriptor.builder()
                    .propertyName("propertyDynamicBoolean")
                    .initValue("true")
                    .displayName("Property Dynamic Boolean")
                    .hintValue("Test boolean hint")
                    .propertyDescription("Property Dynamic Boolean Info")
                    .type(TypeDescriptors.typeDescriptorDynamicBoolean)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicInteger =
            PropertyDescriptor.builder()
                    .propertyName("propertyDynamicInteger")
                    .initValue("4")
                    .displayName("Property Dynamic Integer")
                    .hintValue("Test integer hint")
                    .propertyDescription("Property Dynamic Integer Info")
                    .type(TypeDescriptors.typeDescriptorDynamicInteger)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicResource =
            PropertyDescriptor.builder()
                    .propertyName("propertyDynamicResource")
                    .initValue("#['my-resource.png']")
                    .displayName("Property Dynamic Resource")
                    .hintValue("Test resource hint")
                    .propertyDescription("Property Dynamic Resource Info")
                    .type(TypeDescriptors.typeDescriptorDynamicResource)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicByteArray =
            PropertyDescriptor.builder()
                    .propertyName("propertyDynamicByteArray")
                    .initValue("Test Dynamic Byte Array")
                    .displayName("Property Dynamic byte array")
                    .hintValue("Test byte array hint")
                    .propertyDescription("Property Dynamic Byte Array Info")
                    .type(TypeDescriptors.typeDescriptorDynamicByteArray)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicBigInteger =
            PropertyDescriptor.builder()
                    .propertyName("propertyDynamicBigInteger")
                    .initValue("2348111")
                    .displayName("Property Dynamic Big Integer")
                    .hintValue("1199888")
                    .propertyDescription("Property Dynamic Big Integer Info")
                    .type(TypeDescriptors.typeDescriptorDynamicBigInteger)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicBigDecimal =
            PropertyDescriptor.builder()
                    .propertyName("propertyDynamicBigDecimal")
                    .initValue("234.8111")
                    .displayName("Property Dynamic Big Decimal")
                    .hintValue("1.199888")
                    .propertyDescription("Property Dynamic Big Decimal Info")
                    .type(TypeDescriptors.typeDescriptorDynamicBigDecimal)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicStringMap =
            PropertyDescriptor.builder()
                    .propertyName("propertyDynamicStringMap")
                    .initValue("{'key1':'#['value1']','key2':'#['value2']'}")
                    .displayName("Property Dynamic String Map")
                    .hintValue("{'key1':'#['value1']'}")
                    .propertyDescription("Property Dynamic String Map Info")
                    .type(TypeDescriptors.typeDescriptorDynamicStringMap)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyScript =
            PropertyDescriptor.builder()
                    .propertyName("propertyScript")
                    .initValue("#['Test']")
                    .displayName("Property Script")
                    .hintValue("#['Test Hint']")
                    .propertyDescription("Property Script Info")
                    .type(TypeDescriptors.typeDescriptorScript)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyCombo =
            PropertyDescriptor.builder()
                    .propertyName("propertyCombo")
                    .initValue("Item1")
                    .displayName("Property Combo")
                    .hintValue("Item2")
                    .propertyDescription("Property Combo Info")
                    .type(TypeDescriptors.typeDescriptorCombo)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyPassword =
            PropertyDescriptor.builder()
                    .propertyName("propertyPassword")
                    .initValue("my password")
                    .displayName("Property Password")
                    .hintValue("my password hint")
                    .propertyDescription("Property Password Info")
                    .type(TypeDescriptors.typeDescriptorPassword)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyResourceText =
            PropertyDescriptor.builder()
                    .propertyName("propertyResourceText")
                    .initValue("assets/my-text.txt")
                    .displayName("Property Resource Text")
                    .hintValue("assets/images/my-text.txt")
                    .propertyDescription("Property Resource Text Info")
                    .type(TypeDescriptors.typeDescriptorResourceText)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyResourceBinary =
            PropertyDescriptor.builder()
                    .propertyName("propertyResourceBinary")
                    .initValue("assets/my-image.png")
                    .displayName("Property Resource Binary")
                    .hintValue("assets/images/my-image.png")
                    .propertyDescription("Property Resource Binary Info")
                    .type(TypeDescriptors.typeDescriptorResourceBinary)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyTypeObject =
            PropertyDescriptor.builder()
                    .propertyName("propertyTypeObject")
                    .displayName("Property Type Object")
                    .propertyDescription("Property Type Object Info")
                    .type(TypeDescriptors.typeObjectDescriptor)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyStringWithInitValue =
            PropertyDescriptor.builder()
                    .propertyName("propertyStringWithInitValue")
                    .initValue(InitValue.USE_DEFAULT_VALUE)
                    .displayName("Property String With Init Value")
                    .hintValue("Test string with default hint")
                    .propertyDescription("Property String With Default Info")
                    .type(TypeDescriptors.typeDescriptorString)
                    .build();
}
