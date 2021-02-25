package de.codecentric.reedelk.platform.services.scriptengine;

import de.codecentric.reedelk.platform.test.utils.TestScript;
import org.junit.jupiter.api.Test;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class GroovyEngineProviderTest {

    @Test
    void shouldCompileModuleScript() throws IOException, ScriptException {
        // Given
        Collection<String> modulesNames = Collections.singletonList("TestModule");
        Map<String,Object> bindings = new HashMap<>();

        try (Reader stringReader = new StringReader(TestScript.SIMPLE_MODULE.get())) {
            GroovyEngineProvider.getInstance().compile(modulesNames, stringReader, bindings);
        }
    }
}
