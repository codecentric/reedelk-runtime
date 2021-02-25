package de.codecentric.reedelk.platform.configuration;

public interface SchedulerConfig {

    boolean isBounded();

    int keepAliveTime();

    int poolMinSize();

    int poolMaxSize();

    int queueSize();

}
