package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.commons.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AttachmentTest {

    @Test
    void shouldIsAttachmentMapReturnTrueWhenEmpty() {
        // Given
        Map<String, Attachment> attachmentMap = new HashMap<>();

        // When
        boolean actual = Attachment.isAttachmentMap(attachmentMap);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void shouldIsAttachmentMapReturnTrueWhenNotEmpty() {
        // Given
        Attachment attachment = Attachment.builder()
                .data("Test".getBytes())
                .mimeType(MimeType.TEXT_PLAIN)
                .attribute("attr1", "my-attribute")
                .build();
        Map<String, Attachment> attachmentMap =
                ImmutableMap.of("attachment1", attachment);

        // When
        boolean actual = Attachment.isAttachmentMap(attachmentMap);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void shouldIsAttachmentMapReturnFalseWhenKeyIsInteger() {
        // Given
        Attachment attachment = Attachment.builder()
                .data("Test".getBytes())
                .mimeType(MimeType.TEXT_PLAIN)
                .attribute("attr1", "my-attribute")
                .build();
        Map<Integer, Attachment> attachmentMap =
                ImmutableMap.of(2, attachment);

        // When
        boolean actual = Attachment.isAttachmentMap(attachmentMap);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void shouldIsAttachmentMapReturnFalseWhenValueIsString() {
        // Given
        Map<String, String> attachmentMap =
                ImmutableMap.of("item", "notAnAttachment");

        // When
        boolean actual = Attachment.isAttachmentMap(attachmentMap);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void shouldIsAttachmentMapReturnFalseWhenInputIsNull() {
        // When
        boolean actual = Attachment.isAttachmentMap(null);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void shouldIsAttachmentMapReturnFalseWhenInputIsList() {
        // When
        boolean actual = Attachment.isAttachmentMap(Arrays.asList("item1","item2"));

        // Then
        assertThat(actual).isFalse();
    }
}
