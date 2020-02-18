package com.reedelk.runtime.api.message.content;

import java.io.Serializable;

public interface TypedContent<ItemType, PayloadType> extends Serializable {

    Class<ItemType> type();

    default Class<ItemType> getType() {
        return type();
    }

    MimeType mimeType();

    default MimeType getMimeType() {
        return mimeType();
    }

    PayloadType data();

    default PayloadType getData() {
        return data();
    }

    TypedPublisher<ItemType> stream();

    default TypedPublisher<ItemType> getStream() {
        return stream();
    }

    boolean isStream();

    boolean isConsumed();

    /**
     * Consumes the stream payload if it has not been consumed yet. This method might be useful
     * to call before cloning the message. E.g the fork component uses this method before
     * invoking fork branches with a copy of the message.
     */
    void consume();
}
