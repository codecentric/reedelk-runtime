package com.reedelk.esb.configuration;

import com.reedelk.esb.execution.scheduler.SchedulerProvider;
import com.reedelk.runtime.api.configuration.ConfigurationService;

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
