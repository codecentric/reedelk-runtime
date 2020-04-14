package com.reedelk.platform.flow;

import com.reedelk.runtime.api.commons.ImmutableMap;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.DefaultMessageAttributes;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedContent;
import com.reedelk.runtime.api.message.content.TypedPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DisposableContextAwareMessageTest {

    @Mock
    private FlowContext mockContext;

    private Message sampleMessage;
    private DisposableContextAwareMessage wrappedMessage;

    @BeforeEach
    void setUp() {
        MessageAttributes attributes =
                new DefaultMessageAttributes(TestComponent.class, ImmutableMap.of("attr1", "value1", "attr2", "value2"));
        sampleMessage = spy(MessageBuilder.get()
                .attributes(attributes)
                .withText("My sample message content")
                .build());

        wrappedMessage = new DisposableContextAwareMessage(mockContext, sampleMessage);
    }

    @Test
    void shouldDisposeWhenAttributesAreFetched() {
        // When
        wrappedMessage.attributes();

        // Then
        assertThat(wrappedMessage.shouldDispose()).isTrue();
    }

    @Test
    void shouldDisposeWhenPayloadIsFetched() {
        // When
        wrappedMessage.payload();

        // Then
        assertThat(wrappedMessage.shouldDispose()).isTrue();
    }

    @Test
    void shouldNotDisposeWhenPayloadStreamIsFetched() {
        // When
        wrappedMessage.content().stream();

        // Then
        assertThat(wrappedMessage.shouldDispose()).isFalse();
    }

    @Test
    void shouldDelegateAttributes() {
        // When
        wrappedMessage.attributes();

        // Then
        verify(sampleMessage).attributes();
    }

    @Test
    void shouldDelegateGetAttributes() {
        // When
        wrappedMessage.getAttributes();

        // Then
        verify(sampleMessage).getAttributes();
    }

    @Test
    void shouldDelegateGetPayload() {
        // When
        wrappedMessage.payload();

        // Then
        verify(sampleMessage).payload();
    }

    @Test
    void shouldDelegateContent() {
        // When
        wrappedMessage.content();

        // Then
        verify(sampleMessage).content();
    }

    @Test
    void shouldDelegateContentType() {
        // When
        Class<Object> type = wrappedMessage.content().type();

        // Then
        assertThat(type).isEqualTo(String.class);
    }

    @Test
    void shouldDelegateMimeType() {
        // When
        MimeType mimeType = wrappedMessage.content().mimeType();

        // Then
        assertThat(mimeType).isEqualTo(MimeType.TEXT_PLAIN);
    }

    @Test
    void shouldDelegateData() {
        // When
        TypedContent<String, String> content = wrappedMessage.content();
        String data = content.data();

        // Then
        assertThat(data).isEqualTo("My sample message content");
    }

    @Test
    void shouldDelegateStream() {
        // When
        TypedContent<String, String> content = wrappedMessage.content();
        TypedPublisher<String> stream = content.stream();

        // Then
        StepVerifier.create(stream)
                .expectNext("My sample message content")
                .verifyComplete();
    }

    @Test
    void shouldDelegateIsStream() {
        // When
        TypedContent<String, String> content = wrappedMessage.content();

        // Then
        assertThat(content.isStream()).isFalse();
    }

    static class TestComponent implements ProcessorSync {
        @Override
        public Message apply(FlowContext flowContext, Message message) {
            throw new UnsupportedOperationException("Not supposed to be called");
        }
    }
}
