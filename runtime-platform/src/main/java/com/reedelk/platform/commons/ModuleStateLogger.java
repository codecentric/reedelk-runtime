package com.reedelk.platform.commons;

import com.reedelk.platform.module.Module;
import com.reedelk.platform.module.state.ModuleState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

public class ModuleStateLogger {

    private static final Logger logger = LoggerFactory.getLogger(ModuleState.class);

    private ModuleStateLogger() {
    }

    public static void log(Module module) {
        if (logger.isDebugEnabled()) {
            logger.debug(format(
                    "Module=[%s], " +
                            "id=[%d], " +
                            "state=[%s]",
                    module.name(),
                    module.id(),
                    module.state()));
        }
    }
}
