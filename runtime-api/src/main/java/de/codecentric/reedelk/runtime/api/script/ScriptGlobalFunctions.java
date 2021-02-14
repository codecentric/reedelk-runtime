package de.codecentric.reedelk.runtime.api.script;

import java.util.Map;

public interface ScriptGlobalFunctions {

    long moduleId();

    Map<String, Object> bindings();
}
