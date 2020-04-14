package com.reedelk.platform.configuration;

public class UnboundedSchedulerConfig implements SchedulerConfig {

    private final int keepAliveTime;

    UnboundedSchedulerConfig(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    @Override
    public boolean isBounded() {
        return false;
    }

    @Override
    public int keepAliveTime() {
        return keepAliveTime;
    }

    @Override
    public int poolMinSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int poolMaxSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int queueSize() {
        throw new UnsupportedOperationException();
    }
}
