package com.reedelk.runtime.properties;

import com.reedelk.runtime.Launcher;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static com.reedelk.runtime.commons.RuntimeMessage.message;

public class SystemConfiguration {

    private static final String HOME_DIR = "com.reedelk.home.dir";
    private static final String CONFIG_DIR_PATH = "com.reedelk.config.dir";
    private static final String CONFIG_LOGS_PATH = "com.reedelk.logs.dir";
    private static final String MODULES_DIR_PATH = "com.reedelk.modules.dir";

    public static String modulesDirectory() {
        String bundleDir = System.getProperty(MODULES_DIR_PATH);
        if (bundleDir != null) {
            return Paths.get(bundleDir).toString();
        } else {
            String homeDirectory = homeDirectory();
            return Paths.get(homeDirectory, "modules").toString();
        }
    }

    public static String configDirectory() {
        String configDir = System.getProperty(CONFIG_DIR_PATH);
        if (configDir != null) {
            return Paths.get(configDir).toString();
        } else {
            String homeDirectory = homeDirectory();
            return Paths.get(homeDirectory, "config").toString();
        }
    }

    public static String logsDirectory() {
        String logsDir = System.getProperty(CONFIG_LOGS_PATH);
        if (logsDir != null) {
            return Paths.get(logsDir).toString();
        } else {
            String homeDirectory = homeDirectory();
            return Paths.get(homeDirectory, "logs").toString();
        }
    }

    static String homeDirectory() {
        String homeDir = System.getProperty(HOME_DIR);
        if (homeDir != null) {
            return Paths.get(homeDir).toString();
        } else {
            try {
                String parent = new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
                File parentFile = new File(parent);
                return parentFile.getParent();
            } catch (URISyntaxException e) {
                String errorMessage = message("directory.home.error");
                throw new RuntimeException(errorMessage, e);
            }
        }
    }
}
