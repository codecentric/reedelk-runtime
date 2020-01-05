package com.reedelk.esb.services.scriptengine.converter;

import com.reedelk.esb.services.converter.DefaultConverterService;
import com.reedelk.runtime.api.commons.ImmutableMap;
import com.reedelk.runtime.api.commons.StackTraceUtils;
import com.reedelk.runtime.api.converter.ConverterService;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.message.content.utils.TypedPublisher;
import com.reedelk.runtime.commons.ObjectToBytes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class converterServiceTest {
    
    private ConverterService converterService = DefaultConverterService.getInstance();

    @Nested
    @DisplayName("Convert boolean from input to output class")
    class ConvertBoolean {

        @Test
        void shouldConvertBooleanTrueToByteArray() {
            // Given
            Boolean value = true;

            // When
            byte[] actual = converterService.convert(value, byte[].class);

            // Then
            assertThat(actual).isEqualTo(new byte[]{1});
        }

        @Test
        void shouldConvertBooleanFalseToByteArray() {
            // Given
            Boolean value = false;

            // When
            byte[] actual = converterService.convert(value, byte[].class);

            // Then
            assertThat(actual).isEqualTo(new byte[]{0});
        }

        @Test
        void shouldConvertBooleanTrueToDouble() {
            // Given
            Boolean value = true;

            // When
            Double actual = converterService.convert(value, Double.class);

            // Then
            assertThat(actual).isEqualTo(1d);
        }

        @Test
        void shouldConvertBooleanFalseToDouble() {
            // Given
            Boolean value = false;

            // When
            Double actual = converterService.convert(value, Double.class);

            // Then
            assertThat(actual).isEqualTo(0d);
        }

        @Test
        void shouldConvertBooleanTrueToFloat() {
            // Given
            Boolean value = true;

            // When
            Float actual = converterService.convert(value, Float.class);

            // Then
            assertThat(actual).isEqualTo(1f);
        }

        @Test
        void shouldConvertBooleanFalseToFloat() {
            // Given
            Boolean value = false;

            // When
            Float actual = converterService.convert(value, Float.class);

            // Then
            assertThat(actual).isEqualTo(0f);
        }

        @Test
        void shouldConvertBooleanTrueToInteger() {
            // Given
            Boolean value = true;

            // When
            Integer actual = converterService.convert(value, Integer.class);

            // Then
            assertThat(actual).isEqualTo(1);
        }

        @Test
        void shouldConvertBooleanFalseToInteger() {
            // Given
            Boolean value = false;

            // When
            Integer actual = converterService.convert(value, Integer.class);

            // Then
            assertThat(actual).isEqualTo(0);
        }

        @Test
        void shouldConvertBooleanTrueToString() {
            // Given
            Boolean value = true;

            // When
            String actual = converterService.convert(value, String.class);

            // Then
            assertThat(actual).isEqualTo("true");
        }

        @Test
        void shouldConvertBooleanFalseToString() {
            // Given
            Boolean value = false;

            // When
            String actual = converterService.convert(value, String.class);

            // Then
            assertThat(actual).isEqualTo("false");
        }

        @Test
        void shouldReturnNullWhenInputIsNull() {
            // Given
            Boolean value = null;

            // When
            String actual = converterService.convert(value, String.class);

            // Then
            assertThat(value).isNull();
        }

        @Test
        void shouldConvertNotBooleanObjectToString() {
            // Given
            boolean value = true;

            // When
            String actual = converterService.convert(value, String.class);

            // Then
            assertThat(actual).isEqualTo("true");
        }
    }

    @Nested
    @DisplayName("Convert byte array from input to output class")
    class ConvertByteArray {

        @Test
        void shouldConvertByteArrayToString() {
            // Given
            byte[] value = "my test".getBytes();

            // When
            String result = converterService.convert(value, String.class);

            // Then
            assertThat(result).isEqualTo("my test");
        }

        @Test
        void shouldReturnNullWhenInputIsNull() {
            // Given
            byte[] value = null;

            // When
            String actual = converterService.convert(value, String.class);

            // Then
            assertThat(actual).isNull();
        }
    }

    @Nested
    @DisplayName("Convert byte array from input to output class")
    class ConvertDouble {

        @Test
        void shouldConvertDoubleToBooleanTrue() {
            // Given
            Double value = 1d;

            // When
            Boolean actual = converterService.convert(value, Boolean.class);

            // Then
            assertThat(actual).isTrue();
        }

        @Test
        void shouldConvertDoubleToBooleanFalse() {
            // Given
            Double value = 0d;

            // When
            Boolean actual = converterService.convert(value, Boolean.class);

            // Then
            assertThat(actual).isFalse();
        }

        @Test
        void shouldConvertDoubleToByteArray() {
            // Given
            Double value = 2342.234d;

            // When
            byte[] actual = converterService.convert(value, byte[].class);

            // Then
            assertThat(actual).isEqualTo(new byte[]{value.byteValue()});
        }

        @Test
        void shouldConvertDoubleToFloat() {
            // Given
            Double value = 234.123d;

            // When
            Float actual = converterService.convert(value, Float.class);

            // Then
            assertThat(actual).isEqualTo(value.floatValue());
        }

        @Test
        void shouldConvertDoubleToInteger() {
            // Given
            Double value = 2812.23d;

            // When
            Integer actual = converterService.convert(value, Integer.class);

            // Then
            assertThat(actual).isEqualTo(value.intValue());
        }

        @Test
        void shouldConvertDoubleToString() {
            // Given
            Double value = 234.1234d;

            // When
            String actual = converterService.convert(value, String.class);

            // Then
            assertThat(actual).isEqualTo(String.valueOf(value));
        }

        @Test
        void shouldReturnNullWhenInputIsNull() {
            // Given
            Double value = null;

            // When
            String actual = converterService.convert(value, String.class);

            // Then
            assertThat(actual).isNull();
        }

        @Test
        void shouldConvertNotDoubleObjectToString() {
            // Given
            double value = 23.32d;

            // When
            String actual = converterService.convert(value, String.class);

            // Then
            assertThat(actual).isEqualTo(String.valueOf(value));
        }
    }

    @Nested
    @DisplayName("Convert exception from input to output class")
    class ConvertException {

        @Test
        void shouldConvertExceptionToString() {
            // Given
            ESBException testException = new ESBException("an error");

            // When
            String result = converterService.convert(testException, String.class);

            // Then
            assertThat(result).isEqualTo(StackTraceUtils.asString(testException));
        }

        @Test
        void shouldConvertExceptionToByteArray() {
            // Given
            ESBException testException = new ESBException("another error");

            // When
            byte[] result = converterService.convert(testException, byte[].class);

            // Then
            assertThat(result).isEqualTo(StackTraceUtils.asByteArray(testException));
        }

        @Test
        void shouldThrowExceptionWhenConverterNotPresent() {
            // Given
            DummyClazz input = new DummyClazz();

            // When
            ESBException exception = Assertions.assertThrows(ESBException.class,
                    () -> converterService.convert(input, Integer.class));

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception).hasMessage("Converter for input=[com.reedelk.esb.services.scriptengine.converter.converterServiceTest$DummyClazz] to output=[java.lang.Integer] not available");
        }

        @Test
        void shouldReturnNullWhenInputIsNull() {
            // Given
            Exception value = null;

            // When
            String actual = converterService.convert(value, String.class);

            // Then
            assertThat(actual).isNull();
        }
    }

    @Nested
    @DisplayName("Convert float input to output class")
    class ConvertFloat {

        @Test
        void shouldConvertFloatToBoolean() {
            // Given
            Float value = 1f;

            // When
            Boolean actual = converterService.convert(value, Boolean.class);

            // Then
            assertThat(actual).isTrue();
        }

        @Test
        void shouldConvertFloatToByteArray() {
            // Given
            Float value = 1f;

            // When
            byte[] actual = converterService.convert(value, byte[].class);

            // Then
            assertThat(actual).isEqualTo(new byte[]{value.byteValue()});
        }

        @Test
        void shouldConvertFloatToDouble() {
            // Given
            Float value = 234.234f;

            // When
            Double actual = converterService.convert(value, Double.class);

            // Then
            assertThat(actual).isEqualTo(value.doubleValue());
        }

        @Test
        void shouldConvertFloatToInteger() {
            // Given
            Float value = 212.2348f;

            // When
            Integer actual = converterService.convert(value, Integer.class);

            // Then
            assertThat(actual).isEqualTo(value.intValue());
        }

        @Test
        void shouldConvertFloatToString() {
            // Given
            Float value = 234.12f;

            // When
            String actual  = converterService.convert(value, String.class);

            // Then
            assertThat(actual).isEqualTo(String.valueOf(value));
        }

        @Test
        void shouldConvertNotFloatObjectToString() {
            // Given
            float value = 23.32f;

            // When
            String actual = converterService.convert(value, String.class);

            // Then
            assertThat(actual).isEqualTo(String.valueOf(value));
        }
    }

    @Nested
    @DisplayName("Convert integer from input to output class")
    class ConvertInteger {

        @Test
        void shouldCovertIntegerToBoolean() {
            // Given
            Integer value = 1;

            // When
            Boolean result = converterService.convert(value, Boolean.class);

            // Then
            assertThat(result).isTrue();
        }

        @Test
        void shouldConvertIntegerToByteArray() {
            // Given
            Integer value = 234123;

            // When
            byte[] result = converterService.convert(value, byte[].class);

            // Then
            assertThat(result).isEqualTo(new byte[]{value.byteValue()});
        }

        @Test
        void shouldConvertIntegerToDouble() {
            // Given
            Integer value = 234;

            // When
            Double result = converterService.convert(value, Double.class);

            // Then
            assertThat(result).isEqualTo(value.doubleValue());
        }

        @Test
        void shouldConvertIntegerToFloat() {
            // Given
            Integer value = 865;

            // When
            Float result = converterService.convert(value, Float.class);

            // Then
            assertThat(result).isEqualTo(value.floatValue());
        }

        @Test
        void shouldConvertIntegerToString() {
            // Given
            Integer value = 234;

            // When
            String result = converterService.convert(value, String.class);

            // Then
            assertThat(result).isEqualTo("234");
        }

        @Test
        void shouldConvertNotDoubleObjectToString() {
            // Given
            int value = 23;

            // When
            String actual = converterService.convert(value, String.class);

            // Then
            assertThat(actual).isEqualTo(String.valueOf(value));
        }
    }

    @Nested
    @DisplayName("Convert string from input to output class")
    class ConvertString {

        @Test
        void shouldConvertStringToBoolean() {
            // Given
            String value = "true";

            // When
            Boolean actual = converterService.convert(value, Boolean.class);

            // Then
            assertThat(actual).isTrue();
        }

        @Test
        void shouldConvertStringToByteArray() {
            // Given
            String value = "Test text value";

            // When
            byte[] actual = converterService.convert(value, byte[].class);

            // Then
            assertThat(actual).isEqualTo(value.getBytes());
        }

        @Test
        void shouldConvertStringToDouble() {
            // Given
            String value = "234.21312";

            // When
            Double actual = converterService.convert(value, Double.class);

            // Then
            assertThat(actual).isEqualTo(Double.parseDouble(value));
        }

        @Test
        void shouldConvertStringToFloat() {
            // Given
            String value = "24.2341";

            // When
            Float actual = converterService.convert(value, Float.class);

            // Then
            assertThat(actual).isEqualTo(Float.parseFloat(value));
        }

        @Test
        void shouldConvertStringToInteger() {
            // Given
            String value = "234";

            // When
            Integer actual = converterService.convert(value, Integer.class);

            // Then
            assertThat(actual).isEqualTo(Integer.parseInt(value));
        }

        @Test
        void shouldConvertStringToString() {
            // Given
            String value = "Test text value";

            // When
            String result = converterService.convert(value, String.class);

            // Then
            assertThat(result).isEqualTo(value);
        }

        @Test
        void shouldReturnNullWhenValueToConvertIsNull() {
            // Given
            String value = null;

            // When
            String result = converterService.convert(value, String.class);

            // Then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("Convert any input to object")
    class ConvertUnknownType {

        @Test
        void shouldReturnOriginalValueWhenOutputClazzIsObject() {
            // Given
            BigDecimal aBigDecimal = new BigDecimal(234);

            // When
            Object result = converterService.convert(aBigDecimal, Object.class);

            // Then
            assertThat(result).isEqualTo(aBigDecimal);
        }

        @Test
        void shouldReturnObjectAsString() {
            // Given
            Map<String,String> keysAndValues = ImmutableMap.of("key1", "value1", "key2", "value2");

            // When
            String result = converterService.convert(keysAndValues, String.class);

            // Then
            assertThat(result).isEqualTo(keysAndValues.toString());
        }

        @Test
        void shouldReturnObjectAsByteArray() {
            // Given
            MyTestClazz value = new MyTestClazz(23,"test");

            // When
            byte[] result = converterService.convert(value, byte[].class);

            // Then
            assertThat(result).isEqualTo(ObjectToBytes.from(value));
        }
    }

    @Nested
    @DisplayName("Convert typed publisher")
    class ConvertTypedPublisher {

        @Test
        void shouldConvertStringToInteger() {
            // Given
            TypedPublisher<String> input = TypedPublisher.fromString(Flux.just("1", "2", "4"));

            // When
            TypedPublisher<Integer> converted =
                    converterService.convert(input, Integer.class);

            // Then
            StepVerifier.create(converted)
                    .expectNext(1, 2, 4)
                    .verifyComplete();
        }

        @Test
        void shouldConvertStringToObject() {
            // Given
            TypedPublisher<String> input = TypedPublisher.fromString(Flux.just("1", "2", "4"));

            // When
            TypedPublisher<Object> converted =
                    converterService.convert(input, Object.class);

            // Then
            StepVerifier.create(converted)
                    .expectNext("1", "2", "4")
                    .verifyComplete();
            assertThat(input).isEqualTo(converted);
        }

        @Test
        void shouldStreamPropagateErrorWhenConversionIsFailed() {
            // Given
            TypedPublisher<String> input = TypedPublisher.fromString(Flux.just("1", "not a number", "2"));

            // When
            Publisher<Integer> converted =
                    converterService.convert(input, Integer.class);

            // Then
            StepVerifier.create(converted)
                    .expectNext(1)
                    .expectError()
                    .verify();
        }

        @Test
        void shouldReturnNullWhenPublisherIsNull() {
            // Given
            TypedPublisher<Integer> input = null;

            // When
            Publisher<Integer> converted =
                    converterService.convert(input, Integer.class);

            // Then
            assertThat(converted).isNull();
        }

        @Test
        void shouldReturnOriginalPublisherIfPublisherTypeEqualsOutputType() {
            // Given
            TypedPublisher<Integer> input = TypedPublisher.fromInteger(Flux.just(3, 4, 20));

            // When
            TypedPublisher<Integer> result =
                    converterService.convert(input, Integer.class);

            // Then
            assertThat(result).isEqualTo(input);
        }

        @Test
        void shouldThrowExceptionWhenConverterNotPresent() {
            // Given
            TypedPublisher<DummyClazz> input = TypedPublisher.from(Flux.just(new DummyClazz()), DummyClazz.class);

            // When
            ESBException exception = Assertions.assertThrows(ESBException.class,
                    () -> converterService.convert(input, Integer.class));

            // Then
            assertThat(exception).isNotNull();
            assertThat(exception).hasMessage("Converter for input=[com.reedelk.esb.services.scriptengine.converter.converterServiceTest$DummyClazz] to output=[java.lang.Integer] not available");
        }
    }

    @Test
    void shouldConvertWithInputAndOutputReturnNullWhenInputIsNull() {
        // Given
        BigInteger input = null;

        // When
        Object actual = converterService.convert(input, Object.class);

        // Then
        assertThat(actual).isNull();
    }

    private static class DummyClazz{}
}