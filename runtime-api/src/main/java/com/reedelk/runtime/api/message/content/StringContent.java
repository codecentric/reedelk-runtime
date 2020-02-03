package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.message.content.utils.TypedMono;
import com.reedelk.runtime.api.message.content.utils.TypedPublisher;
import org.reactivestreams.Publisher;

public class StringContent implements TypedContent<String> {

    // The payload as stream is transient because we never clone streams.
    // For instance, before executing a 'fork' node, the stream is first
    // resolved before being cloned, hence it does not need to be cloned
    // when this object is cloned.
    private final transient Publisher<String> payloadAsStream;
    private final Class<String> type = String.class;
    private final MimeType mimeType;

    private String payload;
    private boolean consumed;
    private boolean streamReleased = false;

    public StringContent(String payload, MimeType mimeType) {
        this.payloadAsStream = null;
        this.mimeType = mimeType;
        this.payload = payload;
        this.consumed = true;
    }

    public StringContent(Publisher<String> payloadAsStream, MimeType mimeType) {
        this.payloadAsStream = payloadAsStream;
        this.mimeType = mimeType;
        this.consumed = false;
    }

    @Override
    public Class<String> type() {
        return type;
    }

    @Override
    public MimeType mimeType() {
        return mimeType;
    }

    @Override
    public String data() {
        consumeIfNeeded();
        return payload;
    }

    @Override
    public TypedPublisher<String> stream() {
        // If it is consumed, we just return the
        // payload as a single item stream.
        if (consumed) {
            // If it is consumed, we know that the state cannot change anymore.
            return TypedMono.just(payload);
        }

        // If not consumed, we  must acquire a lock because a concurrent call to
        // the method above might consume it meanwhile.
        synchronized (this) {
            if (!consumed) {
                if (streamReleased) {
                    throw new ESBException("Stream has been already released. This payload cannot be consumed anymore.");
                }
                streamReleased = true; // the original stream has been released. The original stream cannot be consumed anymore.
                return TypedPublisher.fromString(payloadAsStream);
            } else {
                // Meanwhile it has been consumed.
                return TypedMono.just(payload);
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

    @Override
    public String toString() {
        if (consumed) {
            return "String{" +
                    "type=" + type.getName() +
                    ", mimeType=" + mimeType +
                    ", consumed=" + consumed +
                    ", streamReleased=" + streamReleased +
                    ", payload='" + payload + '\'' +
                    '}';
        } else {
            return "String{" +
                    "type=" + type.getName() +
                    ", mimeType=" + mimeType +
                    ", consumed=" + consumed +
                    ", streamReleased=" + streamReleased +
                    ", payload='" + payloadAsStream + '\'' +
                    '}';
        }
    }

    // Stream can only be consumed once.
    private void consumeIfNeeded() {
        if (!consumed) {
            synchronized (this) {
                if (!consumed) {
                    if (streamReleased) {
                        throw new ESBException("Stream has been already released. This payload cannot be consumed anymore.");
                    }
                    payload = StreamUtils.FromString.consume(payloadAsStream);
                    consumed = true;
                }
            }
        }
    }
}
