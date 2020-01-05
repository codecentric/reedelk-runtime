package com.reedelk.esb.services.scriptengine;

import com.reedelk.esb.test.utils.TestScript;
import org.junit.jupiter.api.Test;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class JavascriptEngineProviderTest {

    @Test
    void shouldCompileModuleScript() throws IOException, ScriptException {
        // Given
        Collection<String> modulesNames = Collections.singletonList("TestModule");
        Map<String,Object> bindings = new HashMap<>();

        try (Reader stringReader = new StringReader(TestScript.SIMPLE_MODULE.get())) {
            JavascriptEngineProvider.getInstance().compile(modulesNames, stringReader, bindings);
        }
    }
}