package de.codecentric.reedelk.platform.configuration;

import de.codecentric.reedelk.runtime.api.configuration.ConfigurationService;

import static de.codecentric.reedelk.runtime.api.commons.Preconditions.checkState;

public class RuntimeConfigurationProvider {

    private static final RuntimeConfigurationProvider PROVIDER = new RuntimeConfigurationProvider();

    private ConfigurationService configService;
    private FlowExecutorConfig executorConfig;

    private RuntimeConfigurationProvider() {
    }

    private void init(ConfigurationService configService) {
        synchronized (PROVIDER) {
            checkState(this.configService == null, "Config service already initialized");
            this.configService = configService;
            loadProperties();
        }
    }

    public static RuntimeConfigurationProvider get() {
        return PROVIDER;
    }

    public FlowExecutorConfig getFlowExecutorConfig() {
        return executorConfig;
    }

    static void initialize(ConfigurationService configService) {
        PROVIDER.init(configService);
    }

    private void loadProperties() {
        boolean isUnbounded = configService.getBoolean("executor.scheduler.flow.unbounded", true);

        SchedulerConfig schedulerConfig;
        if (isUnbounded) {
            int keepAliveTimeSeconds = configService.getInt("executor.scheduler.flow.unbounded.keep.alive.time", 60);
            schedulerConfig = new UnboundedSchedulerConfig(keepAliveTimeSeconds);

        } else {
            int poolMinSize = configService.getInt("executor.scheduler.flow.bounded.min.pool.size", 1);
            int poolMaxSize = configService.getInt("executor.scheduler.flow.bounded.max.pool.size", 30);
            int keepAliveTimeSeconds = configService.getInt("executor.scheduler.flow.bounded.keep.alive.time", 60);
            int queueSize = configService.getInt("executor.scheduler.flow.bounded.queue.size", 200);
            schedulerConfig = new BoundedSchedulerConfig(poolMinSize, poolMaxSize, keepAliveTimeSeconds, queueSize);
        }

        long asyncProcessorTimeoutMillis = configService.getLong("executor.scheduler.flow.async.processor.timeout", 120000);

        executorConfig = new FlowExecutorConfig(asyncProcessorTimeoutMillis, schedulerConfig);
    }
}
