package com.reedelk.runtime.api.message.content.factory;

import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.MultipartContent;
import com.reedelk.runtime.api.message.content.Parts;
import com.reedelk.runtime.api.message.content.TypedContent;

class MultipartContentStrategy implements Strategy {

    @Override
    public TypedContent<?> from(Object content, MimeType mimeType) {
        return new MultipartContent((Parts) content, mimeType);
    }

    @Override
    public boolean accept(Object content) {
        return content instanceof Parts;
    }
}
