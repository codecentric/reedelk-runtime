package com.reedelk.platform.execution;

import com.reedelk.platform.commons.SerializationUtils;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;

import static com.reedelk.runtime.api.commons.Preconditions.checkArgument;

public class MessageAndContext {

    private final FlowContext flowContext;
    private Message message;

    MessageAndContext(Message message, FlowContext flowContext) {
        checkArgument(message != null, "message");
        checkArgument(flowContext != null, "flowContext");
        this.message = message;
        this.flowContext = flowContext;
    }

    public Message getMessage() {
        return message;
    }

    public FlowContext getFlowContext() {
        return flowContext;
    }

    public void replaceWith(Message message) {
        checkArgument(message != null,
                "Cannot replace current message with a null message: " +
                        "processors are not allowed to return a null message; " +
                        "an empty message should be returned instead.");
        this.message = message;
    }

    public MessageAndContext copy() {
        Message messageClone = SerializationUtils.clone(message);
        return new MessageAndContext(messageClone, flowContext);
    }

    public MessageAndContext copyWithMessage(Message newMessage) {
        return new MessageAndContext(newMessage, flowContext);
    }
}
