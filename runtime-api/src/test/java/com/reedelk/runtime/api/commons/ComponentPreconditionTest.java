package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.commons.ComponentPrecondition.Input;
import com.reedelk.runtime.api.exception.ComponentInputException;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ComponentPreconditionTest {

    @Test
    void shouldNotThrowExceptionWhenInputStringAndWantedString() {
        // Given
        String input = "This is my input";

        // Expect
        assertDoesNotThrow(() ->
                Input.requireTypeMatches(TestComponent.class, input, String.class));
    }

    @Test
    void shouldNotThrowExceptionWhenInputLongAndWantedStringOrLong() {
        // Given
        long input = 10L;

        // Expect
        assertDoesNotThrow(() ->
                Input.requireTypeMatchesAny(TestComponent.class, input, String.class, Long.class));
    }

    @Test
    void shouldNotThrowExceptionWhenInputByteArrayAndWantedByteArray() {
        // Given
        byte[] input = "This is my input".getBytes();

        // Expect
        assertDoesNotThrow(() ->
                Input.requireTypeMatches(TestComponent.class, input, byte[].class));
    }

    @Test
    void shouldThrowExceptionWhenInputListAndWantedString() {
        // Given
        List<String> input = Arrays.asList("one", "two", "three");

        // When
        ComponentInputException thrown = assertThrows(ComponentInputException.class,
                () -> Input.requireTypeMatches(TestComponent.class, input, String.class));

        // Then
        String expected = "TestComponent (com.reedelk.runtime.api.commons.TestComponent) was invoked with " +
                "a not supported Input Type: actual=[List], expected=[String].";
        assertThat(thrown).hasMessage(expected);
    }

    @Test
    void shouldThrowExceptionWhenWantedIsEmpty() {
        // Given
        String input = "This is my input";

        // When
        ComponentInputException thrown = assertThrows(ComponentInputException.class,
                () -> Input.requireTypeMatchesAny(TestComponent.class, input));

        // Then
        String expected = "TestComponent (com.reedelk.runtime.api.commons.TestComponent) was invoked with " +
                "a not supported Input Type: actual=[String], expected=[].";
        assertThat(thrown).hasMessage(expected);
    }

    @Test
    void shouldNotThrowExceptionWhenInputSubclassOfExpected() {
        // Given
        MyMap input = new MyMap();

        // Expect
        assertDoesNotThrow(() ->
                Input.requireTypeMatches(TestComponent.class, input, Map.class));
    }

    @Test
    void shouldThrowExceptionWhenInputSubclassOfNotExpected() {
        // Given
        MyMap input = new MyMap();

        // When
        ComponentInputException thrown = assertThrows(ComponentInputException.class,
                () -> Input.requireTypeMatches(TestComponent.class, input, String.class));

        // Then
        String expected = "TestComponent (com.reedelk.runtime.api.commons.TestComponent) was invoked with " +
                "a not supported Input Type: actual=[Map], expected=[String].";
        assertThat(thrown).hasMessage(expected);
    }

    @Test
    void shouldThrowExceptionWhenInputIsNull() {
        // Given
        MyMap input = null;

        // When
        ComponentInputException thrown = assertThrows(ComponentInputException.class,
                () -> Input.requireTypeMatches(TestComponent.class, input, String.class));

        // Then
        String expected = "TestComponent (com.reedelk.runtime.api.commons.TestComponent) was invoked with " +
                "a not supported Input Type: actual=[null], expected=[String].";
        assertThat(thrown).hasMessage(expected);
    }

    @Test
    void shouldThrowExceptionWhenInputIsListAndExpectedMap() {
        // Given
        List<MyMap> input = Arrays.asList(new MyMap(), new MyMap());

        // When
        ComponentInputException thrown = assertThrows(ComponentInputException.class,
                () -> Input.requireTypeMatches(TestComponent.class, input, Map.class));

        // Then
        String expected = "TestComponent (com.reedelk.runtime.api.commons.TestComponent) was invoked with " +
                "a not supported Input Type: actual=[List], expected=[Map].";
        assertThat(thrown).hasMessage(expected);
    }

    @Test
    void shouldNotThrowExceptionWhenInputIsNull() {
        // Given
        List<MyMap> input = null;

        // Expect
        assertDoesNotThrow(() ->
                Input.requireTypeMatchesOrNull(TestComponent.class, input, byte[].class));
    }

    static class MyMap extends HashMap<String, Serializable> {
    }
}
