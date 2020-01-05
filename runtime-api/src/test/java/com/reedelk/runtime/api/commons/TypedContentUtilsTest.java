package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.message.content.ByteArrayContent;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.StringContent;
import com.reedelk.runtime.api.message.content.TypedContent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TypedContentUtilsTest {

    @Test
    void shouldConvertByteArrayToStringWhenMimeTypeIsText() {
        // Given
        byte[] given = "Test typed content".getBytes();

        // When
        TypedContent<?> actual = TypedContentUtils.from(given, MimeType.TEXT);

        // Then
        assertThat(actual).isInstanceOf(StringContent.class);
        assertThat(actual.type()).isEqualTo(String.class);
    }

    @Test
    void shouldNotConvertByteArrayWhenMimeTypeIsUnknown() {
        // Given
        byte[] given = "Test typed content".getBytes();

        // When
        TypedContent<?> actual = TypedContentUtils.from(given, MimeType.UNKNOWN);

        // Then
        assertThat(actual).isInstanceOf(ByteArrayContent.class);
        assertThat(actual.type()).isEqualTo(byte[].class);
    }
}