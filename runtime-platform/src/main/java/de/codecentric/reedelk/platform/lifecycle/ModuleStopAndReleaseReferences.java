package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.commons.Log;
import de.codecentric.reedelk.platform.commons.Messages;
import de.codecentric.reedelk.platform.module.Module;
import de.codecentric.reedelk.platform.module.state.ModuleState;
import de.codecentric.reedelk.platform.exception.FlowStopException;
import de.codecentric.reedelk.platform.flow.Flow;
import de.codecentric.reedelk.runtime.api.commons.StringUtils;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;

public class ModuleStopAndReleaseReferences extends AbstractStep<Module, Module> {

    private static final Logger logger = LoggerFactory.getLogger(ModuleStopAndReleaseReferences.class);

    @Override
    public Module run(Module module) {

        // This step is applicable only to Started Modules.
        if (module.state() != ModuleState.STARTED) return module;

        Collection<Flow> flows = module.flows();
        Collection<Exception> exceptions = new HashSet<>();
        for (Flow flow : flows) {
            try {
                flow.stopIfStarted();
                Log.flowStopped(logger, flow);
            } catch (Exception exception) {

                String errorMessage = Messages.FlowErrorMessage.DEFAULT.formatWith(module, flow, exception);
                FlowStopException stopException = new FlowStopException(errorMessage, exception);

                if (logger.isErrorEnabled()) {
                    logger.error(StringUtils.EMPTY, stopException);
                }

                exceptions.add(stopException);
            }
        }

        // Transition to STOPPED
        module.stop(flows);

        // Release references (including OSGi service/instances)
        Bundle bundle = bundle();
        flows.forEach(flow -> flow.releaseReferences(bundle));

        if (!exceptions.isEmpty()) {
            // Transition to ERROR
            module.error(exceptions);
        } else {
            // Transition to RESOLVED
            Collection<String> resolvedComponents = module.resolvedComponents();
            module.resolve(resolvedComponents);
        }

        return module;
    }
}
