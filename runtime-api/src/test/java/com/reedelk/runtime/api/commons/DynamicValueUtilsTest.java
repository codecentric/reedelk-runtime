package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DynamicValueUtilsTest {

    @Test
    void shouldReturnFalseWhenDynamicStringIsNull() {
        // Given
        DynamicString given = null;

        // When
        boolean actual = DynamicValueUtils.isNotNullOrBlank(given);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void shouldReturnFalseWhenDynamicStringIsEmpty() {
        // Given
        DynamicString given = DynamicString.from("");

        // When
        boolean actual = DynamicValueUtils.isNotNullOrBlank(given);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void shouldReturnFalseWhenDynamicStringIsEmptyScript() {
        // Given
        DynamicString given = DynamicString.from("#[]", new ModuleContext(10L));

        // When
        boolean actual = DynamicValueUtils.isNotNullOrBlank(given);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void shouldReturnTrueWhenDynamicStringIsNotEmptyText() {
        // Given
        DynamicString given = DynamicString.from("myVariable");

        // When
        boolean actual = DynamicValueUtils.isNotNullOrBlank(given);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void shouldReturnTrueWhenDynamicStringIsNotEmptyScript() {
        // Given
        DynamicString given = DynamicString.from("#['myVariable']", new ModuleContext(10L));

        // When
        boolean actual = DynamicValueUtils.isNotNullOrBlank(given);

        // Then
        assertThat(actual).isTrue();
    }
}
