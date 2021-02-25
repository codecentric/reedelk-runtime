package de.codecentric.reedelk.runtime.api.commons;

import de.codecentric.reedelk.runtime.api.commons.StringUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    @Test
    void shouldReturnTrue() {
        // Given
        String test = "             ";

        // When
        boolean result = StringUtils.isBlank(test);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalse() {
        // Given
        String test = "     test        ";

        // When
        boolean result = StringUtils.isBlank(test);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueWhenNull() {
        // Given
        String test = null;

        // When
        boolean result = StringUtils.isBlank(test);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldEscapeQuotesCorrectly() {
        // Given
        String text = "My string 'with quotes' in it";

        // When
        String result = StringUtils.escapeQuotes(text);

        // Then
        assertThat(result).isEqualTo("My string \\'with quotes\\' in it");
    }

    @Test
    void shouldEscapeQuotesNotThrowException() {
        // Given
        String text = null;

        // When
        String result = StringUtils.escapeQuotes(text);

        // Then
        assertThat(result).isNull();
    }
}
