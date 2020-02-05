package com.reedelk.runtime.api.message.content;

import java.io.Serializable;

public interface TypedContent<ItemType, PayloadType> extends Serializable {

    Class<ItemType> type();

    MimeType mimeType();

    PayloadType data();

    TypedPublisher<ItemType> stream();

    boolean isStream();

    boolean isConsumed();

    /**
     * Consumes the stream payload if it has not been consumed yet. This method might be useful
     * to call before cloning the message. E.g the fork component uses this method before
     * invoking fork branches with a copy of the message.
     */
    void consume();
}