package com.reedelk.runtime.api.message.content.factory;

import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.ObjectContent;
import com.reedelk.runtime.api.message.content.TypedContent;

class ObjectContentStrategy implements Strategy {

    @Override
    public TypedContent<?> from(Object content, MimeType mimeType) {
        return new ObjectContent(content, mimeType);
    }

    @Override
    public boolean accept(Object content) {
        throw new UnsupportedOperationException();
    }
}
