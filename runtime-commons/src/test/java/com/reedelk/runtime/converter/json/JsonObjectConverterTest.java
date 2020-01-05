package com.reedelk.runtime.converter.json;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JsonObjectConverterTest {

    private final String propertyKey = "TestProperty";

    private JsonObjectConverter converter = JsonObjectConverter.getInstance();

    @Nested
    @DisplayName("String Tests")
    class ToStringTests {

        @Test
        void shouldConvertToString() {
            // Given
            String expected = "My string value";
            JSONObject jsonObject = newObjectWith(expected);

            // When
            String actual = converter.convert(String.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToStringDefault() {
            // Given
            String expected = null;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            String actual = converter.convert(String.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToStringDefaultWhenPropertyAbsent() {
            // Given
            String expected = null;
            JSONObject jsonObject = newEmptyObject();

            // When
            String actual = converter.convert(String.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

    }

    @Nested
    @DisplayName("Long Object Tests")
    class ToLongObjectTests {

        @Test
        void shouldConvertToLongObject() {
            // Given
            Long expected = 23L;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            Long actual = converter.convert(Long.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToLongObjectDefault() {
            // Given
            Long expected = null;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            Long actual = converter.convert(Long.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToLongObjectDefaultWhenPropertyAbsent() {
            // Given
            Long expected = null;
            JSONObject jsonObject = newEmptyObject();

            // When
            Long actual = converter.convert(Long.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Long Tests")
    class ToLongTests {

        @Test
        void shouldConvertToLong() {
            // Given
            long expected = 20L;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            long actual = converter.convert(long.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToLongDefault() {
            // Given
            JSONObject jsonObject = newObjectWith(null);

            // When
            long actual = converter.convert(long.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(0L);
        }

        @Test
        void shouldConvertToLongDefaultWhenPropertyAbsent() {
            // Given
            JSONObject jsonObject = newEmptyObject();

            // When
            long actual = converter.convert(long.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(0L);
        }
    }

    @Nested
    @DisplayName("Integer Object Tests")
    class ToIntegerObjectTests {

        @Test
        void shouldConvertToIntegerObject() {
            // Given
            Integer expected = 23;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            Integer actual = converter.convert(Integer.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToIntegerObjectDefault() {
            // Given
            Integer expected = null;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            Integer actual = converter.convert(Integer.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToIntegerObjectDefaultWhenPropertyAbsent() {
            // Given
            Integer expected = null;
            JSONObject jsonObject = newEmptyObject();

            // When
            Integer actual = converter.convert(Integer.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Integer Tests")
    class ToIntegerTests {

        @Test
        void shouldConvertToInteger() {
            // Given
            int expected = 23;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            int actual = converter.convert(int.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToIntegerDefault() {
            // Given
            int expected = 0;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            int actual = converter.convert(int.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToIntegerDefaultWhenPropertyAbsent() {
            // Given
            int expected = 0;
            JSONObject jsonObject = newEmptyObject();

            // When
            int actual = converter.convert(int.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Double Object Tests")
    class ToDoubleObjectTests {

        @Test
        void shouldConvertToDoubleObject() {
            // Given
            Double expected = 234.234d;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            Double actual = converter.convert(Double.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToDoubleObjectDefault() {
            // Given
            Double expected = null;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            Double actual = converter.convert(Double.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToDoubleObjectDefaultWhenPropertyAbsent() {
            // Given
            Double expected = null;
            JSONObject jsonObject = newEmptyObject();

            // When
            Double actual = converter.convert(Double.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Double Tests")
    class ToDoubleTests {

        @Test
        void shouldConvertToDouble() {
            // Given
            double expected = 23.643d;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            double actual = converter.convert(double.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToDoubleDefault() {
            // Given
            double expected = 0d;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            double actual = converter.convert(double.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToDoubleDefaultWhenPropertyAbsent() {
            // Given
            double expected = 0d;
            JSONObject jsonObject = newEmptyObject();

            // When
            double actual = converter.convert(double.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Float object tests")
    class ToFloatObjectTests {

        @Test
        void shouldConvertToFloatObject() {
            // Given
            Float expected = 234.234f;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            Float actual = converter.convert(Float.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToFloatObjectDefault() {
            // Given
            Float expected = null;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            Float actual = converter.convert(Float.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToFloatObjectDefaultWhenPropertyAbsent() {
            // Given
            Float expected = null;
            JSONObject jsonObject = newEmptyObject();

            // When
            Float actual = converter.convert(Float.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Float tests")
    class ToFloatTests {

        @Test
        void shouldConvertToFloat() {
            // Given
            float expected = 234.234f;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            float actual = converter.convert(Float.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToFloatDefault() {
            // Given
            float expected = 0f;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            float actual = converter.convert(float.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToFloatDefaultWhenPropertyAbsent() {
            // Given
            float expected = 0f;
            JSONObject jsonObject = newEmptyObject();

            // When
            float actual = converter.convert(float.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Boolean object tests")
    class ToBooleanObjectTests {

        @Test
        void shouldConvertToBooleanObject() {
            // Given
            Boolean expected = true;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            Boolean actual = converter.convert(Boolean.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToBooleanObjectDefault() {
            // Given
            Boolean expected = null;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            Boolean actual = converter.convert(Boolean.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToBooleanObjectDefaultWhenPropertyAbsent() {
            // Given
            Boolean expected = null;
            JSONObject jsonObject = newEmptyObject();

            // When
            Boolean actual = converter.convert(Boolean.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Boolean tests")
    class ToBooleanTests {

        @Test
        void shouldConvertToBoolean() {
            // Given
            boolean expected = true;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            boolean actual = converter.convert(boolean.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToBooleanDefault() {
            // Given
            boolean expected = false;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            boolean actual = converter.convert(boolean.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToBooleanDefaultWhenPropertyAbsent() {
            // Given
            boolean expected = false;
            JSONObject jsonObject = newEmptyObject();

            // When
            boolean actual = converter.convert(boolean.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("BigDecimal tests")
    class ToBigDecimalTests {

        @Test
        void shouldConvertToBigDecimal() {
            // Given
            BigDecimal expected = new BigDecimal("234.234");
            JSONObject jsonObject = newObjectWith(expected);

            // When
            BigDecimal actual = converter.convert(BigDecimal.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToBigDecimalDefault() {
            // Given
            BigDecimal expected = null;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            BigDecimal actual = converter.convert(BigDecimal.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToBigDecimalDefaultWhenPropertyAbsent() {
            // Given
            BigDecimal expected = null;
            JSONObject jsonObject = newEmptyObject();

            // When
            BigDecimal actual = converter.convert(BigDecimal.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("BigInteger tests")
    class ToBigIntegerTests {

        @Test
        void shouldConvertToBigInteger() {
            // Given
            BigInteger expected = new BigInteger("234234");
            JSONObject jsonObject = newObjectWith(expected);

            // When
            BigInteger actual = converter.convert(BigInteger.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToBigIntegerDefault() {
            // Given
            BigInteger expected = null;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            BigInteger actual = converter.convert(BigInteger.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToBigIntegerDefaultWhenPropertyAbsent() {
            // Given
            BigInteger expected = null;
            JSONObject jsonObject = newEmptyObject();

            // When
            BigInteger actual = converter.convert(BigInteger.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Object tests")
    class ToObjectTests {

        @Test
        void shouldConvertToObject() {
            // Given
            String expected = "Hello test";
            JSONObject jsonObject = newObjectWith(expected);

            // When
            Object actual = converter.convert(Object.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToObjectDefault() {
            // Given
            String expected = null;
            JSONObject jsonObject = newObjectWith(expected);

            // When
            Object actual = converter.convert(Object.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldConvertToObjectDefaultWhenPropertyAbsent() {
            // Given
            String expected = null;
            JSONObject jsonObject = newEmptyObject();

            // When
            Object actual = converter.convert(Object.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Map tests")
    class ToMapTests {

        @Test
        void shouldConvertToMapCorrectly() {
            // Given
            JSONObject mapContent = new JSONObject();
            mapContent.put("property1", 2);
            mapContent.put("property2", "two");
            JSONObject jsonObject = newObjectWith(mapContent);

            // When
            Map<String,Object> actual = converter.convert(Map.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).containsEntry("property1", 2);
            assertThat(actual).containsEntry("property2", "two");
        }

        @Test
        void shouldConvertDefaultValueWhenEmpty() {
            // Given
            Map<String,Object> expected = null;
            JSONObject jsonObject = newEmptyObject();

            // When
            Map<String,Object> actual = converter.convert(Map.class, jsonObject, propertyKey);

            // Then
            assertThat(actual).isEqualTo(expected);
        }
    }

    private JSONObject newEmptyObject() {
        return new JSONObject();
    }

    private JSONObject newObjectWith(Object value) {
        JSONObject jsonObject = newEmptyObject();
        jsonObject.put(propertyKey, value);
        return jsonObject;
    }
}