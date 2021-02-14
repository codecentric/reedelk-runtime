package de.codecentric.reedelk.runtime;

import de.codecentric.reedelk.runtime.adminconsole.AdminConsoleInstallTask;
import de.codecentric.reedelk.runtime.commons.FileUtils;
import de.codecentric.reedelk.runtime.commons.RuntimeMessage;
import de.codecentric.reedelk.runtime.properties.RuntimeConfiguration;
import de.codecentric.reedelk.runtime.properties.SystemConfiguration;
import de.codecentric.reedelk.runtime.properties.Version;
import de.codecentric.reedelk.runtime.validator.DirectoryExistsValidator;
import de.codecentric.reedelk.runtime.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

        String banner = RuntimeMessage.message("runtime.banner", "  ", " ", " ", " ", " ", " ", Version.getQualifier(), Version.getVersion());
        System.out.println(banner);

        long start = System.currentTimeMillis();

        logger.info(RuntimeMessage.message("runtime.starting"));

        Collection<String> errors = validateSystemContext();
        if (!errors.isEmpty()) {
            logger.error(RuntimeMessage.message("runtime.start.error", String.join(",", errors)));
            return;
        }

        Application application = new Application();

        try {

            addShutdownHook(application);

            application.start();

            long end = System.currentTimeMillis();

            float delta = (end - start) / 1000.0f;

            logger.info(RuntimeMessage.message("runtime.started.in", delta));

            // Launch the Admin console.
            AdminConsoleInstallTask.execute(application);

        } catch (Exception exception) {

            logger.error(RuntimeMessage.message("runtime.start.error", exception.getMessage()));

            System.exit(1);

        }
    }

    private static Collection<String> validateSystemContext() {
        Collection<String> validationErrors = new ArrayList<>();
        List<Validator> validators = asList(
                new DirectoryExistsValidator(SystemConfiguration.modulesDirectory()),
                new DirectoryExistsValidator(SystemConfiguration.configDirectory()));
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
            FileUtils.silentlyRemoveDirectory(RuntimeConfiguration.cacheDir());

            logger.info(RuntimeMessage.message("runtime.stopped"));
        }));
    }
}
