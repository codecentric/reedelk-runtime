package com.reedelk.runtime.api;

import com.reedelk.runtime.api.commons.ImmutableMap;
import com.reedelk.runtime.api.commons.TestComponent;
import com.reedelk.runtime.api.message.DefaultMessageAttributes;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedContent;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MessageCloneTest {

    @Test
    void shouldCloneMessageCorrectly() {
         // Given
         String expectedContent = "testing clone";

         MimeType expectedMimeType = MimeType.of(MimeType.TEXT, StandardCharsets.UTF_8.name());
         Message message = buildMessageWith(expectedMimeType, expectedContent, ImmutableMap.of("attr1", "value1", "aTTr2", "value2"));

         // When
         Message cloned = SerializationUtils.clone(message);

         // Then
         assertThat(message).isNotEqualTo(cloned);

         TypedContent<?,?> typedContent = cloned.content();
         assertThat(typedContent.data()).isEqualTo(expectedContent);
         assertThat(typedContent.type()).isEqualTo(String.class);

         MimeType actualMimeType = typedContent.mimeType();

         assertThat(actualMimeType.getSubType()).isEqualTo(expectedMimeType.getSubType());
         assertThat(actualMimeType.getPrimaryType()).isEqualTo(expectedMimeType.getPrimaryType());
         assertThat(actualMimeType.getCharset()).hasValue(expectedMimeType.getCharset().get());

        MessageAttributes attributes = cloned.attributes();
        assertThat((String) attributes.get("aTTr1")).isEqualTo("value1");
        assertThat((String) attributes.get("ATTR2")).isEqualTo("value2");
    }

    private Message buildMessageWith(MimeType mimeType, String content, Map<String, Serializable> attributes) {
        return MessageBuilder.get()
                .withString(content, mimeType)
                .attributes(new DefaultMessageAttributes(TestComponent.class, attributes))
                .build();
    }
}