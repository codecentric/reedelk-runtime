package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.exception.PlatformException;
import org.reactivestreams.Publisher;

public class ByteArrayContent implements TypedContent<byte[],byte[]> {

    private final transient Publisher<byte[]> dataAsStream;
    private final Class<byte[]> type = byte[].class;
    private final MimeType mimeType;

    private byte[] data;
    private boolean consumed;
    private boolean streamReleased = false;

    public ByteArrayContent(byte[] data, MimeType mimeType) {
        this.dataAsStream = null;
        this.mimeType = mimeType;
        this.consumed = true;
        this.data = data;
    }

    public ByteArrayContent(Publisher<byte[]> dataAsStream, MimeType mimeType) {
        this.dataAsStream = dataAsStream;
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
        return data;
    }

    @Override
    public TypedPublisher<byte[]> stream() {
        // If it is  consumed, we just return the
        // payload as a single item stream.
        if (consumed) {
            // If it is consumed, we know that the state cannot change anymore.
            return TypedMono.just(data);
        }

        // If not consumed, we  must acquire a lock because a concurrent call to
        // the method above might consuming it meanwhile.
        synchronized (this) {
            if (!consumed) {
                if (streamReleased) {
                    throw new PlatformException("Stream has been already released. This payload cannot be consumed anymore.");
                }
                streamReleased = true; // the original stream has been released. The original stream cannot be consumed anymore.
                return TypedPublisher.fromByteArray(dataAsStream);
            } else {
                // Meanwhile it has been consumed.
                return TypedMono.just(data);
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

    @Override
    public String toString() {
        if (consumed) {
            return "ByteArray{" +
                    "type=" + type.getName() +
                    ", mimeType=" + mimeType +
                    ", consumed=" + consumed +
                    ", streamReleased=" + streamReleased +
                    ", data=" + data +
                    '}';
        } else {
            return "ByteArray{" +
                    "type=" + type.getName() +
                    ", mimeType=" + mimeType +
                    ", consumed=" + consumed +
                    ", streamReleased=" + streamReleased +
                    ", data='" + dataAsStream + '\'' +
                    '}';
        }
    }

    // Stream can only be consumed once.
    private void consumeIfNeeded() {
        if (!consumed) {
            synchronized (this) {
                if (!consumed) {
                    if (streamReleased) {
                        throw new PlatformException("Stream has been already released. This payload cannot be consumed anymore.");
                    }
                    data = StreamUtils.FromByteArray.consume(dataAsStream);
                    consumed = true;
                }
            }
        }
    }
}
