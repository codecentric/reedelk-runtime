package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.exception.ESBException;
import reactor.core.publisher.Mono;

import static com.reedelk.runtime.api.commons.Preconditions.checkNotNull;

public class ObjectContent<ItemType> implements TypedContent<ItemType, ItemType> {

    // The payload as stream is transient because we never clone streams.
    // For components using this content (e.g Fork) the content it is first
    // resolved before being cloned.
    private final transient Mono<ItemType> payloadAsStream;
    private final Class<ItemType> type;
    private final MimeType mimeType;

    private ItemType payload;
    private boolean consumed;
    private boolean streamReleased = false;

    @SuppressWarnings("unchecked")
    public ObjectContent(ItemType payload, MimeType mimeType) {
        checkNotNull(payload,
                "Cannot create object content with null payload; use empty content instead");
        this.payload = payload;
        this.mimeType = mimeType;
        this.type = (Class<ItemType>) payload.getClass();
        this.payloadAsStream = null;
        this.consumed = true;
    }

    public ObjectContent(Mono<ItemType> monoStream, Class<ItemType> type, MimeType mimeType) {
        this.type = type;
        this.mimeType = mimeType;
        this.payloadAsStream = monoStream;
        this.consumed = false;
    }

    @Override
    public Class<ItemType> type() {
        return type;
    }

    @Override
    public MimeType mimeType() {
        return mimeType;
    }

    @Override
    public ItemType data() {
        consumeIfNeeded();
        return payload;
    }

    // Single element stream.
    @Override
    public TypedPublisher<ItemType> stream() {
        // If it is consumed, we just return the
        // payload as a single item stream.
        if (consumed) {
            // If it is consumed, we know that the state cannot change anymore.
            return TypedMono.just(payload, type);
        }

        // If not consumed, we  must acquire a lock because a concurrent call to
        // the method above might consuming it meanwhile.
        synchronized (this) {
            if (!consumed) {
                if (streamReleased) {
                    throw new ESBException("Stream has been already released. This payload cannot be consumed anymore.");
                }
                streamReleased = true; // the original stream has been released. The original stream cannot be consumed anymore.
                return TypedMono.from(payloadAsStream, type);
            } else {
                // Meanwhile it has been consumed.
                return TypedMono.just(payload, type);
            }
        }
    }

    @Override
    public boolean isStream() {
        synchronized (this) {
            return !consumed;
        }
    }

    @Override
    public boolean isConsumed() {
        synchronized (this) {
            return consumed;
        }
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
                    payload = payloadAsStream.block();
                    consumed = true;
                }
            }
        }
    }

    @Override
    public String toString() {
        if (consumed) {
            return "ObjectContent{" +
                    "type=" + type.getName() +
                    ", mimeType=" + mimeType +
                    ", consumed=" + consumed +
                    ", streamReleased=" + streamReleased +
                    ", payload=" + payload +
                    '}';
        } else {
            return "ObjectContent{" +
                    "type=" + type.getName() +
                    ", mimeType=" + mimeType +
                    ", consumed=" + consumed +
                    ", streamReleased=" + streamReleased +
                    ", payload=" + payloadAsStream +
                    '}';
        }
    }
}