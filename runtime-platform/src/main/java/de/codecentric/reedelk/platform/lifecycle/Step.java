package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.module.ModulesManager;
import de.codecentric.reedelk.platform.component.ComponentRegistry;
import de.codecentric.reedelk.runtime.api.configuration.ConfigurationService;
import de.codecentric.reedelk.runtime.system.api.SystemProperty;
import org.osgi.framework.Bundle;

public interface Step<I, O> {

    O run(I input);

    Bundle bundle();

    void bundle(Bundle bundle);

    ModulesManager modulesManager();

    void modulesManager(ModulesManager modulesManager);

    ComponentRegistry componentRegistry();

    void componentRegistry(ComponentRegistry componentRegistry);

    ConfigurationService configurationService();

    void configurationService(ConfigurationService configurationService);

    SystemProperty systemPropertyService();

    void systemPropertyService(SystemProperty systemProperty);
}
