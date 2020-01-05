package com.reedelk.runtime.api;

import com.reedelk.runtime.api.message.content.MimeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

class MimeTypeTest {

    @Nested
    @DisplayName("Parse tests")
    class Parse {

        @Test
        void shouldParseCorrectlyPrimaryTypeAndSubtype() {
            // When
            MimeType actualMimeType = MimeType.parse("application/json");

            // Then
            assertThat(actualMimeType.getPrimaryType()).isEqualTo("application");
            assertThat(actualMimeType.getSubType()).isEqualTo("json");
        }

        @Test
        void shouldParseCorrectlyCharset() {
            // Given
            String mimeType = "application/json;charset=UTF-8";

            // When
            MimeType actualMimeType = MimeType.parse(mimeType);

            // Then
            assertThat(actualMimeType.getCharset().isPresent()).isTrue();
            assertThat(actualMimeType.getCharset().get()).isEqualTo(Charset.forName("UTF-8"));
        }

        @Test
        void shouldParseReturnUnknownWhenGivenStringIsNull() {
            // When
            MimeType actualMimeType = MimeType.parse(null);

            // Then
            assertThat(actualMimeType).isEqualTo(MimeType.UNKNOWN);
        }

        @Test
        void shouldParseReturnUnknownWhenGivenStringIsEmpty() {
            // When
            MimeType actualMimeType = MimeType.parse("");

            // Then
            assertThat(actualMimeType).isEqualTo(MimeType.UNKNOWN);
        }
    }

    @Nested
    @DisplayName("Equals tests")
    class Equals {

        @Test
        void shouldEqualWorkCorrectly() {
            // Given
            MimeType mimeType1 = MimeType.parse("application/json;charset=UTF-8");
            MimeType mimeType2 = MimeType.parse("application/json");

            // When
            boolean areEquals = mimeType1.equals(mimeType2);

            // Then
            assertThat(areEquals).isTrue();
        }
    }

    @Nested
    @DisplayName("To string tests")
    class ToString {

        @Test
        void shouldToStringReturnCorrectRepresentation() {
            // Given
            String expected = "application/json";

            // When
            String actual = MimeType.APPLICATION_JSON.toString();

            // Then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("MimeType from file extension")
    class FromFileExtension {

        @Test
        void shouldReturnMimeTypeFromCaseInsensitiveFileExtension() {
            // Given
            String given = "JpG";

            // When
            MimeType actual = MimeType.fromFileExtension(given);

            // Then
            assertThat(actual).isEqualTo(MimeType.IMAGE_JPEG);
        }
    }
}