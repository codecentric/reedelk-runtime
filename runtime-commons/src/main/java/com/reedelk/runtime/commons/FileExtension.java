package com.reedelk.runtime.commons;

import com.reedelk.runtime.api.commons.StringUtils;

public enum FileExtension {

    XML("xml"),
    JAR("jar"),
    JSON("json"),
    SCRIPT("js"),
    FLOW("flow"),
    SUBFLOW("subflow"),
    CONFIG("fconfig"),
    PROPERTIES("properties");

    private final String extension;

    FileExtension(String extension) {
        this.extension = extension;
    }

    public String value() {
        return extension;
    }

    public boolean is(String name) {
        return StringUtils.isNotBlank(name) && name.endsWith("." + extension);
    }
}
