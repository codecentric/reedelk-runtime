package com.reedelk.runtime.api.message.content.factory;

import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedContent;
import com.reedelk.runtime.api.message.content.utils.TypedPublisher;

class TypedPublisherStrategy extends AbstractStrategy {

    @Override
    public TypedContent<?> from(Object content, MimeType mimeType) {
        TypedPublisher<?> typedPublisher = (TypedPublisher<?>) content;
        return instantiateStreamContent(typedPublisher, mimeType);
    }

    @Override
    public boolean accept(Object content) {
        return content instanceof TypedPublisher;
    }
}
