package com.reedelk.esb.execution;

import com.reedelk.esb.commons.CorrelationID;
import com.reedelk.runtime.api.message.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributeKey;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultFlowContext extends ConcurrentHashMap<String, Serializable> implements FlowContext {

    public static DefaultFlowContext from(Message message) {
        DefaultFlowContext context = new DefaultFlowContext();
        context.put(MessageAttributeKey.CORRELATION_ID, CorrelationID.getOrCreate(message));
        return context;
    }

    private DefaultFlowContext() {
    }
}
