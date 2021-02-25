package de.codecentric.reedelk.platform.test.utils;

import de.codecentric.reedelk.runtime.api.commons.FileUtils;

import java.net.URL;

public enum TestScript {

    SIMPLE_MODULE {
        @Override
        String path() {
            return "/de/codecentric/reedelk/platform/lifecycle/TestModule.groovy";
        }
    };

    abstract String path();

    public String get() {
        URL fileURL = TestScript.class.getResource(path());
        return FileUtils.ReadFromURL.asString(fileURL);
    }
}
