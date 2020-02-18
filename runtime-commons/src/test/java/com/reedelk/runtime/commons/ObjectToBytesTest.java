package com.reedelk.runtime.commons;

import com.reedelk.runtime.api.message.content.ByteArrayContent;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.Part;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectToBytesTest {

    @Test
    void shouldConvertPartObjectToBytesWithoutThrowingException() {
        // Given
        Part part = Part.builder()
                .attribute("attr1", "attr1-value")
                .name("myMessagePart")
                .content(new ByteArrayContent("byte daat".getBytes(), MimeType.APPLICATION_BINARY))
                .build();

        // When
        byte[] objectData = ObjectToBytes.from(part);

        // Then
        assertThat(objectData).isNotEmpty();
    }
}
