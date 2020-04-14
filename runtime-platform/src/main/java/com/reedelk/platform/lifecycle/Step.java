package com.reedelk.platform.lifecycle;

import com.reedelk.platform.component.ComponentRegistry;
import com.reedelk.platform.module.ModulesManager;
import com.reedelk.runtime.api.configuration.ConfigurationService;
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
}
