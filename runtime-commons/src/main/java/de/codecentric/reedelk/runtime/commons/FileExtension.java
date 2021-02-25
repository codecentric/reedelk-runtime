package de.codecentric.reedelk.runtime.commons;

import de.codecentric.reedelk.runtime.api.commons.StringUtils;

public enum FileExtension {

    XML("xml"),
    JAR("jar"),
    JSON("json"),
    FLOW("flow"),
    SCRIPT("groovy"),
    CONFIG("fconfig"),
    SUBFLOW("subflow"),
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
