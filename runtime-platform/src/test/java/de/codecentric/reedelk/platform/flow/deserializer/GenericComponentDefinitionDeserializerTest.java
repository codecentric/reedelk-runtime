package de.codecentric.reedelk.platform.flow.deserializer;

import de.codecentric.reedelk.platform.test.utils.*;
import de.codecentric.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class GenericComponentDefinitionDeserializerTest extends AbstractGenericComponentDefinitionDeserializerTest {

    @Nested
    @DisplayName("Primitive type")
    class PrimitiveTypeTests {

        @Test
        void shouldCorrectlySetStringProperty() {
            // Given
            String expectedValue = "Hello";
            TestComponent component = buildComponentWith("stringProperty", expectedValue);

            // Then
            assertThat(component.getStringProperty()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetNullStringProperty() {
            // Given
            TestComponent component = buildComponentWith("stringProperty", null);

            // Then
            assertThat(component.getStringProperty()).isNull();
        }

        @Test
        void shouldCorrectlySetCharProperty() {
            // Given
            char expectedValue = 'c';
            TestComponent component = buildComponentWith("charProperty", String.valueOf(expectedValue));

            // Then
            assertThat(component.getCharProperty()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetCharObjectProperty() {
            // Given
            char expectedValue = 'c';
            TestComponent component = buildComponentWith("charObjectProperty", String.valueOf(expectedValue));

            // Then
            assertThat(component.getCharObjectProperty()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetDefaultCharObjectProperty() {
            // Given
            TestComponent component = buildComponentWith("charObjectProperty", null);

            // Then
            assertThat(component.getCharObjectProperty()).isNull();
        }

        @Test
        void shouldCorrectlySetDefaultCharProperty() {
            // Given
            TestComponent component = buildComponentWith("charProperty", null);

            // Then
            assertThat(component.getCharProperty()).isEqualTo('\u0000');
        }

        @Test
        void shouldCorrectlySetLongProperty() {
            // Given
            Object expectedValue = 234;
            TestComponent component = buildComponentWith("longProperty", expectedValue);

            // Then
            assertThat(component.getLongProperty()).isEqualTo(Long.valueOf(234));
        }

        @Test
        void shouldCorrectlySetDefaultWhenNullLongProperty() {
            // Given
            long value = 0L;
            TestComponent component = buildComponentWith("longProperty", null);

            // Then
            assertThat(component.getLongProperty()).isEqualTo(value);
        }

        @Test
        void shouldCorrectlySetLongObjectProperty() {
            // Given
            Object expectedValue = 5432;
            TestComponent component = buildComponentWith("longObjectProperty", expectedValue);

            // Then
            assertThat(component.getLongObjectProperty()).isEqualTo(Long.valueOf(5432));
        }

        @Test
        void shouldCorrectlySetNullLongObjectProperty() {
            // Given
            TestComponent component = buildComponentWith("longObjectProperty", null);

            // Then
            assertThat(component.getLongObjectProperty()).isNull();
        }

        @Test
        void shouldCorrectlySetIntProperty() {
            // Given
            Object expectedValue = 20;
            TestComponent component = buildComponentWith("intProperty", expectedValue);

            // Then
            assertThat(component.getIntProperty()).isEqualTo(Integer.valueOf(20));
        }

        @Test
        void shouldCorrectlySetDefaultWhenNullIntProperty() {
            // Given
            int value = 0;
            TestComponent component = buildComponentWith("intProperty", null);

            // Then
            assertThat(component.getIntProperty()).isEqualTo(value);
        }

        @Test
        void shouldCorrectlySetIntObjectProperty() {
            // Given
            Object expectedValue = 20;
            TestComponent component = buildComponentWith("intObjectProperty", expectedValue);

            // Then
            assertThat(component.getIntObjectProperty()).isEqualTo(Integer.valueOf(20));
        }

        @Test
        void shouldCorrectlySetNullIntObjectProperty() {
            // Given
            TestComponent component = buildComponentWith("intObjectProperty", null);

            // Then
            assertThat(component.getIntObjectProperty()).isNull();
        }

        @Test
        void shouldCorrectlySetDoubleProperty() {
            // Given
            Object expectedValue = 234.3;
            TestComponent component = buildComponentWith("doubleProperty", expectedValue);

            // Then
            assertThat(component.getDoubleProperty()).isEqualTo(Double.valueOf(234.3d));
        }

        @Test
        void shouldCorrectlySetDefaultWhenNullDoubleProperty() {
            // Given
            TestComponent component = buildComponentWith("doubleProperty", null);

            // Then
            assertThat(component.getDoubleProperty()).isEqualTo(Double.valueOf(0.0d));
        }

        @Test
        void shouldCorrectlySetDoubleObjectProperty() {
            // Given
            Object expectedValue = 111.432;
            TestComponent component = buildComponentWith("doubleObjectProperty", expectedValue);

            // Then
            assertThat(component.getDoubleObjectProperty()).isEqualTo(Double.valueOf(111.432));
        }

        @Test
        void shouldCorrectlySetNullDoubleObjectProperty() {
            // Given
            TestComponent component = buildComponentWith("doubleObjectProperty", null);

            // Then
            assertThat(component.getDoubleObjectProperty()).isNull();
        }

        @Test
        void shouldCorrectlySetFloatProperty() {
            // Given
            Object expectedValue = 234.2f;
            TestComponent component = buildComponentWith("floatProperty", expectedValue);

            // Then
            assertThat(component.getFloatProperty()).isEqualTo(Float.valueOf(234.2f));
        }

        @Test
        void shouldCorrectlySetDefaultWhenNullFloatProperty() {
            // Given
            TestComponent component = buildComponentWith("floatProperty", null);

            // Then
            assertThat(component.getFloatProperty()).isEqualTo(Float.valueOf(0.0f));
        }

        @Test
        void shouldCorrectlySetFloatObjectProperty() {
            // Given
            Object expectedValue = Float.MAX_VALUE;
            TestComponent component = buildComponentWith("floatObjectProperty", expectedValue);

            // Then
            assertThat(component.getFloatObjectProperty()).isEqualTo(Float.valueOf(Float.MAX_VALUE));
        }

        @Test
        void shouldCorrectlySetNullFloatObjectProperty() {
            // Given
            TestComponent component = buildComponentWith("floatObjectProperty", null);

            // Then
            assertThat(component.getFloatObjectProperty()).isNull();
        }

        @Test
        void shouldCorrectlySetBooleanProperty() {
            // Given
            Object expectedValue = true;
            TestComponent component = buildComponentWith("booleanProperty", expectedValue);

            // Then
            assertThat(component.isBooleanProperty()).isTrue();
        }

        @Test
        void shouldCorrectlySetDefaultWhenNullBooleanProperty() {
            // Given
            TestComponent component = buildComponentWith("booleanProperty", null);

            // Then
            assertThat(component.isBooleanProperty()).isFalse();
        }

        @Test
        void shouldCorrectlySetBooleanObjectProperty() {
            // Given
            Object expectedValue = true;
            TestComponent component = buildComponentWith("booleanObjectProperty", expectedValue);

            // Then
            assertThat(component.getBooleanObjectProperty()).isTrue();
        }

        @Test
        void shouldCorrectlySetNullBooleanObjectProperty() {
            // Given
            TestComponent component = buildComponentWith("booleanObjectProperty", null);

            // Then
            assertThat(component.getBooleanObjectProperty()).isNull();
        }

        @Test
        void shouldCorrectlySetBigDecimalProperty() {
            // Given
            Object expectedValue = 2343;
            TestComponent component = buildComponentWith("bigDecimalProperty", expectedValue);

            // Then
            assertThat(component.getBigDecimalProperty()).isEqualTo(new BigDecimal("2343"));
        }

        @Test
        void shouldCorrectlySetNullBigDecimalProperty() {
            // Given
            TestComponent component = buildComponentWith("bigDecimalProperty", null);

            // Then
            assertThat(component.getBigDecimalProperty()).isNull();
        }

        @Test
        void shouldCorrectlySetBigIntegerProperty() {
            // Given
            Object expectedValue = Integer.MAX_VALUE;
            TestComponent component = buildComponentWith("bigIntegerProperty", expectedValue);

            // Then
            assertThat(component.getBigIntegerProperty()).isEqualTo(new BigInteger(String.valueOf(Integer.MAX_VALUE)));
        }

        @Test
        void shouldCorrectlySetNullBigIntegerProperty() {
            // Given
            TestComponent component = buildComponentWith("bigIntegerProperty", null);

            // Then
            assertThat(component.getBigIntegerProperty()).isNull();
        }
    }

    @Nested
    @DisplayName("Collection/s properties")
    class CollectionTests {

        @Test
        void shouldCorrectlySetLongCollection() {
            // Given
            JSONArray array = newArray(21233, Long.MAX_VALUE, Long.MIN_VALUE);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myLongCollection", array);

            // Then
            Collection<Long> collection = component.getMyLongCollection();
            assertAllItemsOfType(collection, Long.class);
            assertThat(collection).containsExactlyInAnyOrder(21233L, Long.MAX_VALUE, Long.MIN_VALUE);
        }

        @Test
        void shouldCorrectlySetIntCollection() {
            // Given
            JSONArray array = newArray(23, Integer.MAX_VALUE, Integer.MIN_VALUE);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myIntCollection", array);

            // Then
            Collection<Integer> collection = component.getMyIntCollection();
            assertAllItemsOfType(collection, Integer.class);
            assertThat(collection).containsExactlyInAnyOrder(23, Integer.MAX_VALUE, Integer.MIN_VALUE);
        }

        @Test
        void shouldCorrectlySetFloatCollection() {
            // Given
            JSONArray array = newArray(23.1f, Float.MAX_VALUE, 234.15f);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myFloatCollection", array);

            // Then
            Collection<Float> collection = component.getMyFloatCollection();
            assertAllItemsOfType(collection, Float.class);
            assertThat(collection).containsExactlyInAnyOrder(23.1f, Float.MAX_VALUE, 234.15f);
        }

        @Test
        void shouldCorrectlySetDoubleCollection() {
            // Given
            JSONArray array = newArray(234.234d, Double.MIN_VALUE, 1.234d);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myDoubleCollection", array);

            // Then
            Collection<Double> collection = component.getMyDoubleCollection();
            assertAllItemsOfType(collection, Double.class);
            assertThat(collection).containsExactlyInAnyOrder(234.234d, Double.MIN_VALUE, 1.234d);
        }

        @Test
        void shouldCorrectlySetStringCollection() {
            // Given
            JSONArray array = newArray("Item1", "Item2", "Item3");
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myStringCollection", array);

            // Then
            Collection<String> collection = component.getMyStringCollection();
            assertAllItemsOfType(collection, String.class);
            assertThat(collection).containsExactlyInAnyOrder("Item1", "Item2", "Item3");
        }


        @Test
        void shouldCorrectlySetBooleanCollection() {
            // Given
            JSONArray array = newArray(true, false, true);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myBooleanCollection", array);

            // Then
            Collection<Boolean> collection = component.getMyBooleanCollection();
            assertAllItemsOfType(collection, Boolean.class);
            assertThat(collection).containsExactlyInAnyOrder(true, false, true);
        }

        @Test
        void shouldCorrectlySetBigIntegerCollection() {
            // Given
            JSONArray array = newArray(243234324, 77465);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myBigIntegerCollection", array);

            // Then
            Collection<BigInteger> collection = component.getMyBigIntegerCollection();
            assertAllItemsOfType(collection, BigInteger.class);
            assertThat(collection).containsExactlyInAnyOrder(new BigInteger("243234324"), new BigInteger("77465"));
        }

        @Test
        void shouldCorrectlySetBigDecimalCollection() {
            // Given
            JSONArray array = newArray(99988, 777788);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myBigDecimalCollection", array);

            // Then
            Collection<BigDecimal> collection = component.getMyBigDecimalCollection();
            assertAllItemsOfType(collection, BigDecimal.class);
            assertThat(collection).containsExactlyInAnyOrder(new BigDecimal("99988"), new BigDecimal("777788"));
        }
    }

    @Nested
    @DisplayName("List/s properties")
    class ListTests {

        @Test
        void shouldCorrectlySetLongList() {
            // Given
            JSONArray array = newArray(21233, Long.MAX_VALUE, Long.MIN_VALUE);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myLongList", array);

            // Then
            List<Long> collection = component.getMyLongList();
            assertAllItemsOfType(collection, Long.class);
            assertThat(collection).containsExactlyInAnyOrder(21233L, Long.MAX_VALUE, Long.MIN_VALUE);
        }

        @Test
        void shouldCorrectlySetIntList() {
            // Given
            JSONArray array = newArray(23, Integer.MAX_VALUE, Integer.MIN_VALUE);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myIntList", array);

            // Then
            List<Integer> collection = component.getMyIntList();
            assertAllItemsOfType(collection, Integer.class);
            assertThat(collection).containsExactlyInAnyOrder(23, Integer.MAX_VALUE, Integer.MIN_VALUE);
        }

        @Test
        void shouldCorrectlySetFloatList() {
            // Given
            JSONArray array = newArray(23.1f, Float.MAX_VALUE, 234.15f);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myFloatList", array);

            // Then
            List<Float> collection = component.getMyFloatList();
            assertAllItemsOfType(collection, Float.class);
            assertThat(collection).containsExactlyInAnyOrder(23.1f, Float.MAX_VALUE, 234.15f);
        }

        @Test
        void shouldCorrectlySetDoubleList() {
            // Given
            JSONArray array = newArray(234.234d, Double.MIN_VALUE, 1.234d);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myDoubleList", array);

            // Then
            List<Double> collection = component.getMyDoubleList();
            assertAllItemsOfType(collection, Double.class);
            assertThat(collection).containsExactlyInAnyOrder(234.234d, Double.MIN_VALUE, 1.234d);
        }

        @Test
        void shouldCorrectlySetStringList() {
            // Given
            JSONArray array = newArray("Item1", "Item2", "Item3");
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myStringList", array);

            // Then
            List<String> collection = component.getMyStringList();
            assertAllItemsOfType(collection, String.class);
            assertThat(collection).containsExactlyInAnyOrder("Item1", "Item2", "Item3");
        }


        @Test
        void shouldCorrectlySetBooleanList() {
            // Given
            JSONArray array = newArray(true, false, true);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myBooleanList", array);

            // Then
            List<Boolean> collection = component.getMyBooleanList();
            assertAllItemsOfType(collection, Boolean.class);
            assertThat(collection).containsExactlyInAnyOrder(true, false, true);
        }

        @Test
        void shouldCorrectlySetBigIntegerList() {
            // Given
            JSONArray array = newArray(243234324, 77465);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myBigIntegerList", array);

            // Then
            List<BigInteger> collection = component.getMyBigIntegerList();
            assertAllItemsOfType(collection, BigInteger.class);
            assertThat(collection).containsExactlyInAnyOrder(new BigInteger("243234324"), new BigInteger("77465"));
        }

        @Test
        void shouldCorrectlySetBigDecimalList() {
            // Given
            JSONArray array = newArray(99988, 777788);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myBigDecimalList", array);

            // Then
            List<BigDecimal> collection = component.getMyBigDecimalList();
            assertAllItemsOfType(collection, BigDecimal.class);
            assertThat(collection).containsExactlyInAnyOrder(new BigDecimal("99988"), new BigDecimal("777788"));
        }

    }

    @Nested
    @DisplayName("Set/s properties")
    class SetTests {

        @Test
        void shouldCorrectlySetLongSet() {
            // Given
            JSONArray array = newArray(21233, Long.MAX_VALUE, Long.MIN_VALUE);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myLongSet", array);

            // Then
            Set<Long> collection = component.getMyLongSet();
            assertAllItemsOfType(collection, Long.class);
            assertThat(collection).containsExactlyInAnyOrder(21233L, Long.MAX_VALUE, Long.MIN_VALUE);
        }

        @Test
        void shouldCorrectlySetIntSet() {
            // Given
            JSONArray array = newArray(23, Integer.MAX_VALUE, Integer.MIN_VALUE);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myIntSet", array);

            // Then
            Set<Integer> collection = component.getMyIntSet();
            assertAllItemsOfType(collection, Integer.class);
            assertThat(collection).containsExactlyInAnyOrder(23, Integer.MAX_VALUE, Integer.MIN_VALUE);
        }

        @Test
        void shouldCorrectlySetFloatSet() {
            // Given
            JSONArray array = newArray(23.1f, Float.MAX_VALUE, 234.15f);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myFloatSet", array);

            // Then
            Set<Float> collection = component.getMyFloatSet();
            assertAllItemsOfType(collection, Float.class);
            assertThat(collection).containsExactlyInAnyOrder(23.1f, Float.MAX_VALUE, 234.15f);
        }

        @Test
        void shouldCorrectlySetDoubleSet() {
            // Given
            JSONArray array = newArray(234.234d, Double.MIN_VALUE, 1.234d, Double.MIN_VALUE);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myDoubleSet", array);

            // Then
            Set<Double> collection = component.getMyDoubleSet();
            assertAllItemsOfType(collection, Double.class);
            assertThat(collection).containsExactlyInAnyOrder(234.234d, Double.MIN_VALUE, 1.234d);
        }

        @Test
        void shouldCorrectlySetStringSet() {
            // Given
            JSONArray array = newArray("Item1", "Item2", "Item3", "Item3");
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myStringSet", array);

            // Then
            Set<String> collection = component.getMyStringSet();
            assertAllItemsOfType(collection, String.class);
            assertThat(collection).containsExactlyInAnyOrder("Item1", "Item2", "Item3");
        }


        @Test
        void shouldCorrectlySetBooleanSet() {
            // Given
            JSONArray array = newArray(true, false, true);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myBooleanSet", array);

            // Then
            Set<Boolean> collection = component.getMyBooleanSet();
            assertAllItemsOfType(collection, Boolean.class);
            assertThat(collection).containsExactlyInAnyOrder(true, false);
        }

        @Test
        void shouldCorrectlySetBigIntegerSet() {
            // Given
            JSONArray array = newArray(243234324, 77465);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myBigIntegerSet", array);

            // Then
            Set<BigInteger> collection = component.getMyBigIntegerSet();
            assertAllItemsOfType(collection, BigInteger.class);
            assertThat(collection).containsExactlyInAnyOrder(new BigInteger("243234324"), new BigInteger("77465"));
        }

        @Test
        void shouldCorrectlySetBigDecimalSet() {
            // Given
            JSONArray array = newArray(99988, 777788);
            TestComponentWithCollectionProperties component =
                    buildCollectionComponentWith("myBigDecimalSet", array);

            // Then
            Set<BigDecimal> collection = component.getMyBigDecimalSet();
            assertAllItemsOfType(collection, BigDecimal.class);
            assertThat(collection).containsExactlyInAnyOrder(new BigDecimal("99988"), new BigDecimal("777788"));
        }

    }

    @Nested
    @DisplayName("Objects tests")
    class ObjectTest {

        @Test
        void shouldCorrectlyMapJSONObjectToImplementor() {
            // Given
            JSONObject testImplementor = ComponentsBuilder.forComponent(TestImplementor.class)
                    .with("property1", "Test")
                    .with("property2", 23432434)
                    .build();

            JSONObject componentDefinition = ComponentsBuilder.forComponent(TestComponentWithObjectProperty.class)
                    .with("config", testImplementor)
                    .build();

            mockComponent(TestComponentWithObjectProperty.class);
            mockImplementor(TestImplementor.class);

            // When
            deserializer.deserialize(componentDefinition, mockExecutionNode.getComponent());

            // Then
            TestComponentWithObjectProperty component = (TestComponentWithObjectProperty) mockExecutionNode.getComponent();
            TestImplementor config = component.getConfig();
            assertThat(config).isNotNull();
            assertThat(config.getProperty1()).isEqualTo("Test");
            assertThat(config.getProperty2()).isEqualTo(new BigDecimal("23432434"));
        }

    }

    @Nested
    @DisplayName("Dynamic value tests")
    class DynamicValueTests {

        @Test
        void shouldCorrectlySetDynamicLongProperty() {
            // Given
            long expectedValue = 843;

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicLongProperty", expectedValue);

            // Then
            DynamicLong property = component.getDynamicLongProperty();
            assertThat(property.isScript()).isFalse();
            assertThat(property.value()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetScriptedDynamicLongProperty() {
            // Given
            String expectedValue = "#[432]";

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicLongProperty", expectedValue);

            // Then
            DynamicLong property = component.getDynamicLongProperty();
            assertThat(property.isScript()).isTrue();
            assertThat(property.body()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetDynamicFloatProperty() {
            // Given
            float expectedValue= 43.32f;

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicFloatProperty", expectedValue);

            // Then
            DynamicFloat property = component.getDynamicFloatProperty();
            assertThat(property.isScript()).isFalse();
            assertThat(property.value()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetScriptedDynamicFloatProperty() {
            // Given
            String expectedValue = "#[43.2]";

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicFloatProperty", expectedValue);

            // Then
            DynamicFloat property = component.getDynamicFloatProperty();
            assertThat(property.isScript()).isTrue();
            assertThat(property.body()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetDynamicDoubleProperty() {
            // Given
            double expectedValue = 43.234;

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicDoubleProperty", expectedValue);

            // Then
            DynamicDouble property = component.getDynamicDoubleProperty();
            assertThat(property.isScript()).isFalse();
            assertThat(property.value()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetScriptedDynamicDoubleProperty() {
            // Given
            String expectedValue = "#[49.432]";

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicDoubleProperty", expectedValue);

            // Then
            DynamicDouble property = component.getDynamicDoubleProperty();
            assertThat(property.isScript()).isTrue();
            assertThat(property.body()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetStringDynamicProperty() {
            // Given
            String expectedValue = "test value";

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicStringProperty", expectedValue);

            // Then
            DynamicString property = component.getDynamicStringProperty();
            assertThat(property.isScript()).isFalse();
            assertThat(property.value()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetScriptedStringDynamicProperty() {
            // Given
            String expectedValue = "#['my test']";

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicStringProperty", expectedValue);

            // Then
            DynamicString property = component.getDynamicStringProperty();
            assertThat(property.isScript()).isTrue();
            assertThat(property.body()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetObjectDynamicProperty() {
            // Given
            BigDecimal bigDecimal = new BigDecimal(432);

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicObjectProperty", bigDecimal);

            // Then
            DynamicObject property = component.getDynamicObjectProperty();
            assertThat(property.isScript()).isFalse();
            assertThat(property.value()).isEqualTo(bigDecimal);
        }

        @Test
        void shouldCorrectlySetScriptedObjectDynamicProperty() {
            // Given
            String expectedValue = "#['my test']";

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicObjectProperty", expectedValue);

            // Then
            DynamicObject property = component.getDynamicObjectProperty();
            assertThat(property.isScript()).isTrue();
            assertThat(property.body()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetBooleanDynamicProperty() {
            // Given
            boolean expectedValue = true;

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicBooleanProperty", expectedValue);

            // Then
            DynamicBoolean property = component.getDynamicBooleanProperty();
            assertThat(property.isScript()).isFalse();
            assertThat(property.value()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetScriptedBooleanDynamicProperty() {
            // Given
            String expectedValue= "#[1 == 1]";

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicBooleanProperty", expectedValue);

            // Then
            DynamicBoolean property = component.getDynamicBooleanProperty();
            assertThat(property.isScript()).isTrue();
            assertThat(property.body()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetIntegerDynamicProperty() {
            // Given
            int expectedValue = 43;

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicIntegerProperty", expectedValue);

            // Then
            DynamicInteger property = component.getDynamicIntegerProperty();
            assertThat(property.isScript()).isFalse();
            assertThat(property.value()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetScriptedIntegerDynamicProperty() {
            // Given
            String expectedValue = "#[4832]";

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicIntegerProperty", expectedValue);

            // Then
            DynamicInteger property = component.getDynamicIntegerProperty();
            assertThat(property.isScript()).isTrue();
            assertThat(property.body()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetByteArrayDynamicProperty() {
            // Given
            String expectedValue = "my byte array string";

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicByteArrayProperty", expectedValue);

            // Then
            DynamicByteArray property = component.getDynamicByteArrayProperty();
            assertThat(property.isScript()).isFalse();
            assertThat(property.value()).isEqualTo(expectedValue.getBytes());
        }

        @Test
        void shouldCorrectlySetScriptedByteArrayDynamicProperty() {
            // Given
            String expectedValue = "#['byte array body']";

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicByteArrayProperty", expectedValue);

            // Then
            DynamicByteArray property = component.getDynamicByteArrayProperty();
            assertThat(property.isScript()).isTrue();
            assertThat(property.body()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetBigDecimalDynamicProperty() {
            // Given
            double expectedValue = 2345342.234d;

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicBigDecimalProperty", expectedValue);

            // Then
            DynamicBigDecimal property = component.getDynamicBigDecimalProperty();
            assertThat(property.isScript()).isFalse();
            assertThat(property.value()).isEqualTo(new BigDecimal(expectedValue));
        }

        @Test
        void shouldCorrectlySetScriptedBigDecimalDynamicProperty() {
            // Given
            String expectedValue = "#[342]";

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicBigDecimalProperty", expectedValue);

            // Then
            DynamicBigDecimal property = component.getDynamicBigDecimalProperty();
            assertThat(property.isScript()).isTrue();
            assertThat(property.body()).isEqualTo(expectedValue);
        }

        @Test
        void shouldCorrectlySetBigIntegerDynamicProperty() {
            // Given
            int expectedValue = 234534;

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicBigIntegerProperty", expectedValue);

            // Then
            DynamicBigInteger property = component.getDynamicBigIntegerProperty();
            assertThat(property.isScript()).isFalse();
            assertThat(property.value()).isEqualTo(new BigInteger(String.valueOf(expectedValue)));
        }

        @Test
        void shouldCorrectlySetScriptedBigIntegerDynamicProperty() {
            // Given
            String expectedValue = "#[43]";

            // When
            TestComponentWithDynamicValueProperty component =
                    buildDynamicValueComponentWith("dynamicBigIntegerProperty", expectedValue);

            // Then
            DynamicBigInteger property = component.getDynamicBigIntegerProperty();
            assertThat(property.isScript()).isTrue();
            assertThat(property.body()).isEqualTo(expectedValue);
        }
    }

    @Nested
    @DisplayName("Dynamic map tests")
    class DynamicMapTests {

        @Test
        void shouldCorrectlySetDynamicStringMapProperty() {
            // Given
            JSONObject expectedValue = new JSONObject();
            expectedValue.put("one", "one value");
            expectedValue.put("two", "two value");
            expectedValue.put("three", "#['three value']");

            // When
            TestComponentWithDynamicMapProperty component =
                    buildDynamicMapComponentWith("dynamicStringMapProperty", expectedValue);

            // Then
            DynamicStringMap property = component.getDynamicStringMapProperty();
            assertThat(property).containsEntry("one", "one value");
            assertThat(property).containsEntry("two", "two value");
            assertThat(property).containsEntry("three", "#['three value']");
            assertThat(property).hasSize(3);
        }
    }
}
