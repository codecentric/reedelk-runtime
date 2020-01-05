package com.reedelk.runtime.api.script;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A script source might contain one or multiple modules defined in the same source.
 */
public interface ScriptSource {

    long moduleId();

    /**
     * Returns the Javascript source file path.
     * @return the Javascript resource file name in the module package.
     */
    String resource();

    /**
     * Returns the name of the registered Javascript module in the source file path.
     * @return the name of the Javascript modules in the source file.
     */
    Collection<String> scriptModuleNames();

    default Map<String, Object> bindings() {
        return new HashMap<>();
    }

    default Reader get() {
        return new BufferedReader(
                new InputStreamReader(
                        this.getClass().getResourceAsStream(resource())));
    }
}
