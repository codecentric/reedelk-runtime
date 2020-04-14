package com.reedelk.platform.commons;

import com.reedelk.platform.flow.Flow;
import com.reedelk.runtime.api.commons.StackTraceUtils;
import org.slf4j.Logger;

import static com.reedelk.platform.commons.Messages.Component;
import static com.reedelk.platform.commons.Messages.Flow.*;

public class Log {

    private Log() {
    }

    // Flow

    public static void flowStarted(Logger logger, Flow flow) {
        if (logger.isDebugEnabled()) {
            String message = flow.getFlowTitle()
                    .map(flowTitle -> START_WITH_TITLE.format(flow.getFlowId(), flowTitle))
                    .orElse(START.format(flow.getFlowId()));
            logger.debug(message);
        }
    }

    public static void flowStopped(Logger logger, Flow flow) {
        if (logger.isDebugEnabled()) {
            String message = flow.getFlowTitle()
                    .map(flowTitle -> STOP_WITH_TITLE.format(flow.getFlowId(), flowTitle))
                    .orElse(STOP.format(flow.getFlowId()));
            logger.debug(message);
        }
    }

    public static void flowForceStopException(Logger logger, Flow flow, Exception exception) {
        if (logger.isWarnEnabled()) {
            String rootCauseMessage = StackTraceUtils.rootCauseMessageOf(exception);
            String message = flow.getFlowTitle()
                    .map(flowTitle -> FORCE_STOP_WITH_TITLE.format(flow.getFlowId(), flowTitle, rootCauseMessage))
                    .orElse(FORCE_STOP.format(flow.getFlowId(), rootCauseMessage));
            logger.warn(message, exception);
        }
    }

    // Component

    public static void componentRegistered(Logger logger, String registeredComponent) {
        if (logger.isDebugEnabled()) {
            String message = Component.REGISTERED.format(registeredComponent);
            logger.debug(message);
        }
    }

    public static void componentUnRegistered(Logger logger, String unRegisteredComponent) {
        if (logger.isDebugEnabled()) {
            String message = Component.UN_REGISTERED.format(unRegisteredComponent);
            logger.debug(message);
        }
    }
}
