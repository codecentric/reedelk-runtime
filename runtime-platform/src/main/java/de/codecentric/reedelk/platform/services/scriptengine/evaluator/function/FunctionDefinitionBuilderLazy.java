package de.codecentric.reedelk.platform.services.scriptengine.evaluator.function;

import de.codecentric.reedelk.runtime.api.commons.ScriptUtils;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

import java.util.List;

/**
 * Creates a function only when needed. This is needed to optimize execution of script functions
 * for which arguments names are not known in advance.
 */
public class FunctionDefinitionBuilderLazy implements FunctionDefinitionBuilder<DynamicValue<?>> {

    private final List<String> functionArgumentNames;

    private FunctionDefinitionBuilderLazy(List<String> functionArgumentNames) {
        this.functionArgumentNames = functionArgumentNames;
    }

    public static FunctionDefinitionBuilder<DynamicValue<?>> from(List<String> functionArgumentNames) {
        return new FunctionDefinitionBuilderLazy(functionArgumentNames);
    }

    @Override
    public String apply(DynamicValue<?> dynamicValue) {
        String joinedArgs = String.join(", ", functionArgumentNames);
        String template = "def %s(" + joinedArgs + ") {\n" +
                "  %s\n" +
                "}";

        String functionBody = ScriptUtils.unwrap(dynamicValue.body());
        return String.format(template, dynamicValue.functionName(), functionBody);
    }
}
