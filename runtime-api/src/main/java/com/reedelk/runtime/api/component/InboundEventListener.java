package com.reedelk.runtime.api.component;

import com.reedelk.runtime.api.message.Message;

public interface InboundEventListener {

    void onEvent(Message message, OnResult callback);

}
