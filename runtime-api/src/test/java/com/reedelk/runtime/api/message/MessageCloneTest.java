package com.reedelk.runtime.api.message;

import com.reedelk.runtime.api.commons.TestComponent;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedContent;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MessageCloneTest {

    static class MyMessageAttributes extends MessageAttributes {
        MyMessageAttributes() {
            put("attr1", "value1");
            put("aTTr2", "value2");
        }
    }

    @Test
    void shouldCloneMessageCorrectly() {
         // Given
         String expectedContent = "testing clone";

         MimeType expectedMimeType = MimeType.TEXT_PLAIN;

         Message message = buildMessageWith(expectedMimeType, expectedContent, new MyMessageAttributes());

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

        MessageAttributes attributes = cloned.attributes();
        assertThat((String) attributes.get("aTTr1")).isEqualTo("value1");
        assertThat((String) attributes.get("ATTR2")).isEqualTo("value2");
    }

    private Message buildMessageWith(MimeType mimeType, String content, MessageAttributes attributes) {
        return MessageBuilder.get(TestComponent.class)
                .withString(content, mimeType)
                .attributes(attributes)
                .build();
    }
}
