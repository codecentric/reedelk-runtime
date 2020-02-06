package com.reedelk.esb.flow;

import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedContent;
import com.reedelk.runtime.api.message.content.TypedPublisher;
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

    @Override
    @SuppressWarnings("unchecked")
    public <ItemType, PayloadType, R extends TypedContent<ItemType, PayloadType>> R getContent() {
        return (R) content;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <ItemType, PayloadType, R extends TypedContent<ItemType, PayloadType>> R content() {
        return (R) content;
    }

    @Override
    public <PayloadType> PayloadType payload() {
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

    private static class TypedContentWrapper<ItemType,PayloadType> implements TypedContent<ItemType,PayloadType> {

        private transient final FlowContext context;
        private final TypedContent<ItemType, PayloadType> delegate;

        private boolean shouldDispose = true;

        TypedContentWrapper(TypedContent<ItemType,PayloadType> delegate, FlowContext context) {
            this.context = context;
            this.delegate = delegate;
        }

        @Override
        public Class<ItemType> type() {
            return delegate.type();
        }

        @Override
        public MimeType mimeType() {
            return delegate.mimeType();
        }

        @Override
        public PayloadType data() {
            return delegate.data();
        }

        /**
         * We must dispose the context only *after* (doOnTerminate) the stream
         * has been consumed, otherwise we might close resources (e.g database connections)
         * before they are actually used while consuming the stream.
         * @return a wrapped publisher in which the context is disposed at the end of the stream.
         */
        @Override
        public TypedPublisher<ItemType> stream() {
            synchronized (this) {
                TypedPublisher<ItemType> stream = delegate.stream();
                Flux<ItemType> itemTypeFlux = Flux.from(stream).doOnTerminate(context::dispose);
                TypedPublisher<ItemType> resultStream = TypedPublisher.from(itemTypeFlux, stream.getType());
                shouldDispose = false; // this is because the context is disposed only after the stream has been consumed (doOnTerminate).
                return resultStream;
            }
        }

        @Override
        public boolean isStream() {
            return delegate.isStream();
        }

        @Override
        public boolean isConsumed() {
            return delegate.isConsumed();
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