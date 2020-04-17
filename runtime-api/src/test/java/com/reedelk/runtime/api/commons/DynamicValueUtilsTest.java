package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
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

    @Test
    void shouldIsNullOrBlankReturnTrueWhenScriptIsEmpty() {
        // Given
        DynamicString given = DynamicString.from("#[]", new ModuleContext(10L));

        // When
        boolean actual = DynamicValueUtils.isNullOrBlank(given);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void shouldIsNullOrBlankReturnTrue() {
        // Given
        DynamicString given = null;

        // When
        boolean actual = DynamicValueUtils.isNullOrBlank(given);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void shouldIsNullOrBlankReturnFalse() {
        // Given
        DynamicString given = DynamicString.from("#[message.payload()]", new ModuleContext(10L));;;

        // When
        boolean actual = DynamicValueUtils.isNullOrBlank(given);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void shouldReturnFalseWhenDynamicObjectWithEmptyString() {
        // Given
        DynamicObject given = DynamicObject.from("");

        // When
        boolean actual = DynamicValueUtils.isNotNullOrBlank(given);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void shouldReturnTrueWhenDynamicObjectWithNotEmptyString() {
        // Given
        DynamicObject given = DynamicObject.from("This is a test");

        // When
        boolean actual = DynamicValueUtils.isNotNullOrBlank(given);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void shouldReturnTrueWhenDynamicObjectWithEmptyScript() {
        // Given
        DynamicObject given = DynamicObject.from("#[]", new ModuleContext(10L));

        // When
        boolean actual = DynamicValueUtils.isNotNullOrBlank(given);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void shouldReturnTrueWhenDynamicObjectWithNotEmptyScript() {
        // Given
        DynamicObject given = DynamicObject.from("#[ 2 + 3 ]", new ModuleContext(10L));

        // When
        boolean actual = DynamicValueUtils.isNotNullOrBlank(given);

        // Then
        assertThat(actual).isTrue();
    }
}
