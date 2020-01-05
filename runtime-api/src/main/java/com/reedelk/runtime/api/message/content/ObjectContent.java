package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.message.content.utils.TypedMono;
import com.reedelk.runtime.api.message.content.utils.TypedPublisher;

public class ObjectContent implements TypedContent<Object> {

    private final MimeType mimeType;
    private final Class<?> type;
    private Object payload;

    public ObjectContent(Object payload, MimeType mimeType) {
        this.payload = payload;
        this.mimeType = mimeType;
        this.type = payload.getClass();
    }

    @Override
    public Class<?> type() {
        return type;
    }

    @Override
    public MimeType mimeType() {
        return mimeType;
    }

    @Override
    public Object data() {
        return payload;
    }

    @Override
    public TypedPublisher<Object> stream() {
        return TypedMono.just(payload);
    }

    @Override
    public String toString() {
        return "ObjectContent{" +
                "type=" + type.getName() +
                ", mimeType=" + mimeType +
                ", payload=" + payload +
                '}';
    }
}
