package com.reedelk.runtime.api.message.content;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ByteArrayContentTest {

    @Test
    void shouldCorrectlyPrintByteArrayWhenLengthIsSmall() {
        // Given
        byte[] data = "Hello".getBytes();
        ByteArrayContent content = new ByteArrayContent(data, MimeType.APPLICATION_BINARY);

        // When
        String toString = content.toString();

        // Then
        assertThat(toString)
                .isEqualTo("ByteArray{type=[B, mimeType=application/octet-stream, " +
                        "consumed=true, " +
                        "streamReleased=false, " +
                        "data=[72, 101, 108, 108, 111]}");
    }

    @Test
    void shouldCorrectlyPrintByteArrayWhenLengthIsBig() {
        // Given
        byte[] data = "Hello this is a test".getBytes();
        ByteArrayContent content = new ByteArrayContent(data, MimeType.APPLICATION_BINARY);

        // When
        String toString = content.toString();

        // Then
        assertThat(toString)
                .isEqualTo("ByteArray{type=[B, mimeType=application/octet-stream, " +
                        "consumed=true, " +
                        "streamReleased=false, " +
                        "data=[72, 101, 108, 108, 111, ...]}");
    }
}
