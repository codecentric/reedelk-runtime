package com.reedelk.platform.services.scriptengine.evaluator;

import com.reedelk.platform.exception.ScriptCompilationException;
import com.reedelk.runtime.api.script.ScriptSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.script.ScriptException;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.reedelk.platform.pubsub.Action.Module.ActionModuleUninstalled;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScriptSourceEvaluatorTest {

    private ScriptSourceEvaluator evaluator;

    private long testModuleId = 10L;
    private String testResource = "/functions/utils.js";
    private Collection<String> testModules = asList("Module1", "Module2");
    private Map<String, Object> testBindings = new HashMap<>();

    @Mock
    private ScriptEngineProvider mockEngineProvider;
    @Mock
    private ScriptSource scriptSource;

    private StringReader stringReader;

    @BeforeEach
    void setUp() {
        evaluator = spy(new ScriptSourceEvaluator());
        doReturn(mockEngineProvider).when(evaluator).scriptEngine();

        stringReader = new StringReader("test source code");
        doReturn(stringReader).when(scriptSource).get();
        doReturn(testBindings).when(scriptSource).bindings();
        doReturn(testModules).when(scriptSource).scriptModuleNames();
    }

    @Test
    void shouldCorrectlyCompileScriptSourceModule() throws ScriptException {
        // Given
        doReturn(testModuleId).when(scriptSource).moduleId();

        // When
        evaluator.register(scriptSource);

        // Then
        verify(mockEngineProvider).compile(testModules, stringReader, testBindings);
        verifyNoMoreInteractions(mockEngineProvider);

        assertThat(evaluator.moduleIdAndScriptModuleNamesMap).containsEntry(testModuleId, testModules);
    }

    @Test
    void shouldThrowExceptionWhenScriptSourceCouldNotBeCompiled() throws ScriptException {
        // Given
        doReturn(testResource).when(scriptSource).resource();
        doThrow(new ScriptException("expected var but ' found"))
                .when(mockEngineProvider)
                .compile(testModules, stringReader, testBindings);

        // When
        ScriptCompilationException thrown = assertThrows(ScriptCompilationException.class,
                () -> evaluator.register(scriptSource));

        // Then
        assertThat(thrown).hasMessage("Could not compile script source: expected var but ' found, \n" +
                "- Source: /functions/utils.js\n" +
                "- Module names: [Module1, Module2]");
    }

    @Test
    void shouldOnModuleUninstalledRemoveModuleReferencesFromModuleIdAndNamesMap() {
        // Given
        doReturn(testModuleId).when(scriptSource).moduleId();
        evaluator.register(scriptSource);
        ActionModuleUninstalled actionModuleUninstalled = new ActionModuleUninstalled(testModuleId);

        // When
        evaluator.onModuleUninstalled(actionModuleUninstalled);

        // Then
        assertThat(evaluator.moduleIdAndScriptModuleNamesMap).doesNotContainKey(testModuleId);
    }

    @Test
    void shouldOnModuleUninstalledUndefineAllModulesFromScriptEngineProvider() throws ScriptException {
        // Given
        doReturn(testModuleId).when(scriptSource).moduleId();
        evaluator.register(scriptSource);
        ActionModuleUninstalled actionModuleUninstalled = new ActionModuleUninstalled(testModuleId);

        // When
        evaluator.onModuleUninstalled(actionModuleUninstalled);

        // Then
        verify(mockEngineProvider).compile(testModules, stringReader, testBindings);
        verify(mockEngineProvider).removeBinding("Module1");
        verify(mockEngineProvider).removeBinding("Module2");
        verifyNoMoreInteractions(mockEngineProvider);
    }
}
