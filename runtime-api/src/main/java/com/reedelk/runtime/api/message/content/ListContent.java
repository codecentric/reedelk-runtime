package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.exception.PlatformException;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.List;

public class ListContent<ItemType> implements TypedContent<ItemType, List<ItemType>> {

    private final transient Publisher<ItemType> payloadAsStream;
    private final Class<ItemType> type;
    private final MimeType mimeType;

    private List<ItemType> payload;
    private boolean consumed;
    private boolean streamReleased = false;

    public ListContent(List<ItemType> payload, Class<ItemType> clazz, MimeType mimeType) {
        this.payloadAsStream = null;
        this.mimeType = mimeType;
        this.payload = payload;
        this.type = clazz;
        this.consumed = true;
    }

    public ListContent(Publisher<ItemType> payloadAsStream, Class<ItemType> type, MimeType mimeType) {
        this.type = type;
        this.mimeType = mimeType;
        this.payloadAsStream = payloadAsStream;
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
    public List<ItemType> data() {
        consumeIfNeeded();
        return payload;
    }

    @Override
    public TypedPublisher<ItemType> stream() {
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
                    throw new PlatformException("Stream has been already released. This payload cannot be consumed anymore.");
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
        synchronized (this) {
            return !consumed;
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
                        throw new PlatformException("Stream has been already released. This payload cannot be consumed anymore.");
                    }
                    payload = StreamUtils.FromObject.consume(payloadAsStream);
                    consumed = true;
                }
            }
        }
    }

    @Override
    public String toString() {
        if (consumed) {
            return "ListContent{" +
                    "type=" + type.getName() +
                    ", mimeType=" + mimeType +
                    ", consumed=" + consumed +
                    ", streamReleased=" + streamReleased +
                    ", data=" + payload +
                    '}';
        } else {
            return "ListContent{" +
                    "type=" + type.getName() +
                    ", mimeType=" + mimeType +
                    ", consumed=" + consumed +
                    ", streamReleased=" + streamReleased +
                    ", data=" + payloadAsStream +
                    '}';
        }
    }
}
