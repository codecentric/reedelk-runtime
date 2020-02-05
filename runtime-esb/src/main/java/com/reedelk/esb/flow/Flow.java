package com.reedelk.esb.flow;

import com.reedelk.esb.commons.CorrelationID;
import com.reedelk.esb.execution.FlowExecutorEngine;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.commons.StackTraceUtils;
import com.reedelk.runtime.api.component.Inbound;
import com.reedelk.runtime.api.component.InboundEventListener;
import com.reedelk.runtime.api.component.OnResult;
import com.reedelk.runtime.api.exception.FlowExecutionException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.reedelk.esb.commons.Messages.FlowErrorMessage.DEFAULT;
import static com.reedelk.runtime.api.commons.Preconditions.checkArgument;
import static com.reedelk.runtime.api.commons.Preconditions.checkState;

public class Flow implements InboundEventListener {

    private static final Logger logger = LoggerFactory.getLogger(Flow.class);
    private static final OnResult NO_OP_CALLBACK = new OnResult() {};

    private final long moduleId;
    private final String moduleName;
    private final String flowId;
    private final String flowTitle;

    private final ExecutionGraph executionGraph;
    private final FlowExecutorEngine executionEngine;

    private boolean started = false;

    public Flow(final long moduleId,
                final String moduleName,
                final String flowId,
                final String flowTitle,
                final ExecutionGraph executionGraph,
                final FlowExecutorEngine executionEngine) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.flowId = flowId;
        this.flowTitle = flowTitle;
        this.executionGraph = executionGraph;
        this.executionEngine = executionEngine;
    }

    public String getFlowId() {
        return flowId;
    }

    public Optional<String> getFlowTitle() {
        return Optional.ofNullable(flowTitle);
    }

    public boolean isUsingComponent(String targetComponentName) {
        checkArgument(targetComponentName != null, "Component Name");

        Optional<ExecutionNode> found = executionGraph
                .findOne(executionNode -> executionNode.isUsingComponent(targetComponentName));
        return found.isPresent();
    }

    public void releaseReferences(Bundle bundle) {
        checkState(!isStarted(), "Flow references can be released only when the flow is stopped!");
        executionGraph.applyOnNodes(ReleaseReferenceConsumer.get(bundle));
    }

    public boolean isStarted() {
        synchronized (this) {
            return started;
        }
    }

    public void start() {
        synchronized (this) {
            // If inbound is not present it means that the flow
            // is empty: there are no components in the flow.
            getInbound().ifPresent(inbound -> {
                inbound.addEventListener(Flow.this);
                inbound.onStart();
            });
            // If the inbound component is not present, we still
            // consider the flow as started even though it does not
            // do anything. This is to keep the consistency with the
            // module state which will be started if and only if all
            // flows will be started without any errors.
            started = true;
        }
    }

    public void stopIfStarted() {
        synchronized (this) {
            if (started) {
                forceStop();
            }
        }
    }

    public void forceStop() {
        synchronized (this) {
            try {
                getInbound().ifPresent(inbound -> {
                    inbound.onShutdown();
                    inbound.removeEventListener();
                });
                // On Shutdown might throw an exception.
            } finally {
                started = false;
            }
        }
    }

    @Override
    public void onEvent(Message message) {
        onEvent(message, NO_OP_CALLBACK);
    }

    @Override
    public void onEvent(Message message, OnResult onResult) {
        OnResultFlowExceptionWrapper exceptionWrapper = new OnResultFlowExceptionWrapper(onResult);
        executionEngine.onEvent(message, exceptionWrapper);
    }

    // If inbound is not present it means that the flow
    // is empty: there are no components in the flow.
    private Optional<Inbound> getInbound() {
        return executionGraph.isEmpty() ?
                Optional.empty() :
                Optional.of((Inbound) executionGraph.getRoot().getComponent());
    }

    /**
     * A wrapper which takes the original exception and it adds some contextual information
     * to it, such as module id, module name, flow id and flow title from which the exception
     * was thrown. The wrapper logs the exception as well.
     */
    class OnResultFlowExceptionWrapper implements OnResult {

        private final OnResult delegate;

        OnResultFlowExceptionWrapper(OnResult delegate) {
            this.delegate = delegate;
        }

        @Override
        public void onResult(FlowContext flowContext, Message message) {
            DisposableContextAwareMessage disposableContextAwareMessage =
                    new DisposableContextAwareMessage(flowContext, message);
            delegate.onResult(flowContext, disposableContextAwareMessage);

            // If the inbound processor did not consume the Message content, then it
            // means that the disposable resources (e.g database connections) in the
            // flow context still need to be closed. This might happen when the
            // inbound processor does not use the body of the message (because for
            // instance is only interested in the message attributes) or maybe because
            // it is not needed at all. If this value is false, then it means that the
            // payload has been consumed by the payload and the Disposable Context Aware Message
            // has already scheduled the flow context cleanup. This is necessary because if the
            // message payload is a stream, then the context must be disposed only *after* the stream
            // has been consumed.
            boolean shouldDisposeContext = disposableContextAwareMessage.shouldDispose();
            if (shouldDisposeContext) {
                flowContext.dispose();
            }
        }

        @Override
        public void onError(FlowContext flowContext, Throwable throwable) {
            String correlationId = CorrelationID.getOrNull(flowContext);
            String error = DEFAULT.format(moduleId, moduleName, flowId, flowTitle, correlationId, throwable.getClass().getName(), throwable.getMessage());
            FlowExecutionException wrapped = new FlowExecutionException(moduleId, moduleName, flowId, flowTitle, correlationId, error, throwable);

            if (logger.isErrorEnabled()) {
                logger.error(StackTraceUtils.asString(wrapped));
            }

            delegate.onError(flowContext, wrapped);

            // An exception occurred and therefore the Message will never be consumed.
            // The disposable resources (e.g database connections) in the flow context must be closed.
            flowContext.dispose();
        }
    }
}