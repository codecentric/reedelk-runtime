package com.reedelk.runtime.api.message.content;

public class EmptyContent implements TypedContent<Void,Void> {

    private final Class<Void> type = Void.class;
    private final MimeType mimeType;

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
        return null;
    }

    @Override
    public TypedPublisher<Void> stream() {
        return TypedMono.emptyVoid();
    }

    @Override
    public String toString() {
        return "Empty{" +
                "type=" + type.getName() +
                ", mimeType=" + mimeType +
                '}';
    }
}