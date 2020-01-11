package com.reedelk.runtime;

import com.reedelk.runtime.commons.FileUtils;
import com.reedelk.runtime.license.LicenseVerifierTask;
import com.reedelk.runtime.properties.SystemConfiguration;
import com.reedelk.runtime.properties.Version;
import com.reedelk.runtime.validator.DirectoryExistsValidator;
import com.reedelk.runtime.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.reedelk.runtime.commons.RuntimeMessage.message;
import static com.reedelk.runtime.properties.RuntimeConfiguration.cacheDir;
import static com.reedelk.runtime.properties.SystemConfiguration.configDirectory;
import static com.reedelk.runtime.properties.SystemConfiguration.modulesDirectory;
import static java.util.Arrays.asList;

public class Launcher {

    private static final long SHUTDOWN_TIMEOUT = TimeUnit.SECONDS.toMillis(1);

    private static final Logger logger;

    static {
        /*
         * The Runtime is not aware of the OSGi managed Logger (PAX-Logger).
         * We let logback know to use the same log config file used by OSGi
         * runtime.
         */
        String logbackXmlConfigFile = Paths.get(SystemConfiguration.configDirectory(), "logback.xml").toString();
        System.setProperty("logs.dir", SystemConfiguration.logsDirectory());
        System.setProperty("logback.configurationFile", logbackXmlConfigFile);

        logger = LoggerFactory.getLogger(Application.class);
    }

    public static void main(String[] args) {

        String banner = message("runtime.banner", "  ", " ", " ", " ", " ", " ", Version.getVersion());
        System.out.println(banner);

        long start = System.currentTimeMillis();

        logger.info(message("runtime.starting"));

        Collection<String> errors = validateSystemContext();
        if (!errors.isEmpty()) {
            logger.error(message("runtime.start.error", String.join(",", errors)));
            return;
        }

        Application application = new Application();

        try {

            addShutdownHook(application);

            application.start();

            long end = System.currentTimeMillis();

            float delta = (end - start) / 1000.0f;

            logger.info(message("runtime.started.in", delta));

            LicenseVerifierTask.execute(application);

        } catch (Exception exception) {

            logger.error(message("runtime.start.error", exception.getMessage()));

            System.exit(1);

        }
    }

    private static Collection<String> validateSystemContext() {
        Collection<String> validationErrors = new ArrayList<>();
        List<Validator> validators = asList(
                new DirectoryExistsValidator(modulesDirectory()),
                new DirectoryExistsValidator(configDirectory()));
        validators.forEach(validator -> {
            if (!validator.validate()) {
                validationErrors.add(validator.error());
            }
        });
        return validationErrors;
    }

    private static void addShutdownHook(Application application) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            application.stop();

            try {
                Thread.sleep(SHUTDOWN_TIMEOUT);
            } catch (InterruptedException e) {
                // nothing to do
            }

            // Remove cache directory.
            FileUtils.silentlyRemoveDirectory(cacheDir());

            logger.info(message("runtime.stopped"));
        }));
    }
}
