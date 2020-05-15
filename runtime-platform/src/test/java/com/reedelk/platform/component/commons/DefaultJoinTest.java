package com.reedelk.platform.component.commons;

import com.reedelk.platform.test.utils.TestComponent;
import com.reedelk.runtime.api.component.Join;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.MessageBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DefaultJoinTest {

    @Mock
    private FlowContext context;

    @Test
    void shouldCorrectlyMergeAttributesAndPayloadsFromAllMessages() {
        // Given
        MessageAttributes attributes1 = new MessageAttributes();
        attributes1.put("key1", "value1");

        Message message1 = MessageBuilder.get(TestComponent.class)
                .attributes(attributes1)
                .withText("My first payload")
                .build();

        MessageAttributes attributes2 = new MessageAttributes();
        attributes1.put("key2", "value2");

        Message message2 = MessageBuilder.get(TestComponent.class)
                .attributes(attributes2)
                .withText("My second payload")
                .build();

        Join join = new DefaultJoin();
        List<Message> input = Arrays.asList(message1, message2);

        // When
        Message result = join.apply(context, input);

        // Then
        MessageAttributes attributes = result.attributes();
        assertThat(attributes).containsEntry("key1", "value1");
        assertThat(attributes).containsEntry("key2", "value2");

        List<String> payload = result.payload();
        assertThat(payload).containsExactlyInAnyOrder("My first payload", "My second payload");
    }
}
