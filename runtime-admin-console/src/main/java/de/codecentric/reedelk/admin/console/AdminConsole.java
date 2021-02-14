package de.codecentric.reedelk.admin.console;

import de.codecentric.reedelk.runtime.api.configuration.ConfigurationService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.osgi.service.component.annotations.ServiceScope.SINGLETON;

@Component(service = AdminConsole.class, scope = SINGLETON, immediate = true)
public class AdminConsole {

    private static final Logger logger = LoggerFactory.getLogger(AdminConsole.class);

    // IMPORTANT: Keep them in sync with the values provided in the
    //  AdminConsoleListenerConfig.fconfig file.
    private static final String DEFAULT_ADDRESS = "localhost";
    private static final int DEFAULT_PORT = 9988;

    // NAME_CONVENTION
    private static final String PROPERTY_ADMIN_CONSOLE_ADDRESS = "admin.console.address";
    // NAME_CONVENTION
    private static final String PROPERTY_ADMIN_CONSOLE_PORT = "admin.console.port";

    @Reference
    private ConfigurationService configurationService;

    @Activate
    public void start() {

        String bindAddress = configurationService.getString(PROPERTY_ADMIN_CONSOLE_ADDRESS, DEFAULT_ADDRESS);

        int bindPort = configurationService.getInt(PROPERTY_ADMIN_CONSOLE_PORT, DEFAULT_PORT);

        logger.info(String.format("Admin console listening on http://%s:%d/console", bindAddress, bindPort));
    }
}
