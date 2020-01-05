package com.reedelk.runtime.api.message.content.factory;

import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.message.content.*;
import com.reedelk.runtime.api.message.content.utils.TypedPublisher;
import org.reactivestreams.Publisher;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractStrategy implements Strategy {

    static final Map<Class<?>, Class<? extends TypedContent<?>>> CLAZZ_TYPED_CONTENT_MAP;
    static {
        Map<Class<?>, Class<? extends TypedContent<?>>> tmp = new HashMap<>();
        tmp.put(String.class, StringContent.class);
        tmp.put(byte[].class, ByteArrayContent.class);
        CLAZZ_TYPED_CONTENT_MAP = tmp;
    }

    static TypedContent<?> instantiateContent(Class<? extends TypedContent<?>> typedContentClazz, Class<?> contentClazz, Object content, MimeType mimeType) {
        try {
            return typedContentClazz
                    .getDeclaredConstructor(contentClazz, MimeType.class)
                    .newInstance(content, mimeType);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ESBException(e);
        }
    }

    static TypedContent<?> instantiateStreamContent(TypedPublisher<?> typedPublisher, MimeType mimeType) {
        if (CLAZZ_TYPED_CONTENT_MAP.containsKey(typedPublisher.getType())) {
            Class<? extends TypedContent<?>> typedContentClazz = CLAZZ_TYPED_CONTENT_MAP.get(typedPublisher.getType());
            return instantiateStreamContent(typedContentClazz, typedPublisher, mimeType);
        } else {
            return instantiateStreamContent(ObjectContent.class, typedPublisher, mimeType);
        }
    }

    private static TypedContent<?> instantiateStreamContent(Class<? extends TypedContent<?>> typedContentClazz, Publisher<?> publisher, MimeType mimeType) {
        try {
            return typedContentClazz
                    .getDeclaredConstructor(Publisher.class, MimeType.class)
                    .newInstance(publisher, mimeType);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ESBException(e);
        }
    }
}