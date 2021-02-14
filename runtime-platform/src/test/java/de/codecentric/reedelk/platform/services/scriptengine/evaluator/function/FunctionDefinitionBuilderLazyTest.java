package de.codecentric.reedelk.platform.services.scriptengine.evaluator.function;

import de.codecentric.reedelk.runtime.api.commons.ModuleContext;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicValue;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class FunctionDefinitionBuilderLazyTest {

    private ModuleContext context = new ModuleContext(10L);

    @Test
    void shouldBuildCorrectlyFunctionDefinitionWithTwoArguments() {
        // Given
        FunctionDefinitionBuilder<DynamicValue<?>> builder =
                FunctionDefinitionBuilderLazy.from(Arrays.asList("argument1", "argument2"));

        DynamicString expression = DynamicString.from("#[argument1.length + argument2.length]", context);

        // When
        String function = builder.apply(expression);

        // Then
        String expected = "def " + expression.functionName() + "(argument1, argument2) {\n" +
                "  argument1.length + argument2.length\n" +
                "}";
        assertThat(function).isEqualTo(expected);
    }

    @Test
    void shouldBuildCorrectlyFunctionDefinitionWithoutArguments() {
        // Given
        FunctionDefinitionBuilder<DynamicValue<?>> builder =
                FunctionDefinitionBuilderLazy.from(Collections.emptyList());

        DynamicString expression = DynamicString.from("#['my test']", context);

        // When
        String function = builder.apply(expression);

        // Then
        String expected = "def " + expression.functionName() + "() {\n" +
                "  'my test'\n" +
                "}";
        assertThat(function).isEqualTo(expected);
    }
}
