package com.reedelk.runtime.api.message.content.factory;

import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedContent;

class KnownTypeContentStrategy extends AbstractStrategy {

    @Override
    public TypedContent<?> from(Object content, MimeType mimeType) {
        Class<? extends TypedContent<?>> typedContentClazz = CLAZZ_TYPED_CONTENT_MAP.get(content.getClass());
        return instantiateContent(typedContentClazz, content.getClass(), content, mimeType);
    }

    @Override
    public boolean accept(Object content) {
        return CLAZZ_TYPED_CONTENT_MAP.containsKey(content.getClass());
    }
}
