package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.exception.ESBException;

public class EmptyContent implements TypedContent<Void,Void> {

    private final Class<Void> type = Void.class;
    private final MimeType mimeType;

    private final Void payload = null;
    private boolean consumed;
    private boolean streamReleased = false;

    public EmptyContent(MimeType mimeType) {
        this.mimeType = mimeType;
    }

    public EmptyContent() {
        this(null);
    }

    @Override
    public Class<Void> type() {
        return type;
    }

    @Override
    public MimeType mimeType() {
        return mimeType;
    }

    @Override
    public Void data() {
        consumeIfNeeded();
        return payload;
    }

    @Override
    public TypedPublisher<Void> stream() {
        // If it is consumed, we just return the
        // payload as a single item stream.
        if (consumed) {
            // If it is consumed, we know that the state cannot change anymore.
            return TypedMono.emptyVoid();
        }

        // If not consumed, we  must acquire a lock because a concurrent call to
        // the method above might consuming it meanwhile.
        synchronized (this) {
            if (!consumed) {
                if (streamReleased) {
                    throw new ESBException("Stream has been already released. This payload cannot be consumed anymore.");
                }
                streamReleased = true; // the original stream has been released. The original stream cannot be consumed anymore.
                return TypedMono.emptyVoid();
            } else {
                // Meanwhile it has been consumed.
                return TypedMono.emptyVoid();
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
        // nothing to do because it is a null Payload content.
    }

    @Override
    public String toString() {
        return "Empty{" +
                "type=" + type.getName() +
                ", mimeType=" + mimeType +
                '}';
    }

    // Stream can only be consumed once.
    private void consumeIfNeeded() {
        if (!consumed) {
            synchronized (this) {
                if (!consumed) {
                    if (streamReleased) {
                        throw new ESBException("Stream has been already released. This payload cannot be consumed anymore.");
                    }
                    consumed = true;
                }
            }
        }
    }
}