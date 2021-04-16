package de.codecentric.reedelk.platform.flow.deserializer.converter;

import de.codecentric.reedelk.runtime.system.api.SystemProperty;

import static de.codecentric.reedelk.runtime.api.commons.ConfigurationPropertyUtils.asConfigProperty;

enum SystemConfigProperty {

    // A special config property which can be used within component property
    // values to reference the runtime config directory. This can be used to reference
    // files from runtime config folder which might be user defined, e.g ${RUNTIME_CONFIG}/my.ssl.certificate.key.
    RUNTIME_CONFIG {

        final String matchingText = asConfigProperty("RUNTIME_CONFIG");

        @Override
        boolean matches(String input) {
            return input.contains(matchingText);
        }

        @Override
        String replace(String input, SystemProperty systemPropertyService) {
            return input.replace(matchingText, systemPropertyService.configDirectory());
        }
    },

    RUNTIME_HOME {

        final String matchingText = asConfigProperty("RUNTIME_HOME");

        @Override
        boolean matches(String input) {
            return input.contains(matchingText);
        }

        @Override
        String replace(String input, SystemProperty systemPropertyService) {
            return input.replace(matchingText, systemPropertyService.homeDirectory());
        }
    },

    RUNTIME_VERSION {

        final String matchingText = asConfigProperty("RUNTIME_VERSION");

        @Override
        boolean matches(String input) {
            return input.contains(matchingText);
        }

        @Override
        String replace(String input, SystemProperty systemPropertyService) {
            return input.replace(matchingText, systemPropertyService.version());
        }
    },

    RUNTIME_MODULES {

        final String matchingText = asConfigProperty("RUNTIME_MODULES");

        @Override
        boolean matches(String input) {
            return input.contains(matchingText);
        }

        @Override
        String replace(String input, SystemProperty systemPropertyService) {
            return input.replace(matchingText, systemPropertyService.modulesDirectory());
        }
    };


    abstract boolean matches(String input);

    abstract String replace(String input, SystemProperty systemPropertyService);
}
