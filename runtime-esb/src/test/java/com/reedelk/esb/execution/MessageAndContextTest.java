package com.reedelk.esb.execution;

import com.reedelk.runtime.api.message.FlowContext;
import com.reedelk.runtime.api.message.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MessageAndContextTest {

    @Mock
    private Message mockMessage;
    @Mock
    private FlowContext flowContext;

    @Test
    void shouldThrowExceptionWhenReplacedMessageIsNull() {
        // Given
        MessageAndContext messageAndContext = new MessageAndContext(mockMessage, flowContext);
        Message newNullMessage = null;

        // When
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> messageAndContext.replaceWith(newNullMessage));

        // Then
        assertThat(thrown).isNotNull();
        assertThat(thrown).hasMessage("Cannot replace current message with a null message: " +
                "processors are not allowed to return a null message; an empty message should be returned instead.");
    }
}