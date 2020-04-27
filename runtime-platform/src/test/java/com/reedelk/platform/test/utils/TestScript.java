package com.reedelk.platform.test.utils;

import java.net.URL;

import static com.reedelk.runtime.api.commons.FileUtils.ReadFromURL;

public enum TestScript {

    SIMPLE_MODULE {
        @Override
        String path() {
            return "/com/reedelk/platform/lifecycle/TestModule.groovy";
        }
    };

    abstract String path();

    public String get() {
        URL fileURL = TestScript.class.getResource(path());
        return ReadFromURL.asString(fileURL);
    }
}
