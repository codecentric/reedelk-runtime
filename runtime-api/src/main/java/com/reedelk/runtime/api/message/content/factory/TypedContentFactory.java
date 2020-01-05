package com.reedelk.runtime.api.message.content.factory;

import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedContent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TypedContentFactory {

    private static final Strategy DEFAULT = new ObjectContentStrategy();
    private static final List<Strategy> STRATEGIES = Collections.unmodifiableList(Arrays.asList(
            new EmptyContentStrategy(),
            new TypedContentStrategy(),
            new TypedPublisherStrategy(),
            new MultipartContentStrategy(),
            new KnownTypeContentStrategy()));

    public static TypedContent<?> from(Object content, MimeType mimeType) {
        return STRATEGIES.stream()
                .filter(strategy -> strategy.accept(content)).findFirst()
                .orElse(DEFAULT)
                .from(content, mimeType);
    }
}
