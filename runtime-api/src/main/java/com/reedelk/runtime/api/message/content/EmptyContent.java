package com.reedelk.runtime.api.message.content;

public class EmptyContent implements TypedContent<Void,Void> {

    private final Class<Void> type = Void.class;
    private final MimeType mimeType;

    private final Void payload = null;

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
    public Class<Void> streamType() {
        return type;
    }

    @Override
    public MimeType mimeType() {
        return mimeType;
    }

    @Override
    public Void data() {
        return payload;
    }

    @Override
    public TypedPublisher<Void> stream() {
        return TypedMono.emptyVoid();
    }

    @Override
    public boolean isStream() {
        return false;
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
}
