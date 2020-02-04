package com.reedelk.esb.execution;

import com.reedelk.esb.commons.CorrelationID;
import com.reedelk.esb.commons.Messages;
import com.reedelk.runtime.api.commons.StackTraceUtils;
import com.reedelk.runtime.api.flow.Disposable;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.reedelk.runtime.api.commons.Preconditions.checkNotNull;

public class DefaultFlowContext extends ConcurrentHashMap<String, Serializable> implements FlowContext {

    private static final Logger logger = LoggerFactory.getLogger(DefaultFlowContext.class);

    private transient final List<Disposable> disposableList = new ArrayList<>();

    public static DefaultFlowContext from(Message message) {
        DefaultFlowContext context = new DefaultFlowContext();
        context.put(MessageAttributeKey.CORRELATION_ID, CorrelationID.getOrCreate(message));
        return context;
    }

    private DefaultFlowContext() {
    }

    @Override
    public void register(Disposable disposable) {
        checkNotNull(disposable, "disposable object must not be null");
        disposableList.add(disposable);
    }

    @Override
    public void dispose() {
        disposableList.forEach(disposable -> {
            try {
                disposable.dispose();
            } catch (Throwable exception) {
                String errorCause = StackTraceUtils.rootCauseMessageOf(exception);
                String errorMessage = Messages.Execution.ERROR_DISPOSING_OBJECT.format(disposable.getClass(), errorCause);
                logger.warn(errorMessage, exception);
            }
        });
        disposableList.clear();
    }
}
