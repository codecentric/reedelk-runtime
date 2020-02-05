package com.reedelk.runtime.api.component;

import com.reedelk.runtime.api.message.Message;

public abstract class AbstractInbound implements Inbound, InboundEventListener {

    private InboundEventListener listener;

    @Override
    public void onEvent(Message message) {
        if (listener == null) {
            throw new IllegalStateException("Event listener was not registered!");
        }
        listener.onEvent(message);
    }

    @Override
    public void onEvent(Message message, OnResult onResult) {
        if (listener == null) {
            throw new IllegalStateException("Event listener was not registered!");
        }
        listener.onEvent(message, onResult);
    }

    @Override
    public void addEventListener(InboundEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void removeEventListener() {
        this.listener = null;
    }
}