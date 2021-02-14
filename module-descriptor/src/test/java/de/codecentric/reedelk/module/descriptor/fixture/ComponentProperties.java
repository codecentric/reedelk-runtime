package de.codecentric.reedelk.module.descriptor.fixture;

import de.codecentric.reedelk.module.descriptor.model.property.PropertyDescriptor;
import de.codecentric.reedelk.runtime.api.annotation.InitValue;

public class ComponentProperties {

    private ComponentProperties() {
    }

    public static PropertyDescriptor propertyBooleanObject =
            PropertyDescriptor.builder()
                    .name("propertyBooleanObject")
                    .initValue("true")
                    .example("false")
                    .displayName("Property Boolean Object")
                    .hintValue("true") // In the UI the hint is not applied because it is a checkbox. But we  test it anyway.
                    .description("Property Boolean Object Info")
                    .type(TypeDescriptors.typeDescriptorBooleanObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .when(WhenDescriptors.whenDescriptorAlternative)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyBoolean =
            PropertyDescriptor.builder()
                    .name("propertyBoolean")
                    .initValue("true")
                    .displayName("Property Boolean")
                    .hintValue("true") // In the UI the hint is not applied because it is a checkbox. But we  test it anyway.
                    .description("Property Boolean Info")
                    .type(TypeDescriptors.typeDescriptorBoolean)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDoubleObject =
            PropertyDescriptor.builder()
                    .mandatory()
                    .name("propertyDoubleObject")
                    .initValue("234.54223")
                    .example("3342.543111")
                    .defaultValue("11.32")
                    .displayName("Property Double Object")
                    .hintValue("1111.2222")
                    .description("Property Double Object Info")
                    .type(TypeDescriptors.typeDescriptorDoubleObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDouble =
            PropertyDescriptor.builder()
                    .name("propertyDouble")
                    .initValue("333.8764")
                    .displayName("Property Double")
                    .hintValue("771.32212")
                    .description("Property Double Info")
                    .type(TypeDescriptors.typeDescriptorDouble)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyFloatObject =
            PropertyDescriptor.builder()
                    .name("propertyFloatObject")
                    .initValue("3.1f")
                    .displayName("Property Float Object")
                    .hintValue("1.2f")
                    .description("Property Float Object Info")
                    .type(TypeDescriptors.typeDescriptorFloatObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyFloat =
            PropertyDescriptor.builder()
                    .name("propertyFloat")
                    .initValue("1.8f")
                    .displayName("Property Float")
                    .hintValue("7.2f")
                    .description("Property Float Info")
                    .type(TypeDescriptors.typeDescriptorFloat)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyIntegerObject =
            PropertyDescriptor.builder()
                    .name("propertyIntegerObject")
                    .initValue("122")
                    .displayName("Property Integer Object")
                    .hintValue("809")
                    .description("Property Integer Object Info")
                    .type(TypeDescriptors.typeDescriptorIntegerObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyInteger =
            PropertyDescriptor.builder()
                    .name("propertyInteger")
                    .initValue("544")
                    .displayName("Property Integer")
                    .hintValue("764")
                    .description("Property Integer Info")
                    .type(TypeDescriptors.typeDescriptorInteger)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyLongObject =
            PropertyDescriptor.builder()
                    .name("propertyLongObject")
                    .initValue("892L")
                    .displayName("Property Long Object")
                    .hintValue("444L")
                    .description("Property Long Object Info")
                    .type(TypeDescriptors.typeDescriptorLongObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyLong =
            PropertyDescriptor.builder()
                    .name("propertyLong")
                    .initValue("1L")
                    .defaultValue("2L")
                    .example("711222L")
                    .displayName("Property Long")
                    .hintValue("4")
                    .description("Property Long Info")
                    .type(TypeDescriptors.typeDescriptorLong)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyString =
            PropertyDescriptor.builder()
                    .name("propertyString")
                    .initValue("Test property string")
                    .displayName("Property String")
                    .hintValue("Test property string hint")
                    .description("Property String Info")
                    .type(TypeDescriptors.typeDescriptorString)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyBigInteger =
            PropertyDescriptor.builder()
                    .name("propertyBigInteger")
                    .initValue("4222222")
                    .displayName("Property BigInteger")
                    .hintValue("1123")
                    .description("Property Big Integer Info")
                    .type(TypeDescriptors.typeDescriptorBigInteger)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyBigDecimal =
            PropertyDescriptor.builder()
                    .name("propertyBigDecimal")
                    .initValue("234.2343")
                    .displayName("Property BigDecimal")
                    .hintValue("11.2343")
                    .description("Property Big Decimal Info")
                    .type(TypeDescriptors.typeDescriptorBigDecimal)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyMap =
            PropertyDescriptor.builder()
                    .name("propertyMap")
                    .initValue("{'key1':'value1','key2':'value2'}")
                    .displayName("Property Map")
                    .hintValue("{'key1':'value1','key2':'value2'}")
                    .description("Property Map Info")
                    .type(TypeDescriptors.typeDescriptorMap)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyEnum =
            PropertyDescriptor.builder()
                    .name("propertyEnum")
                    .initValue("STREAM")
                    .displayName("Property Enum")
                    .hintValue("AUTO")
                    .description("Property Enum Info")
                    .type(TypeDescriptors.typeDescriptorEnum)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicLong =
            PropertyDescriptor.builder()
                    .name("propertyDynamicLong")
                    .initValue("432L")
                    .displayName("Property Dynamic Long")
                    .hintValue("8L")
                    .description("Property Dynamic Long Info")
                    .type(TypeDescriptors.typeDescriptorDynamicLong)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicFloat =
            PropertyDescriptor.builder()
                    .name("propertyDynamicFloat")
                    .initValue("432.23f")
                    .displayName("Property Dynamic Float")
                    .hintValue("8.1f")
                    .description("Property Dynamic Float Info")
                    .type(TypeDescriptors.typeDescriptorDynamicFloat)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicDouble =
            PropertyDescriptor.builder()
                    .name("propertyDynamicDouble")
                    .initValue("1432.23d")
                    .displayName("Property Dynamic Double")
                    .hintValue("18.1d")
                    .description("Property Dynamic Double Info")
                    .type(TypeDescriptors.typeDescriptorDynamicDouble)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicObject =
            PropertyDescriptor.builder()
                    .name("propertyDynamicObject")
                    .initValue("Test")
                    .displayName("Property Dynamic Object")
                    .hintValue("Test hint")
                    .description("Property Dynamic Object Info")
                    .type(TypeDescriptors.typeDescriptorDynamicObject)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicString =
            PropertyDescriptor.builder()
                    .name("propertyDynamicString")
                    .initValue("Test string")
                    .displayName("Property Dynamic String")
                    .hintValue("Test string hint")
                    .description("Property Dynamic String Info")
                    .type(TypeDescriptors.typeDescriptorDynamicString)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicBoolean =
            PropertyDescriptor.builder()
                    .name("propertyDynamicBoolean")
                    .initValue("true")
                    .displayName("Property Dynamic Boolean")
                    .hintValue("Test boolean hint")
                    .description("Property Dynamic Boolean Info")
                    .type(TypeDescriptors.typeDescriptorDynamicBoolean)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicInteger =
            PropertyDescriptor.builder()
                    .name("propertyDynamicInteger")
                    .initValue("4")
                    .displayName("Property Dynamic Integer")
                    .hintValue("Test integer hint")
                    .description("Property Dynamic Integer Info")
                    .type(TypeDescriptors.typeDescriptorDynamicInteger)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicResource =
            PropertyDescriptor.builder()
                    .name("propertyDynamicResource")
                    .initValue("#['my-resource.png']")
                    .displayName("Property Dynamic Resource")
                    .hintValue("Test resource hint")
                    .description("Property Dynamic Resource Info")
                    .type(TypeDescriptors.typeDescriptorDynamicResource)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicByteArray =
            PropertyDescriptor.builder()
                    .name("propertyDynamicByteArray")
                    .initValue("Test Dynamic Byte Array")
                    .displayName("Property Dynamic byte array")
                    .hintValue("Test byte array hint")
                    .description("Property Dynamic Byte Array Info")
                    .type(TypeDescriptors.typeDescriptorDynamicByteArray)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicBigInteger =
            PropertyDescriptor.builder()
                    .name("propertyDynamicBigInteger")
                    .initValue("2348111")
                    .displayName("Property Dynamic Big Integer")
                    .hintValue("1199888")
                    .description("Property Dynamic Big Integer Info")
                    .type(TypeDescriptors.typeDescriptorDynamicBigInteger)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicBigDecimal =
            PropertyDescriptor.builder()
                    .name("propertyDynamicBigDecimal")
                    .initValue("234.8111")
                    .displayName("Property Dynamic Big Decimal")
                    .hintValue("1.199888")
                    .description("Property Dynamic Big Decimal Info")
                    .type(TypeDescriptors.typeDescriptorDynamicBigDecimal)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyDynamicStringMap =
            PropertyDescriptor.builder()
                    .name("propertyDynamicStringMap")
                    .initValue("{'key1':'#['value1']','key2':'#['value2']'}")
                    .displayName("Property Dynamic String Map")
                    .hintValue("{'key1':'#['value1']'}")
                    .description("Property Dynamic String Map Info")
                    .type(TypeDescriptors.typeDescriptorDynamicStringMap)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyScript =
            PropertyDescriptor.builder()
                    .name("propertyScript")
                    .initValue("#['Test']")
                    .displayName("Property Script")
                    .hintValue("#['Test Hint']")
                    .description("Property Script Info")
                    .type(TypeDescriptors.typeDescriptorScript)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyCombo =
            PropertyDescriptor.builder()
                    .name("propertyCombo")
                    .initValue("Item1")
                    .displayName("Property Combo")
                    .hintValue("Item2")
                    .description("Property Combo Info")
                    .type(TypeDescriptors.typeDescriptorCombo)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyPassword =
            PropertyDescriptor.builder()
                    .name("propertyPassword")
                    .initValue("my password")
                    .displayName("Property Password")
                    .hintValue("my password hint")
                    .description("Property Password Info")
                    .type(TypeDescriptors.typeDescriptorPassword)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyResourceText =
            PropertyDescriptor.builder()
                    .name("propertyResourceText")
                    .initValue("assets/my-text.txt")
                    .displayName("Property Resource Text")
                    .hintValue("assets/images/my-text.txt")
                    .description("Property Resource Text Info")
                    .type(TypeDescriptors.typeDescriptorResourceText)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyResourceBinary =
            PropertyDescriptor.builder()
                    .name("propertyResourceBinary")
                    .initValue("assets/my-image.png")
                    .displayName("Property Resource Binary")
                    .hintValue("assets/images/my-image.png")
                    .description("Property Resource Binary Info")
                    .type(TypeDescriptors.typeDescriptorResourceBinary)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyTypeObject =
            PropertyDescriptor.builder()
                    .name("propertyTypeObject")
                    .displayName("Property Type Object")
                    .description("Property Type Object Info")
                    .type(TypeDescriptors.typeObjectDescriptor)
                    .when(WhenDescriptors.whenDescriptorDefault)
                    .scriptSignature(ScriptSignatureDescriptors.scriptSignatureDescriptorDefault)
                    .build();

    public static PropertyDescriptor propertyStringWithInitValue =
            PropertyDescriptor.builder()
                    .name("propertyStringWithInitValue")
                    .initValue(InitValue.USE_DEFAULT_VALUE)
                    .displayName("Property String With Init Value")
                    .hintValue("Test string with default hint")
                    .description("Property String With Default Info")
                    .type(TypeDescriptors.typeDescriptorString)
                    .build();
}
