package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.message.content.utils.TypedPublisher;

import java.io.Serializable;

public interface TypedContent<T> extends Serializable {

    Class<?> type();

    MimeType mimeType();

    T data();

    TypedPublisher<T> stream(); // the original stream if it is a stream type

    default boolean isStream() {
        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    default boolean isConsumed() {
        return false;
    }

    default void consume() {
    } // consumes the stream payload if it has not been consumed yet
}
