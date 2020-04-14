package com.reedelk.platform.configuration;

public class BoundedSchedulerConfig implements SchedulerConfig {

    private final int queueSize;
    private final int poolMinSize;
    private final int poolMaxSize;
    private final int keepAliveTime;

    BoundedSchedulerConfig(int poolMinSize, int poolMaxSize, int keepAliveTime, int queueSize) {
        this.queueSize = queueSize;
        this.poolMinSize = poolMinSize;
        this.poolMaxSize = poolMaxSize;
        this.keepAliveTime = keepAliveTime;
    }

    @Override
    public boolean isBounded() {
        return true;
    }

    @Override
    public int keepAliveTime() {
        return keepAliveTime;
    }

    @Override
    public int poolMinSize() {
        return poolMinSize;
    }

    @Override
    public int poolMaxSize() {
        return poolMaxSize;
    }

    @Override
    public int queueSize() {
        return queueSize;
    }
}
