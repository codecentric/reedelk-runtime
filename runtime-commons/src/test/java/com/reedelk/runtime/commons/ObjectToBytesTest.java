package com.reedelk.runtime.commons;

import com.reedelk.runtime.api.message.content.Attachment;
import com.reedelk.runtime.api.message.content.MimeType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectToBytesTest {

    @Test
    void shouldConvertAttachmentObjectToBytesWithoutThrowingException() {
        // Given
        Attachment attachment = Attachment.builder()
                .attribute("attr1", "attr1-value")
                .data("byte daat".getBytes())
                .mimeType(MimeType.APPLICATION_BINARY)
                .build();

        // When
        byte[] objectData = ObjectToBytes.from(attachment);

        // Then
        assertThat(objectData).isNotEmpty();
    }
}
