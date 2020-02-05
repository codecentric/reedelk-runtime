package com.reedelk.runtime.api.component;

import com.reedelk.runtime.api.message.Message;

public interface InboundEventListener {

    /**
     * On Event callback to be used when the Inbound processor is not interested
     * in consuming the message payload.
     * @param message the inbound message.
     */
    default void onEvent(Message message) {
    }

    void onEvent(Message message, OnResult callback);

}