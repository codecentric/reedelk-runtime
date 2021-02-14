package de.codecentric.reedelk.runtime.api.message.content;

import de.codecentric.reedelk.runtime.api.commons.StreamUtils;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import org.reactivestreams.Publisher;

import java.util.Arrays;

public class ByteArrayContent implements TypedContent<byte[],byte[]> {

    private final transient Publisher<byte[]> dataAsStream;
    private final Class<byte[]> type = byte[].class;
    private final MimeType mimeType;

    private boolean streamReleased = false;
    private boolean consumed;
    private byte[] data;

    public ByteArrayContent(byte[] data, MimeType mimeType) {
        this.dataAsStream = null;
        this.mimeType = mimeType;
        this.consumed = true;
        this.data = data;
    }

    public ByteArrayContent(Byte[] data, MimeType mimeType) {
        this.dataAsStream = null;
        this.mimeType = mimeType;
        this.consumed = true;
        this.data = toPrimitives(data);
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
    public Class<byte[]> streamType() {
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
                    ", data=" + toPrintableString(data) +
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

    public static byte[] toPrimitives(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];
        for(int i = 0; i < oBytes.length; i++){
            bytes[i] = oBytes[i];
        }
        return bytes;
    }

    // Avoids printing to output the whole byte array.
    private String toPrintableString(byte[] data) {
        if (data == null) return null;
        if (data.length < 6) {
            return Arrays.toString(data);
        } else {
            StringBuilder byteArrayString = new StringBuilder("[");
            for (int i = 0; i < 5; i++) {
                byteArrayString.append(data[i]);
                byteArrayString.append(", ");
            }
            byteArrayString.append("...]");
            return byteArrayString.toString();
        }
    }
}
