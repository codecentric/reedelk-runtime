package com.reedelk.runtime.api.component;

import com.reedelk.runtime.api.message.FlowContext;
import com.reedelk.runtime.api.message.Message;

public interface OnResult {

    default void onResult(Message message, FlowContext flowContext) {}

    default void onError(Throwable throwable, FlowContext flowContext) {}
}
