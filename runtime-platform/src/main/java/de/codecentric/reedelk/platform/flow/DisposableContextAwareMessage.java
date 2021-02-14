package de.codecentric.reedelk.platform.flow;

import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageAttributes;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.runtime.api.message.content.TypedContent;
import de.codecentric.reedelk.runtime.api.message.content.TypedPublisher;
import reactor.core.publisher.Flux;

/**
 * A message implementation which disposes the message only when
 * the payload is consumed via the stream or data.
 */
public class DisposableContextAwareMessage implements Message {

    private final Message delegate;
    private final TypedContentWrapper<?,?> content;

    DisposableContextAwareMessage(FlowContext context, Message delegate) {
        this.delegate = delegate;
        this.content = new TypedContentWrapper<>(delegate.content(), context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, StreamType, U extends TypedContent<T, StreamType>> U getContent() {
        return (U) content;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, StreamType, U extends TypedContent<T, StreamType>> U content() {
        return (U) content;
    }

    @Override
    public <T> T payload() {
        return delegate.payload();
    }

    @Override
    public MessageAttributes getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public MessageAttributes attributes() {
        return delegate.attributes();
    }

    public boolean shouldDispose() {
        return content.shouldDispose();
    }

    private static class TypedContentWrapper<T, StreamType> implements TypedContent<T, StreamType> {

        private transient final FlowContext context;
        private final TypedContent<T, StreamType> delegate;

        private boolean shouldDispose = true;

        TypedContentWrapper(TypedContent<T, StreamType> delegate, FlowContext context) {
            this.context = context;
            this.delegate = delegate;
        }

        @Override
        public Class<T> type() {
            return delegate.type();
        }

        @Override
        public Class<StreamType> streamType() {
            return delegate.streamType();
        }

        @Override
        public MimeType mimeType() {
            return delegate.mimeType();
        }

        @Override
        public T data() {
            return delegate.data();
        }

        /**
         * We must dispose the context only *after* (doOnTerminate) the stream
         * has been consumed, otherwise we might close resources (e.g database connections)
         * before they are actually used while consuming the stream.
         * @return a wrapped publisher in which the context is disposed at the end of the stream.
         */
        @Override
        public TypedPublisher<StreamType> stream() {
            synchronized (this) {
                TypedPublisher<StreamType> stream = delegate.stream();
                Flux<StreamType> itemTypeFlux = Flux.from(stream).doOnTerminate(context::dispose);
                TypedPublisher<StreamType> resultStream = TypedPublisher.from(itemTypeFlux, stream.getType());
                shouldDispose = false; // this is because the context is disposed only after the stream has been consumed (doOnTerminate).
                return resultStream;
            }
        }

        @Override
        public boolean isStream() {
            return delegate.isStream();
        }

        @Override
        public void consume() {
            delegate.consume();
        }

        boolean shouldDispose() {
            synchronized (this) {
                return shouldDispose;
            }
        }
    }
}
