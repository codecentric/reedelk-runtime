package com.reedelk.runtime.api.message.content.factory;

import com.reedelk.runtime.api.message.content.EmptyContent;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedContent;

class EmptyContentStrategy implements Strategy {

    @Override
    public TypedContent<?> from(Object content, MimeType mimeType) {
        return new EmptyContent(mimeType);
    }

    @Override
    public boolean accept(Object content) {
        return content == null;
    }
}
