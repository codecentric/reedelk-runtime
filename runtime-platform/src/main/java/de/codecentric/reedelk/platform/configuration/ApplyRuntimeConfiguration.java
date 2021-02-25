package de.codecentric.reedelk.platform.configuration;

import de.codecentric.reedelk.platform.execution.scheduler.SchedulerProvider;
import de.codecentric.reedelk.runtime.api.configuration.ConfigurationService;

/**
 * Applies the properties from the runtime configuration file. For example
 * it initializes the Flow Executor Schedulers with the provided config values.
 */
public class ApplyRuntimeConfiguration {

    private ApplyRuntimeConfiguration() {
    }

    public static void from(ConfigurationService configurationService) {
        RuntimeConfigurationProvider.initialize(configurationService);
        SchedulerProvider.initialize();
    }
}
