package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.exception.PlatformException;
import reactor.core.publisher.Mono;

import static com.reedelk.runtime.api.commons.Preconditions.checkNotNull;
import static com.reedelk.runtime.api.commons.Preconditions.checkState;

public class ObjectContent<ItemType> implements TypedContent<ItemType, ItemType> {

    private final MimeType mimeType = MimeType.APPLICATION_JAVA;
    // The data as stream is transient because we never clone streams.
    // For components using this content (e.g Fork) the content it is first
    // resolved before being cloned.
    private final transient Mono<ItemType> dataAsStream;
    private final Class<ItemType> type;

    private ItemType data;
    private boolean consumed;
    private boolean streamReleased = false;

    @SuppressWarnings("unchecked")
    public ObjectContent(ItemType data) {
        checkNotNull(data,"Cannot create object content with null data; use empty content instead");
        checkIsAcceptedTypeOrThrow(data.getClass());

        this.data = data;
        this.type = (Class<ItemType>) data.getClass();
        this.dataAsStream = null;
        this.consumed = true;
    }

    public ObjectContent(Mono<ItemType> monoStream, Class<ItemType> type) {
        checkNotNull(monoStream,"Cannot create object content with null data; use empty content instead");
        checkIsAcceptedTypeOrThrow(type);
        this.type = type;
        this.dataAsStream = monoStream;
        this.consumed = false;
    }

    @Override
    public Class<ItemType> type() {
        return type;
    }

    @Override
    public Class<ItemType> streamType() {
        return type;
    }

    @Override
    public MimeType mimeType() {
        return mimeType;
    }

    @Override
    public ItemType data() {
        consumeIfNeeded();
        return data;
    }

    // Single element stream: it is a single element stream.
    @Override
    public TypedPublisher<ItemType> stream() {
        // If it is consumed, we just return the data as a single item stream.
        if (consumed) {
            // If it is consumed, we know that the state cannot change anymore.
            return TypedMono.just(data, type);
        }

        // If not consumed, we  must acquire a lock because a concurrent call to
        // the method above might consuming it meanwhile.
        synchronized (this) {
            if (!consumed) {
                if (streamReleased) {
                    throw new PlatformException("Stream has been already released. This data cannot be consumed anymore.");
                }
                streamReleased = true; // the original stream has been released. The original stream cannot be consumed anymore.
                return TypedMono.from(dataAsStream, type);
            } else {
                // Meanwhile it has been consumed.
                return TypedMono.just(data, type);
            }
        }
    }

    @Override
    public boolean isStream() {
        // The ObjectContent might be a Mono stream. We don't consider it a 'stream'
        // because it is just a stream with a single element in it.
        // The use case is when we have attachments to be sent with the REST client.
        // In that case the data contains Map<String, Attachment> and when the REST client
        // checks if it is stream-able or not this method would return correctly false.
        // The REST listener would set MessageBuilder.get().withJavaObject(partsMono, Attachments.class, request.mimeType());
        // when a Multipart REST request is being made.
        return false;
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
                        throw new PlatformException("Stream has been already released. This data cannot be consumed anymore.");
                    }
                    data = dataAsStream.block();
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
                    ", data=" + data +
                    '}';
        } else {
            return "ObjectContent{" +
                    "type=" + type.getName() +
                    ", mimeType=" + mimeType +
                    ", consumed=" + consumed +
                    ", streamReleased=" + streamReleased +
                    ", data=" + dataAsStream +
                    '}';
        }
    }

    private void checkIsAcceptedTypeOrThrow(Class<?> dataTypeClass) {
        checkState(!String.class.equals(dataTypeClass),
                "Cannot create object content with String type; " +
                        "use MessageBuilder.withString(..) or create a new StringContent type.");
        checkState(!Byte.class.equals(dataTypeClass),
                "Cannot create object content with byte array type; " +
                        "use MessageBuilder.withBinary(..) or create a new ByteArrayContent type.");
        checkState(!byte.class.equals(dataTypeClass),
                "Cannot create object content with byte array type; " +
                        "use MessageBuilder.withBinary(..) or create a new ByteArrayContent type.");
    }
}
