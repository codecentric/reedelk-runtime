package com.reedelk.esb.commons;

import com.reedelk.runtime.api.commons.StringUtils;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourcePathTest {

    @Test
    void shouldReplaceEncodedSpacesWithSpaces() throws MalformedURLException {
        // Given
        URL url = new URL("file:/test/something/with%20space.txt");

        // When
        String actual = ResourcePath.from(url);

        // Then
        assertThat(actual).isEqualTo("/test/something/with space.txt");
    }

    @Test
    void shouldReplaceEncodedSpacesWithSpacesWhenBetweenDirectoryName() throws MalformedURLException {
        // Given
        URL url = new URL("file:/test/something%20important/with%20space.txt");

        // When
        String actual = ResourcePath.from(url);

        // Then
        assertThat(actual).isEqualTo("/test/something important/with space.txt");
    }

    @Test
    void shouldKeepEmptySpaces() throws MalformedURLException {
        // Given
        URL url = new URL("file:/test/something/with space.txt");

        // When
        String actual = ResourcePath.from(url);

        // Then
        assertThat(actual).isEqualTo("/test/something/with space.txt");
    }

    @Test
    void shouldThrowExceptionWhenURLIsNull() {
        // Given
        URL url = null;

        // When
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> ResourcePath.from(url));

        // Then
        assertThat(thrown).isNotNull();
        assertThat(thrown).hasMessage("url");
    }

    @Test
    void shouldReturnEmptyStringWhenURLIsEmpty() throws MalformedURLException {
        // Given
        URL url = new URL("file:    ");

        // When
        String actual = ResourcePath.from(url);

        // Then
        assertThat(actual).isEqualTo(StringUtils.EMPTY);
    }
}