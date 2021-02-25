package de.codecentric.reedelk.runtime.api.component;

import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;

public interface OnResult {

    default void onResult(FlowContext flowContext, Message message) {}

    default void onError(FlowContext flowContext, Throwable throwable) {}
}
