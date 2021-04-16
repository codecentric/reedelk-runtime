package de.codecentric.reedelk.platform.execution.context;

import de.codecentric.reedelk.platform.commons.CorrelationID;
import de.codecentric.reedelk.platform.commons.Messages;
import de.codecentric.reedelk.runtime.api.commons.StackTraceUtils;
import de.codecentric.reedelk.runtime.api.flow.Disposable;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageAttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static de.codecentric.reedelk.runtime.api.commons.Preconditions.checkArgument;
import static de.codecentric.reedelk.runtime.api.commons.Preconditions.checkNotNull;
import static de.codecentric.reedelk.runtime.api.commons.StringUtils.isNotBlank;

public class DefaultFlowContext extends SynchronizedMap<String, Object> implements FlowContext {

    private static final Logger logger = LoggerFactory.getLogger(DefaultFlowContext.class);

    private final transient List<Disposable> disposableList = new ArrayList<>();

    public static DefaultFlowContext from(Message message) {
        DefaultFlowContext context = new DefaultFlowContext();
        context.put(MessageAttributeKey.CORRELATION_ID, CorrelationID.getOrCreate(message));
        return context;
    }

    private DefaultFlowContext() {
        super(new HashMap<>());
    }

    @Override
    public Object put(String key, Object value) {
        checkArgument(isNotBlank(key), "flow context key must not be empty");
        return super.put(key, value);
    }

    @Override
    public boolean contains(String key) {
        return super.containsKey(key);
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
