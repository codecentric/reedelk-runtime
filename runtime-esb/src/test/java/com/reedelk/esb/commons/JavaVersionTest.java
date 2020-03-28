package com.reedelk.esb.commons;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JavaVersionTest {

    // 11 -> 11.0.5,14 -> 14, 8 -> 1.8.0_66,
    @Test
    void shouldGreaterThan18ReturnTrueWhenVersionIs11() {
        // Given
        String fullVersion = "11.0.5"; // Java 11

        // When
        boolean actual = JavaVersion.internalIsGreaterThan18(fullVersion);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void shouldGreaterThan18ReturnTrueWhenVersionIs14() {
        // Given
        String fullVersion = "14";

        // When
        boolean actual = JavaVersion.internalIsGreaterThan18(fullVersion);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void shouldGreaterThan18ReturnFalseWhenVersionIs8() {
        // Given
        String fullVersion = "8";

        // When
        boolean actual = JavaVersion.internalIsGreaterThan18(fullVersion);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void shouldGreaterThan18ReturnFalseWhenVersionIs8WithMinor() {
        // Given
        String fullVersion = "1.8.0_66";

        // When
        boolean actual = JavaVersion.internalIsGreaterThan18(fullVersion);

        // Then
        assertThat(actual).isFalse();
    }
}
