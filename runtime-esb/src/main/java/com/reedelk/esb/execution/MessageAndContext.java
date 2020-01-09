package com.reedelk.esb.execution;

import com.reedelk.esb.commons.SerializationUtils;
import com.reedelk.runtime.api.message.FlowContext;
import com.reedelk.runtime.api.message.Message;

import static com.reedelk.runtime.api.commons.Preconditions.checkArgument;

class MessageAndContext {

    private final FlowContext flowContext;
    private Message message;

    MessageAndContext(Message message, FlowContext flowContext) {
        checkArgument(message != null, "message");
        checkArgument(flowContext != null, "flowContext");
        this.message = message;
        this.flowContext = flowContext;
    }

    Message getMessage() {
        return message;
    }

    public FlowContext getFlowContext() {
        return flowContext;
    }

    void replaceWith(Message message) {
        checkArgument(message != null,
                "Cannot replace current message with a null message: " +
                        "processors are not allowed to return a null message; " +
                        "an empty message should be returned instead.");
        this.message = message;
    }

    MessageAndContext copy() {
        Message messageClone = SerializationUtils.clone(message);
        return new MessageAndContext(messageClone, flowContext);
    }
}