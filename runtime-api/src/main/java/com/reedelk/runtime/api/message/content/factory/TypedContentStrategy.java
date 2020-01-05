package com.reedelk.runtime.api.message.content.factory;

import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedContent;

class TypedContentStrategy implements Strategy {

    @Override
    public TypedContent<?> from(Object content, MimeType mimeType) {
        return (TypedContent<?>) content;
    }

    @Override
    public boolean accept(Object content) {
        return content instanceof TypedContent;
    }
}
