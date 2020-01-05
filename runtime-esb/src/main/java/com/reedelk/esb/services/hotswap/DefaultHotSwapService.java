package com.reedelk.esb.services.hotswap;

import com.reedelk.runtime.system.api.HotSwapService;
import com.reedelk.runtime.system.api.ModuleNotFoundException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.reedelk.esb.commons.Messages.HotSwap.MODULE_NOT_FOUND;

public class DefaultHotSwapService implements HotSwapService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultHotSwapService.class);

    private final BundleContext context;
    private final HotSwapListener listener;

    public DefaultHotSwapService(BundleContext context, HotSwapListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public long hotSwap(String modulePath, String resourcesRootDirectory) throws ModuleNotFoundException {
        Bundle moduleAtPath = getModuleAtPath(modulePath)
                .orElseThrow(() -> new ModuleNotFoundException(MODULE_NOT_FOUND.format(modulePath)));

        listener.hotSwap(moduleAtPath.getBundleId(), resourcesRootDirectory);

        logger.info("Module [{}] updated", moduleAtPath.getSymbolicName());
        return moduleAtPath.getBundleId();
    }

    private Optional<Bundle> getModuleAtPath(String bundlePath) {
        return Optional.ofNullable(context.getBundle(bundlePath));
    }
}
