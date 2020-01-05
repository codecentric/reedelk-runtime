package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.message.content.utils.TypedMono;
import com.reedelk.runtime.api.message.content.utils.TypedPublisher;
import org.reactivestreams.Publisher;

import static com.reedelk.runtime.api.message.content.MimeType.MULTIPART_FORM_DATA;
import static java.lang.String.format;

public class MultipartContent implements TypedContent<Parts> {

    private final transient Publisher<Parts> payloadAsStream;
    private final Class<Parts> type = Parts.class;
    private final MimeType mimeType;
    private Parts payload;

    private boolean consumed;
    private boolean streamReleased = false;

    public MultipartContent(Parts payload, MimeType mimeType) {
        checkSupportedMimeTypeOrThrow(mimeType);
        this.payloadAsStream = null;
        this.mimeType = mimeType;
        this.payload = payload;
        this.consumed = true;
    }

    public MultipartContent(Publisher<Parts> payloadAsStream, MimeType mimeType) {
        checkSupportedMimeTypeOrThrow(mimeType);
        this.payloadAsStream = payloadAsStream;
        this.mimeType = mimeType;
        this.consumed = false;
    }

    @Override
    public Class<Parts> type() {
        return type;
    }

    @Override
    public MimeType mimeType() {
        return mimeType;
    }

    @Override
    public Parts data() {
        consumeIfNeeded();
        return payload;
    }

    @Override
    public TypedPublisher<Parts> stream() {
        // If it is consumed, we just return the
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
                return TypedPublisher.fromParts(payloadAsStream);
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
            return "Multipart{" +
                    "type=" + type.getName() +
                    ", mimeType=" + mimeType +
                    ", consumed=" + consumed +
                    ", streamReleased=" + streamReleased +
                    ", payload=" + payload +
                    '}';
        } else {
            return "Multipart{" +
                    "type=" + type.getName() +
                    ", mimeType=" + mimeType +
                    ", consumed=" + consumed +
                    ", streamReleased=" + streamReleased +
                    ", payload=" + payloadAsStream +
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
                    payload = StreamUtils.FromParts.consume(payloadAsStream);
                    consumed = true;
                }
            }
        }
    }

    private void checkSupportedMimeTypeOrThrow(MimeType mimeType) {
        if (!MULTIPART_FORM_DATA.equals(mimeType)) {
            throw new IllegalArgumentException(format("MultipartContent supports only 'multipart/form-data' mime type. Given mime type was '%s'", mimeType));
        }
    }
}
