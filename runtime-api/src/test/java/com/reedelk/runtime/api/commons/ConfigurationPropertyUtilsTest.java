package com.reedelk.runtime.api.commons;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationPropertyUtilsTest {

    @Nested
    @DisplayName("is config property tests")
    class IsConfigProperty {

        @Test
        void shouldReturnFalseWhenValueIsNull() {
            // Given
            String given = null;

            // When
            boolean actual = ConfigurationPropertyUtils.isConfigProperty(given);

            // Then
            assertThat(actual).isFalse();
        }

        @Test
        void shouldReturnFalseWhenValueIsEmptyString() {
            // Given
            String given = "";

            // When
            boolean actual = ConfigurationPropertyUtils.isConfigProperty(given);

            // Then
            assertThat(actual).isFalse();
        }

        @Test
        void shouldReturnFalseWhenValueContainsOnlyOneChar() {
            // Given
            String given = "$";

            // When
            boolean actual = ConfigurationPropertyUtils.isConfigProperty(given);

            // Then
            assertThat(actual).isFalse();
        }

        @Test
        void shouldReturnFalseWhenValueContainsOnlyLeadingMarker() {
            // Given
            String given = "${";

            // When
            boolean actual = ConfigurationPropertyUtils.isConfigProperty(given);

            // Then
            assertThat(actual).isFalse();
        }

        @Test
        void shouldReturnTrue() {
            // Given
            String given = "${my.property}";

            // When
            boolean actual = ConfigurationPropertyUtils.isConfigProperty(given);

            // Then
            assertThat(actual).isTrue();
        }
    }

    @Nested
    @DisplayName("unwrap tests")
    class Unwrap {

        @Test
        void shouldReturnNullWhenInputIsNull() {
            // Given
            String given = null;

            // When
            String actual = ConfigurationPropertyUtils.unwrap(given);

            // Then
            assertThat(actual).isNull();
        }

        @Test
        void shouldReturnEmptyWhenInputIsEmpty() {
            // Given
            String given = "";

            // When
            String actual = ConfigurationPropertyUtils.unwrap(given);

            // Then
            assertThat(actual).isEmpty();
        }

        @Test
        void shouldReturnUnWrappedConfigPropertyKey() {
            // Given
            String given = "${smtp.port}";

            // When
            String actual = ConfigurationPropertyUtils.unwrap(given);

            // Then
            assertThat(actual).isEqualTo("smtp.port");
        }

        @Test
        void shouldReturnOriginalNotWellFormedConfigProperty() {
            // Given
            String  given = "${smtp.port";

            // When
            String actual = ConfigurationPropertyUtils.unwrap(given);

            // Then
            assertThat(actual).isEqualTo("${smtp.port");
        }
    }

    @Nested
    @DisplayName("as config property tests")
    class AsConfigPropertyTest {

        @Test
        void shouldConvertAsConfigPropertyStringValue() {
            // Given
            String given = "MY_CONFIG_PROPERTY";

            // When
            String actual = ConfigurationPropertyUtils.asConfigProperty(given);

            // Then
            assertThat(actual).isEqualTo("${MY_CONFIG_PROPERTY}");
        }
    }
}
