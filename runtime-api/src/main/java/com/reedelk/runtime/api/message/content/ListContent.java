package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.exception.PlatformException;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.List;

@SuppressWarnings("rawtypes")
public class ListContent<StreamType> implements TypedContent<List, StreamType> {

    private final MimeType mimeType = MimeType.APPLICATION_JAVA;
    private final transient Publisher<StreamType> payloadAsStream;
    private final Class<StreamType> streamType;

    private List<StreamType> payload;
    private boolean consumed;
    private boolean streamReleased = false;

    public ListContent(List<StreamType> payload, Class<StreamType> streamType) {
        this.payloadAsStream = null;
        this.streamType = streamType;
        this.payload = payload;
        this.consumed = true;
    }

    public ListContent(Publisher<StreamType> payloadAsStream, Class<StreamType> streamType) {
        this.streamType = streamType;
        this.payloadAsStream = payloadAsStream;
        this.consumed = false;
    }

    @Override
    public Class<List> type() {
        return List.class;
    }

    @Override
    public Class<StreamType> streamType() {
        return streamType;
    }

    @Override
    public MimeType mimeType() {
        return mimeType;
    }

    @Override
    public List<StreamType> data() {
        consumeIfNeeded();
        return payload;
    }

    @Override
    public TypedPublisher<StreamType> stream() {
        // If it is consumed, we just return the
        // payload as a single item stream.
        if (consumed) {
            // If it is consumed, we know that the state cannot change anymore.
            // Convert back list to stream.
            return TypedPublisher.from(Flux.fromStream(payload.stream()), streamType);
        }

        // If not consumed, we  must acquire a lock because a concurrent call to
        // the method above might consume it meanwhile.
        synchronized (this) {
            if (!consumed) {
                if (streamReleased) {
                    throw new PlatformException("Stream has been already released. This payload cannot be consumed anymore.");
                }
                streamReleased = true; // the original stream has been released. The original stream cannot be consumed anymore.
                return TypedPublisher.fromObject(payloadAsStream, streamType);
            } else {
                // Meanwhile it has been consumed.
                // Convert back list to stream.
                return TypedPublisher.from(Flux.fromStream(payload.stream()), streamType);
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
                    "type=" + streamType.getName() +
                    ", mimeType=" + mimeType +
                    ", consumed=" + consumed +
                    ", streamReleased=" + streamReleased +
                    ", data=" + payload +
                    '}';
        } else {
            return "ListContent{" +
                    "type=" + streamType.getName() +
                    ", mimeType=" + mimeType +
                    ", consumed=" + consumed +
                    ", streamReleased=" + streamReleased +
                    ", data=" + payloadAsStream +
                    '}';
        }
    }
}
