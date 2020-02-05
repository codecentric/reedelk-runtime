package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.exception.ESBException;
import org.reactivestreams.Publisher;

import java.util.Arrays;

public class ByteArrayContent implements TypedContent<byte[],byte[]> {

    private final transient Publisher<byte[]> payloadAsStream;
    private final Class<byte[]> type = byte[].class;
    private final MimeType mimeType;

    private byte[] payload;
    private boolean consumed;
    private boolean streamReleased = false;

    public ByteArrayContent(byte[] payload, MimeType mimeType) {
        this.payloadAsStream = null;
        this.mimeType = mimeType;
        this.payload = payload;
        this.consumed = true;
    }

    public ByteArrayContent(Publisher<byte[]> payloadAsStream, MimeType mimeType) {
        this.payloadAsStream = payloadAsStream;
        this.mimeType = mimeType;
        this.consumed = false;
    }

    @Override
    public Class<byte[]> type() {
        return type;
    }

    @Override
    public MimeType mimeType() {
        return mimeType;
    }

    @Override
    public byte[] data() {
        consumeIfNeeded();
        return payload;
    }

    @Override
    public TypedPublisher<byte[]> stream() {
        // If it is  consumed, we just return the
        // payload as a single item stream.
        if (consumed) {
            // If it is consumed, we know that the state cannot change anymore.
            return TypedMono.just(payload);
        }

        // If not consumed, we  must acquire a lock because a concurrent call to
        // the method above might consuming it meanwhile.
        synchronized (this) {
            if (!consumed) {
                if (streamReleased) {
                    throw new ESBException("Stream has been already released. This payload cannot be consumed anymore.");
                }
                streamReleased = true; // the original stream has been released. The original stream cannot be consumed anymore.
                return TypedPublisher.fromByteArray(payloadAsStream);
            } else {
                // Meanwhile it has been consumed.
                return TypedMono.just(payload);
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

    @Override
    public String toString() {
        if (consumed) {
            return "ByteArray{" +
                    "type=" + type.getName() +
                    ", mimeType=" + mimeType +
                    ", consumed=" + consumed +
                    ", streamReleased=" + streamReleased +
                    ", payload=" + Arrays.toString(payload) +
                    '}';
        } else {
            return "ByteArray{" +
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
                    payload = StreamUtils.FromByteArray.consume(payloadAsStream);
                    consumed = true;
                }
            }
        }
    }
}