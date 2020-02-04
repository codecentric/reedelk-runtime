package com.reedelk.runtime.api.message.content;

import java.io.Serializable;

public interface TypedContent<ItemType, PayloadType> extends Serializable {

    Class<ItemType> type();

    MimeType mimeType();

    PayloadType data();

    TypedPublisher<ItemType> stream(); // the original stream if it is a stream type

    default boolean isStream() {
        return false;
    }

    default boolean isConsumed() {
        return false;
    }

    default void consume() {
    } // consumes the stream payload if it has not been consumed yet
}