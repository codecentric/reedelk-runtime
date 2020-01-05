package com.reedelk.esb.execution;

import com.reedelk.esb.test.utils.TestComponent;
import com.reedelk.runtime.api.message.*;
import org.junit.jupiter.api.Test;

import static com.reedelk.runtime.api.commons.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;

class DefaultFlowContextTest {

    @Test
    void shouldCreateContextWithNewCorrelationIdIfDoesNotExistInMessageAttributes() {
        // Given
        Message message = MessageBuilder.get().empty().build();

        // When
        DefaultFlowContext context = DefaultFlowContext.from(message);

        // Then
        assertThat((String) context.get("correlationId")).isNotBlank();
    }

    @Test
    void shouldCreateContextWithCorrelationIdFromGivenMessageAttributes() {
        // Given
        String expectedCorrelationId = "aabbcc";
        MessageAttributes attributes = new DefaultMessageAttributes(TestComponent.class,
                of(MessageAttributeKey.CORRELATION_ID, expectedCorrelationId));

        Message message = MessageBuilder.get().attributes(attributes).empty().build();

        // When
        DefaultFlowContext context = DefaultFlowContext.from(message);

        // Then
        assertThat((String) context.get("correlationId")).isEqualTo(expectedCorrelationId);
    }
}