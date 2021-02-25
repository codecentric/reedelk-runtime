package de.codecentric.reedelk.platform.commons;

import de.codecentric.reedelk.platform.flow.Flow;
import de.codecentric.reedelk.runtime.api.commons.StackTraceUtils;
import org.slf4j.Logger;

public class Log {

    private Log() {
    }

    // Flow

    public static void flowStarted(Logger logger, Flow flow) {
        if (logger.isDebugEnabled()) {
            String message = flow.getFlowTitle()
                    .map(flowTitle -> Messages.Flow.START_WITH_TITLE.format(flow.getFlowId(), flowTitle))
                    .orElse(Messages.Flow.START.format(flow.getFlowId()));
            logger.debug(message);
        }
    }

    public static void flowStopped(Logger logger, Flow flow) {
        if (logger.isDebugEnabled()) {
            String message = flow.getFlowTitle()
                    .map(flowTitle -> Messages.Flow.STOP_WITH_TITLE.format(flow.getFlowId(), flowTitle))
                    .orElse(Messages.Flow.STOP.format(flow.getFlowId()));
            logger.debug(message);
        }
    }

    public static void flowForceStopException(Logger logger, Flow flow, Exception exception) {
        if (logger.isWarnEnabled()) {
            String rootCauseMessage = StackTraceUtils.rootCauseMessageOf(exception);
            String message = flow.getFlowTitle()
                    .map(flowTitle -> Messages.Flow.FORCE_STOP_WITH_TITLE.format(flow.getFlowId(), flowTitle, rootCauseMessage))
                    .orElse(Messages.Flow.FORCE_STOP.format(flow.getFlowId(), rootCauseMessage));
            logger.warn(message, exception);
        }
    }

    // Component

    public static void componentRegistered(Logger logger, String registeredComponent) {
        if (logger.isDebugEnabled()) {
            String message = Messages.Component.REGISTERED.format(registeredComponent);
            logger.debug(message);
        }
    }

    public static void componentUnRegistered(Logger logger, String unRegisteredComponent) {
        if (logger.isDebugEnabled()) {
            String message = Messages.Component.UN_REGISTERED.format(unRegisteredComponent);
            logger.debug(message);
        }
    }
}
