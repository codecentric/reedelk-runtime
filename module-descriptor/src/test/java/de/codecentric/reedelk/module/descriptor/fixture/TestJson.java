package de.codecentric.reedelk.module.descriptor.fixture;

import de.codecentric.reedelk.runtime.api.commons.FileUtils;

import java.net.URL;

public enum TestJson {

    COMPONENT_WITH_ALL_SUPPORTED_PROPERTIES {
        @Override
        String path() {
            return "/json/component_with_all_supported_properties.json";
        }
    };

    abstract String path();

    public String get() {
        URL fileURL = TestJson.class.getResource(path());
        return FileUtils.ReadFromURL.asString(fileURL);
    }
}
