package com.reedelk.esb.execution;

import com.reedelk.esb.commons.SerializationUtils;
import com.reedelk.runtime.api.message.FlowContext;
import com.reedelk.runtime.api.message.Message;

import static com.reedelk.runtime.api.commons.Preconditions.checkState;

class MessageAndContext {

    private final FlowContext flowContext;
    private Message message;

    MessageAndContext(Message message, FlowContext flowContext) {
        checkState(message != null, "message");
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
        checkState(message != null, "message");
        this.message = message;
    }

    MessageAndContext copy() {
        Message messageClone = SerializationUtils.clone(message);
        return new MessageAndContext(messageClone, flowContext);
    }
}