package com.reedelk.esb.configuration;

public interface SchedulerConfig {

    boolean isBounded();

    int keepAliveTime();

    int poolMinSize();

    int poolMaxSize();

    int queueSize();

}
