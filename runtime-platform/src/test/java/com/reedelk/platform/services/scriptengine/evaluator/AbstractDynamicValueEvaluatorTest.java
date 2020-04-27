package com.reedelk.platform.services.scriptengine.evaluator;

import com.reedelk.platform.exception.ScriptCompilationException;
import com.reedelk.platform.exception.ScriptExecutionException;
import com.reedelk.platform.services.scriptengine.evaluator.function.FunctionDefinitionBuilder;
import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.script.ScriptBlock;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.script.ScriptException;

import static com.reedelk.platform.pubsub.Action.Module.ActionModuleUninstalled;
import static com.reedelk.runtime.api.commons.ImmutableMap.of;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * This test verifies the behaviour of the invokeFunction: make sure that
 * a function is compiled if it does not exists yet. Moreover, this test
 * verifies that when 'onModuleUninstalled' event is fired, previously
 * defined functions are correctly removed from the script engine.
 */
@ExtendWith(MockitoExtension.class)
class AbstractDynamicValueEvaluatorTest {

    private final long testModuleId = 10L;
    private ModuleContext moduleContext = new ModuleContext(testModuleId);

    @Mock
    private ScriptEngineProvider mockEngineProvider;

    private TestAwareAbstractDynamicValueEvaluatorTest evaluator;
    private TestFunctionBuilder testFunctionBuilder = new TestFunctionBuilder();

    @BeforeEach
    void setUp() {
        evaluator = spy(new TestAwareAbstractDynamicValueEvaluatorTest());
        doReturn(mockEngineProvider).when(evaluator).scriptEngine();
    }

