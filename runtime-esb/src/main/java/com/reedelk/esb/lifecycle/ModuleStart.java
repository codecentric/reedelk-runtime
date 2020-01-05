package com.reedelk.esb.lifecycle;


import com.reedelk.esb.commons.Log;
import com.reedelk.esb.exception.FlowStartException;
import com.reedelk.esb.flow.Flow;
import com.reedelk.esb.module.Module;
import com.reedelk.esb.module.state.ModuleState;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;

import static com.reedelk.esb.commons.Messages.FlowErrorMessage.DEFAULT;
import static com.reedelk.runtime.api.commons.StringUtils.EMPTY;

public class ModuleStart extends AbstractStep<Module, Module> {

    private static final Logger logger = LoggerFactory.getLogger(ModuleStart.class);

    @Override
    public Module run(Module module) {

        if (module.state() != ModuleState.STOPPED) return module;

        Collection<Flow> flows = module.flows();
        Collection<Exception> exceptions = new HashSet<>();

        for (Flow flow : flows) {
            try {

                flow.start();

                Log.flowStarted(logger, flow);

            } catch (Throwable exception) {
                // We must catch all the exception Thrown during flow startup, so that
                // we can property log them and notify the user about the specific error.
                // This is also needed to call "forceStop" method below, so that Clients
                // get a possibility to properly cleanup any resource partially opened
                // during the start phase.
                String errorMessage = DEFAULT.formatWith(module, flow, exception);
                FlowStartException startException = new FlowStartException(errorMessage, exception);

                if (logger.isErrorEnabled()) {
                    logger.error(EMPTY, startException);
                }

                exceptions.add(startException);
            }
        }

        if (!exceptions.isEmpty()) {
            // At least one exception has been thrown while starting one/many flows from the module.
            // Since errors where thrown onStart, flows might have not be completely started.
            // To give a chance to cleanup resources which might have been created during the
            // failed start attempt, we force the flow to stop and then we release any reference.
            flows.forEach(this::forceStop);

            // Release Flow references (including OSGi services)
            Bundle bundle = bundle();
            flows.forEach(flow -> flow.releaseReferences(bundle));

            // Transition to Error state
            module.error(exceptions);

        } else {
            module.start(flows);
        }

        return module;
    }

    private void forceStop(Flow flow) {
        try {
            flow.forceStop();
        } catch (Exception exception) {
            Log.flowForceStopException(logger, flow, exception);
        }
    }
}