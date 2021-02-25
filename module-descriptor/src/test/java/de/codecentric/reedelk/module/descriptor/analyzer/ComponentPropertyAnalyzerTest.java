package de.codecentric.reedelk.module.descriptor.analyzer;

import de.codecentric.reedelk.module.descriptor.model.property.*;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.*;
import de.codecentric.reedelk.module.descriptor.analyzer.property.PropertyAnalyzer;
import de.codecentric.reedelk.runtime.api.annotation.InitValue;
import de.codecentric.reedelk.runtime.api.commons.ImmutableMap;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.runtime.api.resource.DynamicResource;
import de.codecentric.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.FieldInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class ComponentPropertyAnalyzerTest {

    private static PropertyAnalyzer analyzer;
    private static ClassInfo componentClassInfo;

    private static final String[] MIME_TYPES_ARRAY = MimeType.ALL.stream().map(MimeType::toString)
            .collect(toList())
            .toArray(new String[]{});

    @BeforeAll
    static void beforeAll() {
        ScannerTestUtils.ScanContext scanContext = ScannerTestUtils.scan(TestComponent.class);
        analyzer = new PropertyAnalyzer(scanContext.context);
        componentClassInfo = scanContext.targetComponentClassInfo;
    }

    @Test
    void shouldCorrectlyAnalyzeIntegerTypeProperty() {
        // Given
        PrimitiveDescriptor typeInteger = ObjectFactories.createTypePrimitiveDescriptor(int.class);

        // Expect
        assertThatExistProperty(
                "integerProperty",
                "Integer property",
                "3",
                TypeDescriptorMatchers.ofPrimitiveType(typeInteger));
    }

    @Test
    void shouldCorrectlyAnalyzeIntegerObjectProperty() {
        // Given
        PrimitiveDescriptor typeIntegerObject = ObjectFactories.createTypePrimitiveDescriptor(Integer.class);

        // Expect
        assertThatExistProperty(
                "integerObjectProperty",
                "Integer object property",
                "4569",
                TypeDescriptorMatchers.ofPrimitiveType(typeIntegerObject));
    }

    @Test
    void shouldCorrectlyAnalyzeLongTypeProperty() {
        // Given
        PrimitiveDescriptor typeLong = ObjectFactories.createTypePrimitiveDescriptor(long.class);

        // Expect
        assertThatExistProperty(
                "longProperty",
                "Long property",
                InitValue.USE_DEFAULT_VALUE,
                TypeDescriptorMatchers.ofPrimitiveType(typeLong));
    }

    @Test
    void shouldCorrectlyAnalyzeLongObjectProperty() {
        // Given
        PrimitiveDescriptor typeLongObject =  ObjectFactories.createTypePrimitiveDescriptor(Long.class);

        // Expect
        assertThatExistProperty(
                "longObjectProperty",
                "Long object property",
                InitValue.USE_DEFAULT_VALUE,
                TypeDescriptorMatchers.ofPrimitiveType(typeLongObject));
    }

    @Test
    void shouldCorrectlyAnalyzeFloatTypeProperty() {
        // Given
        PrimitiveDescriptor typeFloat =  ObjectFactories.createTypePrimitiveDescriptor(float.class);

        // Expect
        assertThatExistProperty(
                "floatProperty",
                "Float property",
                "23.23f",
                TypeDescriptorMatchers.ofPrimitiveType(typeFloat));
    }

    @Test
    void shouldCorrectlyAnalyzeFloatObjectTypeProperty() {
        // Given
        PrimitiveDescriptor typeFloatObject =  ObjectFactories.createTypePrimitiveDescriptor(Float.class);

        // Expect
        assertThatExistProperty(
                "floatObjectProperty",
                "Float object property",
                "13.23f",
                TypeDescriptorMatchers.ofPrimitiveType(typeFloatObject));
    }

    @Test
    void shouldCorrectlyAnalyzeDoubleTypeProperty() {
        // Given
        PrimitiveDescriptor typeDouble =  ObjectFactories.createTypePrimitiveDescriptor(double.class);

        // Expect
        assertThatExistProperty(
                "doubleProperty",
                "Double property",
                "234.5322343d",
                TypeDescriptorMatchers.ofPrimitiveType(typeDouble));
    }

    @Test
    void shouldCorrectlyAnalyzeDoubleObjectTypeProperty() {
        // Given
        PrimitiveDescriptor typeDoubleObject =  ObjectFactories.createTypePrimitiveDescriptor(Double.class);

        // Expect
        assertThatExistProperty(
                "doubleObjectProperty",
                "Double object property",
                "234.12d",
                TypeDescriptorMatchers.ofPrimitiveType(typeDoubleObject));
    }

    @Test
    void shouldCorrectlyAnalyzeBooleanTypeProperty() {
        // Given
        PrimitiveDescriptor typeBoolean =  ObjectFactories.createTypePrimitiveDescriptor(boolean.class);

        // Expect
        assertThatExistProperty(
                "booleanProperty",
                "Boolean property",
                "true",
                TypeDescriptorMatchers.ofPrimitiveType(typeBoolean));
    }

    @Test
    void shouldCorrectlyAnalyzeBooleanObjectTypeProperty() {
        // Given
        PrimitiveDescriptor typeBooleanObject =  ObjectFactories.createTypePrimitiveDescriptor(Boolean.class);

        // Expect
        assertThatExistProperty(
                "booleanObjectProperty",
                "Boolean object property",
                "true",
                TypeDescriptorMatchers.ofPrimitiveType(typeBooleanObject));
    }

    @Test
    void shouldCorrectlyAnalyzeEnumTypeProperty() {
        // Given
        Map<String, String> enumValues =
                ImmutableMap.of("VALUE1", "Value 1", "VALUE2", "VALUE2", "VALUE3", "Value 3");
        EnumDescriptor typeEnum = ObjectFactories.createTypeEnumDescriptor(enumValues);

        // Expect
        assertThatExistProperty(
                "enumProperty",
                "Enum Property",
                "VALUE2",
                TypeDescriptorMatchers.ofTypeEnum(typeEnum));
    }

    @Test
    void shouldCorrectlyAnalyzeStringTypeProperty() {
        // Given
        PrimitiveDescriptor typeString =  ObjectFactories.createTypePrimitiveDescriptor(String.class);

        // Expect
        assertThatExistProperty(
                "stringProperty",
                "String property",
                "init string value",
                TypeDescriptorMatchers.ofPrimitiveType(typeString));
    }

    @Test
    void shouldCorrectlyAnalyzeCharTypeProperty() {
        // Given
        PrimitiveDescriptor typeChar =  ObjectFactories.createTypePrimitiveDescriptor(char.class);

        // Expect
        assertThatExistProperty(
                "charProperty",
                "Char property",
                "c",
                TypeDescriptorMatchers.ofPrimitiveType(typeChar));
    }

    @Test
    void shouldCorrectlyAnalyzeCharObjectTypeProperty() {
        // Given
        PrimitiveDescriptor typeChar =  ObjectFactories.createTypePrimitiveDescriptor(Character.class);

        // Expect
        assertThatExistProperty(
                "charObjectProperty",
                "Char object property",
                "b",
                TypeDescriptorMatchers.ofPrimitiveType(typeChar));
    }

    @Test
    void shouldCorrectlyAnalyzeBigIntegerTypeProperty() {
        // Given
        PrimitiveDescriptor typeBigInteger =  ObjectFactories.createTypePrimitiveDescriptor(BigInteger.class);

        // Expect
        assertThatExistProperty(
                "bigIntegerProperty",
                "Big Integer property",
                "6723823",
                TypeDescriptorMatchers.ofPrimitiveType(typeBigInteger));
    }

    @Test
    void shouldCorrectlyAnalyzeBigDecimalTypeProperty() {
        // Given
        PrimitiveDescriptor typeBigDecimal =  ObjectFactories.createTypePrimitiveDescriptor(BigDecimal.class);

        // Expect
        assertThatExistProperty(
                "bigDecimalProperty",
                "Big Decimal property",
                "342.14823",
                TypeDescriptorMatchers.ofPrimitiveType(typeBigDecimal));
    }

    @Test
    void shouldCorrectlyAnalyzeResourceTextTypeProperty() {
        // Given
        ResourceTextDescriptor typeResource = new ResourceTextDescriptor();

        // Expect
        assertThatExistProperty(
                "resourceTextProperty",
                "Resource text property",
                InitValue.USE_DEFAULT_VALUE,
                TypeDescriptorMatchers.ofTypeResourceText(typeResource));
    }

    @Test
    void shouldCorrectlyAnalyzeResourceBinaryTypeProperty() {
        // Given
        ResourceBinaryDescriptor typeResource = new ResourceBinaryDescriptor();

        // Expect
        assertThatExistProperty(
                "resourceBinaryProperty",
                "Resource binary property",
                InitValue.USE_DEFAULT_VALUE,
                TypeDescriptorMatchers.ofTypeResourceBinary(typeResource));
    }

    @Test
    void shouldCorrectlyAnalyzeResourceDynamicTypeProperty() {
        // Given
        DynamicValueDescriptor typeDynamicResource =
                ObjectFactories.createTypeDynamicValueDescriptor(DynamicResource.class);

        // Expect
        assertThatExistProperty(
                "resourceDynamicProperty",
                "Resource dynamic property",
                InitValue.USE_DEFAULT_VALUE,
                TypeDescriptorMatchers.ofDynamicType(typeDynamicResource));
    }

    @Test
    void shouldCorrectlyAnalyzeComboTypeProperty() {
        // Given
        ComboDescriptor typeCombo = ObjectFactories.createTypeComboDescriptor(true, new String[]{"one", "two", "three"}, "XXXXXXXXXXXX");

        // Expect
        assertThatExistProperty(
                "comboProperty",
                "Combo property",
                "two",
                TypeDescriptorMatchers.ofTypeCombo(typeCombo));
    }

    // Map

    @Test
    void shouldCorrectlyAnalyzeMapTypeProperty() {
        // Given
        MapDescriptor typeMap = ObjectFactories.createTypeMapDescriptor("Test tab group");

        // Expect
        assertThatExistProperty(
                "mapProperty",
                "Map property",
                InitValue.USE_DEFAULT_VALUE,
                TypeDescriptorMatchers.ofTypeMap(typeMap));
    }

    @Test
    void shouldCorrectlyAnalyzeMapTypePropertyWithInitValues() {
        // Given
        MapDescriptor typeMap = ObjectFactories.createTypeMapDescriptor("Init values tab group");

        // Expect
        assertThatExistProperty(
                "mapPropertyWithInitValues",
                "Map property with init values",
                "{'key1':'value1','key2':'value2'}",
                TypeDescriptorMatchers.ofTypeMap(typeMap));
    }

    // List

    @Test
    void shouldCorrectlyAnalyzeListTypeProperty() {
        // Given
        ListDescriptor typeList = ObjectFactories.createTypeListDescriptor(String.class);

        // Expect
        assertThatExistProperty(
                "listProperty",
                "List property",
                InitValue.USE_DEFAULT_VALUE,
                TypeDescriptorMatchers.ofTypeList(typeList));
    }

    @Test
    void shouldCorrectlyAnalyzeListTypePropertyWithInitValues() {
        // Given
        ListDescriptor typeList = ObjectFactories.createTypeListDescriptor(String.class);

        // Expect
        assertThatExistProperty(
                "listPropertyWithInitValues",
                "List property with init values",
                "['one','two','three']",
                TypeDescriptorMatchers.ofTypeList(typeList));
    }

    @Test
    void shouldCorrectlyAnalyzeScriptTypeProperty() {
        // Given
        ScriptDescriptor typeScript = new ScriptDescriptor();

        // Expect
        assertThatExistProperty(
                "scriptProperty",
                "Script property",
                InitValue.USE_DEFAULT_VALUE,
                TypeDescriptorMatchers.ofTypeScript(typeScript));
    }

    // Dynamic value types

    @Test
    void shouldCorrectlyAnalyzeDynamicBigDecimalProperty() {
        // Given
        DynamicValueDescriptor typeDynamicBigDecimal =
                ObjectFactories.createTypeDynamicValueDescriptor(DynamicBigDecimal.class);

        // Expect
        assertThatExistProperty(
                "dynamicBigDecimalProperty",
                "Dynamic big decimal",
                "#[56789.234]",
                TypeDescriptorMatchers.ofDynamicType(typeDynamicBigDecimal));
    }

    @Test
    void shouldCorrectlyAnalyzeDynamicBigIntegerProperty() {
        // Given
        DynamicValueDescriptor typeDynamicBigInteger =
                ObjectFactories.createTypeDynamicValueDescriptor(DynamicBigInteger.class);

        // Expect
        assertThatExistProperty(
                "dynamicBigIntegerProperty",
                "Dynamic big integer",
                "56789",
                TypeDescriptorMatchers.ofDynamicType(typeDynamicBigInteger));
    }

    @Test
    void shouldCorrectlyAnalyzeDynamicBooleanProperty() {
        // Given
        DynamicValueDescriptor typeDynamicBoolean =
                ObjectFactories.createTypeDynamicValueDescriptor(DynamicBoolean.class);

        // Expect
        assertThatExistProperty(
                "dynamicBooleanProperty",
                "Dynamic boolean",
                "true",
                TypeDescriptorMatchers.ofDynamicType(typeDynamicBoolean));
    }

    @Test
    void shouldCorrectlyAnalyzeDynamicByteArrayProperty() {
        // Given
        DynamicValueDescriptor typeDynamicByteArray =
                ObjectFactories.createTypeDynamicValueDescriptor(DynamicByteArray.class);

        // Expect
        assertThatExistProperty(
                "dynamicByteArrayProperty",
                "Dynamic byte array",
                "#[message.payload()]",
                TypeDescriptorMatchers.ofDynamicType(typeDynamicByteArray));
    }

    @Test
    void shouldCorrectlyAnalyzeDynamicDoubleProperty() {
        // Given
        DynamicValueDescriptor typeDynamicDouble =
                ObjectFactories.createTypeDynamicValueDescriptor(DynamicDouble.class);

        // Expect
        assertThatExistProperty(
                "dynamicDoubleProperty",
                "Dynamic double",
                "234.23d",
                TypeDescriptorMatchers.ofDynamicType(typeDynamicDouble));
    }

    @Test
    void shouldCorrectlyAnalyzeDynamicFloatProperty() {
        // Given
        DynamicValueDescriptor typeDynamicFloat =
                ObjectFactories.createTypeDynamicValueDescriptor(DynamicFloat.class);

        // Expect
        assertThatExistProperty(
                "dynamicFloatProperty",
                "Dynamic float",
                "#[message.attributes['id']]",
                TypeDescriptorMatchers.ofDynamicType(typeDynamicFloat));
    }

    @Test
    void shouldCorrectlyAnalyzeDynamicIntegerProperty() {
        // Given
        DynamicValueDescriptor typeDynamicInteger =
                ObjectFactories.createTypeDynamicValueDescriptor(DynamicInteger.class);

        // Expect
        assertThatExistProperty(
                "dynamicIntegerProperty",
                "Dynamic integer",
                "1233",
                TypeDescriptorMatchers.ofDynamicType(typeDynamicInteger));
    }

    @Test
    void shouldCorrectlyAnalyzeDynamicLongProperty() {
        // Given
        DynamicValueDescriptor typeDynamicLong =
                ObjectFactories.createTypeDynamicValueDescriptor(DynamicLong.class);

        // Expect
        assertThatExistProperty(
                "dynamicLongProperty",
                "Dynamic long",
                "658291",
                TypeDescriptorMatchers.ofDynamicType(typeDynamicLong));
    }

    @Test
    void shouldCorrectlyAnalyzeDynamicObjectProperty() {
        // Given
        DynamicValueDescriptor typeDynamicObject =
                ObjectFactories.createTypeDynamicValueDescriptor(DynamicObject.class);

        // Expect
        assertThatExistProperty(
                "dynamicObjectProperty",
                "Dynamic object",
                "my object text",
                TypeDescriptorMatchers.ofDynamicType(typeDynamicObject));
    }

    @Test
    void shouldCorrectlyAnalyzeDynamicStringProperty() {
        // Given
        DynamicValueDescriptor typeDynamicString =
                ObjectFactories.createTypeDynamicValueDescriptor(DynamicString.class);

        // Expect
        assertThatExistProperty(
                "dynamicStringProperty",
                "Dynamic string",
                InitValue.USE_DEFAULT_VALUE,
                TypeDescriptorMatchers.ofDynamicType(typeDynamicString));
    }

    // Dynamic map types

    @Test
    void shouldCorrectlyAnalyzeDynamicStringMapProperty() {
        // Given
        DynamicMapDescriptor propertyDynamicMapDescriptor =
                ObjectFactories.createTypeDynamicMapDescriptor(DynamicStringMap.class, "My dynamic string map");

        // Expect
        assertThatExistProperty(
                "dynamicStringMapProperty",
                "Dynamic string map",
                InitValue.USE_DEFAULT_VALUE,
                TypeDescriptorMatchers.ofDynamicMapType(propertyDynamicMapDescriptor));
    }

    // Mime Type Combo

    @Test
    void shouldCorrectlyAnalyzeMimeTypeComboProperty() {
        // Given
        ComboDescriptor comboDescriptor =
                ObjectFactories.createTypeComboDescriptor(true, MIME_TYPES_ARRAY, MimeType.MIME_TYPE_PROTOTYPE);

        // Expect
        assertThatExistProperty(
                "mimeType",
                "Mime type",
                "*/*",
                TypeDescriptorMatchers.ofTypeCombo(comboDescriptor));
    }

    @Test
    void shouldCorrectlyAnalyzeMimeTypeCustomComboProperty() {
        // Given
        List<String> predefinedMimeTypes = new ArrayList<>(Arrays.asList(MIME_TYPES_ARRAY));
        predefinedMimeTypes.add("img/xyz");
        predefinedMimeTypes.add("audio/mp13");
        String[] comboMimeTypesArray = predefinedMimeTypes.toArray(new String[]{});

        // Given
        ComboDescriptor comboDescriptor =
                ObjectFactories.createTypeComboDescriptor(true, comboMimeTypesArray, MimeType.MIME_TYPE_PROTOTYPE);

        // Expect
        assertThatExistProperty(
                "mimeTypeCustom",
                "Mime type with additional types",
                "img/xyz",
                TypeDescriptorMatchers.ofTypeCombo(comboDescriptor));
    }

    // Special cases

    @Test
    void shouldCorrectlyReturnEmptyOptionalForNotExposedProperty() {
        // Given
        FieldInfo notExposedProperty =
                componentClassInfo.getFieldInfo("notExposedProperty");

        // When
        Optional<PropertyDescriptor> propertyDescriptor = analyzer.analyze(notExposedProperty);

        // Then
        assertThat(propertyDescriptor).isNotPresent();
    }

    @Test
    void shouldReturnFieldNameWhenPropertyAnnotationDoesNotContainPropertyDisplayName() {
        // Given
        PrimitiveDescriptor typeFloat =  ObjectFactories.createTypePrimitiveDescriptor(float.class);

        // Expect
        assertThatExistProperty(
                "withoutDisplayNameProperty",
                "withoutDisplayNameProperty",
                InitValue.USE_DEFAULT_VALUE,
                TypeDescriptorMatchers.ofPrimitiveType(typeFloat));
    }

    @Test
    void shouldReturnDefaultIntValueWhenDefaultAnnotationDoesNotSpecifyAnyValue() {
        // Given
        PrimitiveDescriptor typeInteger =  ObjectFactories.createTypePrimitiveDescriptor(int.class);

        // Expect
        assertThatExistProperty(
                "missingInitValueProperty",
                "Property with missing init value",
                InitValue.USE_DEFAULT_VALUE,
                TypeDescriptorMatchers.ofPrimitiveType(typeInteger));
    }

    @Test
    void shouldReturnCorrectPropertyDescription() {
        // Given
        FieldInfo property = componentClassInfo.getFieldInfo("stringPropertyWithDescription");

        // When
        Optional<PropertyDescriptor> optionalDescriptor = analyzer.analyze(property);

        // Then
        assertThat(optionalDescriptor).isPresent();
        PropertyDescriptor descriptor = optionalDescriptor.get();
        assertThat(descriptor.getName()).isEqualTo("stringPropertyWithDescription");
        assertThat(descriptor.getDisplayName()).isEqualTo("String property with description text");
        assertThat(descriptor.getDescription()).isEqualTo("This is the description text");
    }

    @Test
    void shouldReturnCorrectExample() {
        // Given
        FieldInfo property = componentClassInfo.getFieldInfo("stringPropertyWithExample");

        // When
        Optional<PropertyDescriptor> optionalDescriptor = analyzer.analyze(property);

        // Then
        assertThat(optionalDescriptor).isPresent();
        PropertyDescriptor descriptor = optionalDescriptor.get();
        assertThat(descriptor.getName()).isEqualTo("stringPropertyWithExample");
        assertThat(descriptor.getDisplayName()).isEqualTo("Property with example");
        assertThat(descriptor.getExample()).isEqualTo("A string example");
    }

    @Test
    void shouldReturnCorrectDefaultValue() {
        // Given
        FieldInfo property = componentClassInfo.getFieldInfo("stringPropertyWithDefaultValue");

        // When
        Optional<PropertyDescriptor> optionalDescriptor = analyzer.analyze(property);

        // Then
        assertThat(optionalDescriptor).isPresent();
        PropertyDescriptor descriptor = optionalDescriptor.get();
        assertThat(descriptor.getName()).isEqualTo("stringPropertyWithDefaultValue");
        assertThat(descriptor.getDisplayName()).isEqualTo("Property with default value");
        assertThat(descriptor.getDefaultValue()).isEqualTo("My default value");
    }

    private void assertThatExistProperty(String propertyName,
                                         String displayName,
                                         String initValue,
                                         Matcher<PropertyTypeDescriptor> matcher) {
        assertThatExistProperty(propertyName, displayName, initValue, null, null, matcher);
    }

    private void assertThatExistProperty(String propertyName,
                                         String displayName,
                                         String initValue,
                                         String example,
                                         String defaultValue,
                                         Matcher<PropertyTypeDescriptor> matcher) {
        // Given
        FieldInfo property = componentClassInfo.getFieldInfo(propertyName);

        // When
        Optional<PropertyDescriptor> optionalDescriptor = analyzer.analyze(property);

        // Then
        assertThat(optionalDescriptor).isPresent();
        PropertyDescriptor descriptor = optionalDescriptor.get();
        assertThat(descriptor.getName()).isEqualTo(propertyName);
        assertThat(descriptor.getDisplayName()).isEqualTo(displayName);
        assertThat(descriptor.getInitValue()).isEqualTo(initValue);
        assertThat(descriptor.getExample()).isEqualTo(example);
        assertThat(descriptor.getDefaultValue()).isEqualTo(defaultValue);
        assertThat(matcher.matches(descriptor.getType())).isTrue();
    }
}
