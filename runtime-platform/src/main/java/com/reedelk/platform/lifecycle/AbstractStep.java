package com.reedelk.platform.lifecycle;

import com.reedelk.platform.component.ComponentRegistry;
import com.reedelk.platform.exception.ModuleDeserializationException;
import com.reedelk.platform.module.DeSerializedModule;
import com.reedelk.platform.module.Module;
import com.reedelk.platform.module.ModulesManager;
import com.reedelk.runtime.api.commons.StackTraceUtils;
import com.reedelk.runtime.api.configuration.ConfigurationService;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.reedelk.platform.commons.Messages.FlowErrorMessage.DEFAULT;
import static com.reedelk.runtime.api.commons.StringUtils.EMPTY;

public abstract class AbstractStep<I, O> implements Step<I, O> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractStep.class);

    static final Void NOTHING = null;

    private Bundle bundle;
    private ModulesManager modulesManager;
    private ComponentRegistry componentRegistry;
    private ConfigurationService configurationService;

    @Override
    public Bundle bundle() {
        return bundle;
    }

    @Override
    public void bundle(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public ModulesManager modulesManager() {
        return modulesManager;
    }

    @Override
    public void modulesManager(ModulesManager modulesManager) {
        this.modulesManager = modulesManager;
    }

    @Override
    public ComponentRegistry componentRegistry() {
        return componentRegistry;
    }

    @Override
    public void componentRegistry(ComponentRegistry componentRegistry) {
        this.componentRegistry = componentRegistry;
    }

    @Override
    public ConfigurationService configurationService() {
        return configurationService;
    }

    @Override
    public void configurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    /**
     * De-serializes a given module. Note that this function has a side effect
     * on the provided Module! It sets its state to ERROR if a deserialization error
     * occurred.
     * @param module the module to be de-serialized.
     * @return an object containing the de-serialized flows, subflows and configurations.
     */
    protected Optional<DeSerializedModule> deserialize(Module module) {
        try {

            return Optional.of(module.deserialize());

        } catch (Exception exception) {

            String rootCauseMessage = StackTraceUtils.rootCauseMessageOf(exception);
            String errorMessage = DEFAULT.format(module.id(), module.name(), null, null,
                    null, exception.getClass().getName(), rootCauseMessage);

            ModuleDeserializationException moduleDeserializationException =
                    new ModuleDeserializationException(errorMessage, exception);

            if (logger.isErrorEnabled()) {
                logger.error(EMPTY, moduleDeserializationException);
            }

            module.error(moduleDeserializationException);
            return Optional.empty();
        }
    }
}
