package com.reedelk.esb.execution;

import com.reedelk.esb.commons.CorrelationID;
import com.reedelk.runtime.api.flow.Disposable;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributeKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultFlowContext extends ConcurrentHashMap<String, Serializable> implements FlowContext {

    // TODO: Check what happens when the join joins..
    private final List<Disposable> disposableList = new ArrayList<>();

    public static DefaultFlowContext from(Message message) {
        DefaultFlowContext context = new DefaultFlowContext();
        context.put(MessageAttributeKey.CORRELATION_ID, CorrelationID.getOrCreate(message));
        return context;
    }

    private DefaultFlowContext() {
    }

    @Override
    public void register(Disposable disposable) {
        disposableList.add(disposable);
    }

    @Override
    public void dispose() {
        disposableList.forEach(Disposable::dispose);
    }
}
