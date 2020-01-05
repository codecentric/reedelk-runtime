package com.reedelk.runtime.commons;

import com.reedelk.runtime.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.reedelk.runtime.commons.RuntimeMessage.message;

public abstract class ApplicationTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(ApplicationTask.class);

    protected final Application application;
    private final long startDelay = 500;

    protected ApplicationTask(Application application) {
        this.application = application;
    }

    @Override
    public void run() {
        try {
            try {
                Thread.sleep(startDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (!Thread.currentThread().isInterrupted()) {
                doRun();
            }

        } catch (Exception e) {
            logger.error(message("runtime.shutdown.error", e.getMessage()));
            application.stop();
        }
    }

    protected abstract void doRun() throws Exception;
}
