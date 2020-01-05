package com.reedelk.runtime.api.message.content.factory;

import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedContent;

public interface Strategy {

    TypedContent<?> from(Object content, MimeType mimeType);

    boolean accept(Object content);

}