    @Test
    void shouldCompileScriptWhenFunctionIsNotFoundAndInvokeAgainFunctionAfterCompilation() throws NoSuchMethodException, ScriptException {
        // Given
        String expectedResult = "evaluation result";
        DynamicString dynamicValue = DynamicString.from("#['evaluation result']", moduleContext);

        when(mockEngineProvider
                .invokeFunction(dynamicValue.functionName()))
                .thenThrow(new NoSuchMethodException("method not found"))
                .thenReturn(expectedResult);

        // When
        Object actualResult = evaluator.invokeFunction(dynamicValue, testFunctionBuilder);

        // Then
        verify(mockEngineProvider).compile(anyString());
        verify(mockEngineProvider, times(2)).invokeFunction(dynamicValue.functionName());
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void shouldThrowExceptionWhenScriptCouldNotBeCompiled() throws NoSuchMethodException, ScriptException {
        // Given
        DynamicString dynamicValue = DynamicString.from("#[notValid'Script']", moduleContext);

        when(mockEngineProvider
                .invokeFunction(dynamicValue.functionName()))
                .thenThrow(new NoSuchMethodException("method not found"));

        doThrow(new ScriptException("expected ' ' but 'S was found"))
                .when(mockEngineProvider)
                .compile(anyString());

        // When
        ScriptCompilationException thrown = assertThrows(ScriptCompilationException.class,
                () -> evaluator.invokeFunction(dynamicValue, testFunctionBuilder));

        // Then

        verify(mockEngineProvider).invokeFunction(anyString());
        verify(mockEngineProvider).compile(anyString());
        verifyNoMoreInteractions(mockEngineProvider);
        assertThat(thrown).hasMessage("Could not compile script: expected ' ' but 'S was found,\n" +
                "- Script code:\n" +
                "#[notValid'Script']");
    }

    @Test
    void shouldThrowExceptionWhenScriptCouldNotBeInvokedAfterCompilation() throws NoSuchMethodException, ScriptException {
        // Given
        DynamicString dynamicValue = DynamicString.from("#['Script' + unknown]", moduleContext);

        when(mockEngineProvider
                .invokeFunction(dynamicValue.functionName()))
                .thenThrow(new NoSuchMethodException("method not found"))
                .thenThrow(new ScriptException("variable not found 'unknown'"));


        // When
        ScriptExecutionException thrown = assertThrows(ScriptExecutionException.class,
                () -> evaluator.invokeFunction(dynamicValue, testFunctionBuilder));

        // Then

        verify(mockEngineProvider, times(2)).invokeFunction(dynamicValue.functionName());
        verify(mockEngineProvider).compile(anyString());
        verifyNoMoreInteractions(mockEngineProvider);
        assertThat(thrown).hasMessage("Could not execute script: variable not found 'unknown',\n" +
                "- Script code:\n" +
                "#['Script' + unknown]");
    }

    @Test
    void shouldRethrowExceptionWithScriptBodyWhenScriptExceptionIsThrown() throws NoSuchMethodException, ScriptException {
        // Given
        DynamicString dynamicValue = DynamicString.from("#['test' + unknownVariable]", moduleContext);

        doThrow(new ScriptException("variable not found unknownVariable"))
                .when(mockEngineProvider)
                .invokeFunction(dynamicValue.functionName());

        // When
        ScriptExecutionException exception = assertThrows(ScriptExecutionException.class,
                () -> evaluator.invokeFunction(dynamicValue, testFunctionBuilder));

        // Then
        assertThat(exception).hasMessage("Could not execute script: variable not found unknownVariable,\n" +
                "- Script code:\n" +
                "#['test' + unknownVariable]");
    }

    @Test
    void shouldCompileRegisterFunctionsCorrectlyForModuleId() throws ScriptException {
        // Given
        DynamicString dynamicValue1 = DynamicString.from("#['evaluation result']", moduleContext);
        DynamicString dynamicValue2 = DynamicString.from("#['another evaluation result']", moduleContext);

        // When
        evaluator.compile(dynamicValue1, testFunctionBuilder);
        evaluator.compile(dynamicValue2, testFunctionBuilder);

        // Then
        verify(mockEngineProvider).compile(testFunctionBuilder.apply(dynamicValue1));
        verify(mockEngineProvider).compile(testFunctionBuilder.apply(dynamicValue2));
        assertThat(evaluator.moduleIdFunctionNamesMap)
                .containsEntry(
                        moduleContext.getModuleId(),
                        asList(dynamicValue1.functionName(), dynamicValue2.functionName()));
    }

    @Test
    void shouldRemoveEntryFromModuleIdFunctionMapWhenModuleUninstalled() {
        // Given
        DynamicString dynamicValue1 = DynamicString.from("#['evaluation result']", moduleContext);
        DynamicString dynamicValue2 = DynamicString.from("#['another evaluation result']", moduleContext);

        evaluator.compile(dynamicValue1, testFunctionBuilder);
        evaluator.compile(dynamicValue2, testFunctionBuilder);

        // When
        ActionModuleUninstalled actionModuleUninstalled = new ActionModuleUninstalled(testModuleId);
        evaluator.onModuleUninstalled(actionModuleUninstalled);

        // Then
        assertThat(evaluator.moduleIdFunctionNamesMap).doesNotContainKey(testModuleId);
    }

    @Test
    void shouldUndefineFunctionFromScriptEngineWhenModuleUninstalled() throws ScriptException {
        // Given
        DynamicString dynamicValue1 = DynamicString.from("#['evaluation result']", moduleContext);
        DynamicString dynamicValue2 = DynamicString.from("#['another evaluation result']", moduleContext);

        evaluator.compile(dynamicValue1, testFunctionBuilder);
        evaluator.compile(dynamicValue2, testFunctionBuilder);

        // When
        ActionModuleUninstalled actionModuleUninstalled = new ActionModuleUninstalled(testModuleId);
        evaluator.onModuleUninstalled(actionModuleUninstalled);

        // Then
        verify(mockEngineProvider).removeBinding(dynamicValue1.functionName());
        verify(mockEngineProvider).removeBinding(dynamicValue2.functionName());
        verify(mockEngineProvider, times(2)).compile(anyString());
        verifyNoMoreInteractions(mockEngineProvider);
    }

    @Test
    void shouldDoNothingWhenModuleUninstalledDidNotHaveAnyFunctionRegisteredInTheScriptEngine() {
        // Given
        DynamicString dynamicValue1 = DynamicString.from("#['evaluation result']", moduleContext);
        DynamicString dynamicValue2 = DynamicString.from("#['another evaluation result']", moduleContext);

        evaluator.compile(dynamicValue1, testFunctionBuilder);
        evaluator.compile(dynamicValue2, testFunctionBuilder);

        assumeTrue(evaluator.moduleIdFunctionNamesMap.containsKey(testModuleId));

        // When: we use a different module id from the one which was used to compile the dynamic values)
        long notExistingModuleId = testModuleId + 1;
        ActionModuleUninstalled actionModuleUninstalled = new ActionModuleUninstalled(notExistingModuleId);
        evaluator.onModuleUninstalled(actionModuleUninstalled);

        // Then
        assertThat(evaluator.moduleIdFunctionNamesMap).doesNotContainKey(notExistingModuleId);
        assertThat(evaluator.moduleIdFunctionNamesMap).containsOnlyKeys(testModuleId);
    }

    @Test
    void shouldThrowExceptionWhenDynamicMapCouldNotBeCompiled() throws NoSuchMethodException, ScriptException {
        // Given
        DynamicStringMap dynamicStringMap = DynamicStringMap.from(
                of("X-Correlation-ID", "#[notValid'Script']"), moduleContext);

        when(mockEngineProvider
                .invokeFunction(dynamicStringMap.functionName()))
                .thenThrow(new NoSuchMethodException("method not found"));

        doThrow(new ScriptException("Could not find '-'"))
                .when(mockEngineProvider)
                .compile(anyString());

        // When
        ScriptCompilationException thrown = assertThrows(ScriptCompilationException.class,
                () -> evaluator.invokeFunction(dynamicStringMap, testFunctionBuilder));

        // Then

        verify(mockEngineProvider).invokeFunction(anyString());
        verify(mockEngineProvider).compile(anyString());
        verifyNoMoreInteractions(mockEngineProvider);
        assertThat(thrown).hasMessage("Could not compile script: Could not find '-',\n" +
                "- Script code:\n" +
                "{X-Correlation-ID=#[notValid'Script']}");
    }

    private static class TestAwareAbstractDynamicValueEvaluatorTest extends AbstractDynamicValueEvaluator {
    }

    static class TestFunctionBuilder implements FunctionDefinitionBuilder<ScriptBlock> {

        private static final String TEMPLATE =
                "def %s() {\n" +
                        "%s\n" +
                        "}";

        @Override
        public String apply(ScriptBlock dynamicValue) {
            return format(TEMPLATE, dynamicValue.functionName(), dynamicValue.body());
        }
    }
}
