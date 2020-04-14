package com.reedelk.platform.configuration;

public class FlowExecutorConfig {

    private final long asyncProcessorTimeout;
    private final SchedulerConfig schedulerConfig;

    FlowExecutorConfig(long asyncProcessorTimeout, SchedulerConfig schedulerConfig) {
        this.asyncProcessorTimeout = asyncProcessorTimeout;
        this.schedulerConfig = schedulerConfig;
    }

    public long asyncProcessorTimeout() {
        return asyncProcessorTimeout;
    }

    public SchedulerConfig schedulerConfig() {
        return schedulerConfig;
    }
}
