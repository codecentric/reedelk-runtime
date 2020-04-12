package com.reedelk.esb.services.scriptengine.evaluator.function;

import com.reedelk.esb.flow.deserializer.converter.ProxyScript;
import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.script.Script;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

class ScriptDefinitionBuilderTest {

    private final long moduleId = 10L;
    private final ModuleContext moduleContext = new ModuleContext(moduleId);

    private ScriptDefinitionBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new ScriptDefinitionBuilder();
    }

    @Test
    void shouldCorrectlyReplaceOriginalFunctionNameWithGeneratedFunctionName() {
        // Given
        String myFunction = "function myFunction(message,context) {\n" +
                "   return 'This is a test';\n" +
                "}\n";

        String expectedReplaced = "function %s(message,context) {\n" +
                "   return 'This is a test';\n" +
                "}\n";

        Script script = scriptFromBody(myFunction);

        // When
        String replaced = builder.apply(script);

        // Then
        String randomlyGeneratedFunctionName = script.functionName();
        assertThat(replaced).isEqualTo(format(expectedReplaced, randomlyGeneratedFunctionName));
    }

    private Script scriptFromBody(String body) {
        Script originalScript = Script.from("/test/path", moduleContext);
        return new ProxyScript(originalScript, body);
    }
}
