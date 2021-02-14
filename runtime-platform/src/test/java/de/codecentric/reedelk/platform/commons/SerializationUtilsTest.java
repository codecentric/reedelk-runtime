package de.codecentric.reedelk.platform.commons;

import de.codecentric.reedelk.platform.test.utils.TestComponent;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageAttributes;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

class SerializationUtilsTest {

    @Test
    void shouldCloneMessageCorrectly() {
        // Given
        MyAttributes myAttributes = new MyAttributes();
        MyObject myObject = new MyObject(432);
        Message message = MessageBuilder.get(TestComponent.class)
                .attributes(myAttributes)
                .withJavaObject(myObject)
                .build();

        // When
        Message clone = SerializationUtils.clone(message);

        // Then
        assertThat(clone).isNotSameAs(message);
        assertThat(clone.attributes()).isNotSameAs(myAttributes);
        assertThat(clone.attributes()).containsEntry("attr1", "attr1 value");
        assertThat(clone.attributes()).containsEntry("attr2", "attr2 value");
        assertThat(clone.attributes()).containsEntry("attr3", "attr3 value");

        MyObject actualMyObject = clone.payload();
        assertThat(actualMyObject).isNotSameAs(myObject);
        assertThat(actualMyObject.value).isEqualTo(432);
    }

    static class MyAttributes extends MessageAttributes {
        MyAttributes() {
            put("attr1", "attr1 value");
            put("attr2", "attr2 value");
            put("attr3", "attr3 value");
        }
    }

    static class MyObject implements Serializable {

        int value;

        public MyObject(int value) {
            this.value = value;
        }
    }
}
