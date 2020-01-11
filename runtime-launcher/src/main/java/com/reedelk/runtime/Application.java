package com.reedelk.runtime;

import com.reedelk.runtime.properties.RuntimeConfiguration;
import com.reedelk.runtime.properties.SystemBundle;
import com.reedelk.runtime.properties.SystemConfiguration;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

import static com.reedelk.runtime.commons.RuntimeMessage.message;

public class Application {

    private final Logger logger = LoggerFactory.getLogger(Application.class);
    private final Framework framework;

    public Application() {
        Map<String, String> frameworkConfiguration = RuntimeConfiguration.get();
        framework = createFramework(frameworkConfiguration);
    }

    void start() throws BundleException {

        framework.init();

        framework.start();

        installSystemBundles();

        logger.info(message("runtime.modules.starting"));

        String autoDeployDirectory = SystemConfiguration.modulesDirectory();

        ModuleInstaller.install(this, autoDeployDirectory);
    }

    public void stop() {
        try {
            if (framework.getState() == Bundle.ACTIVE) {
                logger.info(message("runtime.stopping"));
                framework.stop();
            }
        } catch (BundleException exception) {
            logger.error(message("runtime.stop.error", exception.getMessage()));
        }
    }

    /**
     * Installs all the Bundles from src/main/resources/system
     */
    private void installSystemBundles() {
        for (String bundleName : SystemBundle.all()) {
            installSystemBundle(bundleName);
        }
    }

    public void installAdminConsole() {
        String bundleName = SystemBundle.adminConsole();
        installSystemBundle(bundleName);
    }

    private void installSystemBundle(String bundleName) {
        String bundleResource = SystemBundle.basePath() + bundleName;
        URL bundleResourceURL = SystemBundle.class.getResource(bundleResource);
        if (bundleResourceURL == null) {
            throw new IllegalArgumentException("Could not find bundle named: '" + bundleName + "'");
        }

        try (InputStream bundleInputStream = SystemBundle.class.getResourceAsStream(bundleResource)) {
            install(bundleResourceURL.getFile(), bundleInputStream);
        } catch (IOException exception) {
            String errorMessage = message("runtime.install.system.bundle.error", bundleName);
            throw new ESBRuntimeException(errorMessage, exception);
        }
    }

    public void start(Bundle bundle) {
        try {
            bundle.start();
        } catch (BundleException exception) {
            String errorMessage = message("runtime.start.module.error", bundle.getSymbolicName(), exception.getMessage());
            logger.error(errorMessage);

            try {
                bundle.uninstall();
            } catch (BundleException e) {
                // If an error occurred while starting a module,
                // we must uninstall it. In this case there is nothing
                // we can do to recover from this error:
                // we just log a warning message.
                String uninstallError = message("runtime.start.error.uninstall.error", bundle.getSymbolicName(), exception.getMessage());
                logger.warn(uninstallError);
            }
        }
    }

    public Optional<Bundle> install(File file) {
        try {
            BundleContext context = framework.getBundleContext();
            // A context might be null when we press Ctrl+c and we install a  bundle.
            // As soon as we press Ctrl+c the framework is being destroyed and the context
            // as well, hence it might be null.
            if (context != null) {
                return Optional.of(context.installBundle(file.toURI().toString()));
            } else {
                return Optional.empty();
            }
        } catch (BundleException exception) {
            String errorMessage = message("runtime.install.module.error", file.toString());
            throw new ESBRuntimeException(errorMessage, exception);
        }
    }

    private void install(String bundlePath, InputStream inputStream) {
        if (bundlePath == null) {
            throw new IllegalStateException(message("runtime.install.bundle.path.is.null"));
        }

        BundleContext bundleContext = framework.getBundleContext();
        try {
            Bundle extension = bundleContext.installBundle(bundlePath, inputStream);
            extension.start();
        } catch (BundleException e) {
            throw new RuntimeException(e);
        }
    }

    private Framework createFramework(Map<String, String> config) {
        ServiceLoader<FrameworkFactory> factoryLoader = ServiceLoader.load(FrameworkFactory.class);
        Iterator<FrameworkFactory> iterator = factoryLoader.iterator();
        if (iterator.hasNext()) {
            FrameworkFactory next = iterator.next();
            return next.newFramework(config);
        }
        throw new IllegalStateException(message("runtime.framework.service.load.error"));
    }
}