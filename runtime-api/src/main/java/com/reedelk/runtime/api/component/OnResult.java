package com.reedelk.runtime.api.component;

import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;

public interface OnResult {

    default void onResult(FlowContext flowContext, Message message) {}

    default void onError(FlowContext flowContext, Throwable throwable) {}
}
