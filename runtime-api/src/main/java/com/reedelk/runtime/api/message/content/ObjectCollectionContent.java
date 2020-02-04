package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.exception.ESBException;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.Collection;

public class ObjectCollectionContent<T> implements TypedContent<T, Collection<T>> {

    private final transient Publisher<T> payloadAsStream;
    private final Class<T> type;
    private final MimeType mimeType;

    private Collection<T> payload;
    private boolean consumed;
    private boolean streamReleased = false;

    public ObjectCollectionContent(Collection<T> payload, Class<T> clazz, MimeType mimeType) {
        this.payloadAsStream = null;
        this.mimeType = mimeType;
        this.payload = payload;
        this.type = clazz;
        this.consumed = true;
    }

    public ObjectCollectionContent(Publisher<T> payloadAsStream, Class<T> type, MimeType mimeType) {
        this.type = type;
        this.payloadAsStream = payloadAsStream;
        this.mimeType = mimeType;
        this.consumed = false;
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public MimeType mimeType() {
        return mimeType;
    }

    @Override
    public Collection<T> data() {
        consumeIfNeeded();
        return payload;
    }

    @Override
    public TypedPublisher<T> stream() {
        // If it is consumed, we just return the
        // payload as a single item stream.
        if (consumed) {
            // If it is consumed, we know that the state cannot change anymore.
            // Convert back list to stream.
            return TypedPublisher.from(Flux.fromStream(payload.stream()), type);
        }

        // If not consumed, we  must acquire a lock because a concurrent call to
        // the method above might consume it meanwhile.
        synchronized (this) {
            if (!consumed) {
                if (streamReleased) {
                    throw new ESBException("Stream has been already released. This payload cannot be consumed anymore.");
                }
                streamReleased = true; // the original stream has been released. The original stream cannot be consumed anymore.
                return TypedPublisher.fromObject(payloadAsStream, type);
            } else {
                // Meanwhile it has been consumed.
                // Convert back list to stream.
                return TypedPublisher.from(Flux.fromStream(payload.stream()), type);
            }
        }
    }

    @Override
    public boolean isStream() {
        return !consumed;
    }

    @Override
    public boolean isConsumed() {
        return consumed;
    }

    @Override
    public void consume() {
        consumeIfNeeded();
    }

    // Stream can only be consumed once.
    private void consumeIfNeeded() {
        if (!consumed) {
            synchronized (this) {
                if (!consumed) {
                    if (streamReleased) {
                        throw new ESBException("Stream has been already released. This payload cannot be consumed anymore.");
                    }
                    payload = StreamUtils.FromObject.consume(payloadAsStream);
                    consumed = true;
                }
            }
        }
    }

    // TODO: Fix to string
}