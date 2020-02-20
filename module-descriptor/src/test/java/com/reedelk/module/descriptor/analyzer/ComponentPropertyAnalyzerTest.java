package com.reedelk.module.descriptor.analyzer;

import com.reedelk.module.descriptor.analyzer.property.ComponentPropertyAnalyzer;
import com.reedelk.module.descriptor.model.*;
import com.reedelk.runtime.api.annotation.InitValue;
import com.reedelk.runtime.api.commons.ImmutableMap;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.resource.DynamicResource;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import com.reedelk.runtime.api.script.dynamicvalue.*;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.FieldInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentPropertyAnalyzerTest {

    private static ComponentPropertyAnalyzer analyzer;
    private static ClassInfo componentClassInfo;

    @BeforeAll
    static void beforeAll() {
        ScannerTestUtils.ScanContext scanContext = ScannerTestUtils.scan(TestComponent.class);
        analyzer = new ComponentPropertyAnalyzer(scanContext.context);
        componentClassInfo = scanContext.targetComponentClassInfo;
    }

    @Test
    void shouldCorrectlyAnalyzeIntegerTypeProperty() {
        // Given
        TypePrimitiveDescriptor typeInteger = ObjectFactories.createTypePrimitiveDescriptor(int.class);

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
        TypePrimitiveDescriptor typeIntegerObject = ObjectFactories.createTypePrimitiveDescriptor(Integer.class);

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
        TypePrimitiveDescriptor typeLong = ObjectFactories.createTypePrimitiveDescriptor(long.class);

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
        TypePrimitiveDescriptor typeLongObject =  ObjectFactories.createTypePrimitiveDescriptor(Long.class);

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
        TypePrimitiveDescriptor typeFloat =  ObjectFactories.createTypePrimitiveDescriptor(float.class);

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
        TypePrimitiveDescriptor typeFloatObject =  ObjectFactories.createTypePrimitiveDescriptor(Float.class);

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
        TypePrimitiveDescriptor typeDouble =  ObjectFactories.createTypePrimitiveDescriptor(double.class);

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
        TypePrimitiveDescriptor typeDoubleObject =  ObjectFactories.createTypePrimitiveDescriptor(Double.class);

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
        TypePrimitiveDescriptor typeBoolean =  ObjectFactories.createTypePrimitiveDescriptor(boolean.class);

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
        TypePrimitiveDescriptor typeBooleanObject =  ObjectFactories.createTypePrimitiveDescriptor(Boolean.class);

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
        TypeEnumDescriptor typeEnum = ObjectFactories.createTypeEnumDescriptor(enumValues);

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
        TypePrimitiveDescriptor typeString =  ObjectFactories.createTypePrimitiveDescriptor(String.class);

        // Expect
        assertThatExistProperty(
                "stringProperty",
                "String property",
                "init string value",
                TypeDescriptorMatchers.ofPrimitiveType(typeString));
    }

    @Test
    void shouldCorrectlyAnalyzeBigIntegerTypeProperty() {
        // Given
        TypePrimitiveDescriptor typeBigInteger =  ObjectFactories.createTypePrimitiveDescriptor(BigInteger.class);

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
        TypePrimitiveDescriptor typeBigDecimal =  ObjectFactories.createTypePrimitiveDescriptor(BigDecimal.class);

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
        TypeResourceTextDescriptor typeResource = new TypeResourceTextDescriptor();

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
        TypeResourceBinaryDescriptor typeResource = new TypeResourceBinaryDescriptor();

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
        TypeDynamicValueDescriptor typeDynamicResource =
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
        TypeComboDescriptor typeCombo = ObjectFactories.createTypeComboDescriptor(true, new String[]{"one", "two", "three"}, "XXXXXXXXXXXX");

        // Expect
        assertThatExistProperty(
                "comboProperty",
                "Combo property",
                "two",
                TypeDescriptorMatchers.ofTypeCombo(typeCombo));
    }

    @Test
    void shouldCorrectlyAnalyzeMapTypeProperty() {
        // Given
        TypeMapDescriptor typeMap = ObjectFactories.createTypeMapDescriptor("Test tab group", TabPlacement.LEFT);

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
        TypeMapDescriptor typeMap = ObjectFactories.createTypeMapDescriptor("Init values tab group", TabPlacement.LEFT);

        // Expect
        assertThatExistProperty(
                "mapPropertyWithInitValues",
                "Map property with init values",
                "{'key1':'value1','key2':'value2'}",
                TypeDescriptorMatchers.ofTypeMap(typeMap));
    }

    @Test
    void shouldCorrectlyAnalyzeMapTypePropertyWithTabPlacementTop() {
        // Given
        TypeMapDescriptor typeMap = ObjectFactories.createTypeMapDescriptor(null, TabPlacement.TOP);

        // Expect
        assertThatExistProperty(
                "mapPropertyWithTabPlacementTop",
                "Map property with tab placement top",
                InitValue.USE_DEFAULT_VALUE,
                TypeDescriptorMatchers.ofTypeMap(typeMap));
    }

    @Test
    void shouldCorrectlyAnalyzeScriptTypeProperty() {
        // Given
        TypeScriptDescriptor typeScript = new TypeScriptDescriptor();

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
        TypeDynamicValueDescriptor typeDynamicBigDecimal =
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
        TypeDynamicValueDescriptor typeDynamicBigInteger =
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
        TypeDynamicValueDescriptor typeDynamicBoolean =
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
        TypeDynamicValueDescriptor typeDynamicByteArray =
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
        TypeDynamicValueDescriptor typeDynamicDouble =
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
        TypeDynamicValueDescriptor typeDynamicFloat =
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
        TypeDynamicValueDescriptor typeDynamicInteger =
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
        TypeDynamicValueDescriptor typeDynamicLong =
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
        TypeDynamicValueDescriptor typeDynamicObject =
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
        TypeDynamicValueDescriptor typeDynamicString =
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
        TypeDynamicMapDescriptor typeDynamicMapDescriptor =
                ObjectFactories.createTypeDynamicMapDescriptor(DynamicStringMap.class, "My dynamic string map");

        // Expect
        assertThatExistProperty(
                "dynamicStringMapProperty",
                "Dynamic string map",
                InitValue.USE_DEFAULT_VALUE,
                TypeDescriptorMatchers.ofDynamicMapType(typeDynamicMapDescriptor));
    }

    // Mime Type Combo

    @Test
    void shouldCorrectlyAnalyzeMimeTypeComboProperty() {
        // Given
        TypeComboDescriptor typeComboDescriptor =
                ObjectFactories.createTypeComboDescriptor(true, MimeType.ALL_MIME_TYPES, MimeType.MIME_TYPE_PROTOTYPE);

        // Expect
        assertThatExistProperty(
                "mimeType",
                "Mime type",
                "*/*",
                TypeDescriptorMatchers.ofTypeCombo(typeComboDescriptor));
    }

    @Test
    void shouldCorrectlyAnalyzeMimeTypeCustomComboProperty() {
        // Given
        List<String> predefinedMimeTypes = new ArrayList<>(Arrays.asList(MimeType.ALL_MIME_TYPES));
        predefinedMimeTypes.add("img/xyz");
        predefinedMimeTypes.add("audio/mp13");
        String[] comboMimeTypesArray = predefinedMimeTypes.toArray(new String[]{});

        // Given
        TypeComboDescriptor typeComboDescriptor =
                ObjectFactories.createTypeComboDescriptor(true, comboMimeTypesArray, MimeType.MIME_TYPE_PROTOTYPE);

        // Expect
        assertThatExistProperty(
                "mimeTypeCustom",
                "Mime type with additional types",
                "img/xyz",
                TypeDescriptorMatchers.ofTypeCombo(typeComboDescriptor));
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
        TypePrimitiveDescriptor typeFloat =  ObjectFactories.createTypePrimitiveDescriptor(float.class);

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
        TypePrimitiveDescriptor typeInteger =  ObjectFactories.createTypePrimitiveDescriptor(int.class);

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
                                         TypeDescriptorMatchers.TypeDescriptorMatcher matcher) {
        assertThatExistProperty(propertyName, displayName, initValue, null, null, matcher);
    }

    private void assertThatExistProperty(String propertyName,
                                         String displayName,
                                         String initValue,
                                         String example,
                                         String defaultValue,
                                         TypeDescriptorMatchers.TypeDescriptorMatcher matcher) {
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
