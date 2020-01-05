package com.reedelk.esb.execution.scheduler;

import com.reedelk.esb.configuration.FlowExecutorConfig;
import com.reedelk.esb.configuration.RuntimeConfigurationProvider;
import com.reedelk.esb.configuration.SchedulerConfig;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.reedelk.runtime.api.commons.Preconditions.checkState;

class FlowScheduler {

    private static final String THREAD_POOL_NAME_PREFIX = "Flow-pool";
    private static final FlowScheduler INSTANCE = new FlowScheduler();

    private Scheduler scheduler;

    private FlowScheduler() {
    }

    private void initialize(SchedulerConfig config) {
        synchronized (INSTANCE) {
            checkState(scheduler == null, "Scheduler already initialized.");

            if (config.isBounded()) {
                BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(config.queueSize());
                ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                        config.poolMinSize(), config.poolMaxSize(), config.keepAliveTime(), TimeUnit.SECONDS,
                        queue, new DefaultThreadFactory(THREAD_POOL_NAME_PREFIX));
                scheduler = Schedulers.fromExecutorService(threadPoolExecutor, THREAD_POOL_NAME_PREFIX);
            } else {
                scheduler = Schedulers.newElastic(THREAD_POOL_NAME_PREFIX, config.keepAliveTime());
            }
        }
    }

    static void initialize() {
        RuntimeConfigurationProvider configProvider = RuntimeConfigurationProvider.get();
        FlowExecutorConfig executorConfig = configProvider.getFlowExecutorConfig();
        SchedulerConfig schedulerConfig = executorConfig.schedulerConfig();
        INSTANCE.initialize(schedulerConfig);
    }

    static Scheduler scheduler() {
        if (INSTANCE.scheduler == null) {
            synchronized (INSTANCE) {
                if (INSTANCE.scheduler == null) {
                    throw new IllegalStateException("Error, scheduler not initialized");
                }
            }
        }
        return INSTANCE.scheduler;
    }
}
