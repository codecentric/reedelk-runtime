package com.reedelk.esb.services.scriptengine.evaluator.function;

import com.reedelk.runtime.api.commons.ScriptUtils;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

import java.util.List;

import static java.util.Arrays.asList;

public class FunctionDefinitionBuilderDefault implements FunctionDefinitionBuilder<DynamicValue<?>> {

    private final String template;

    private FunctionDefinitionBuilderDefault(List<String> functionArgumentNames) {
        String joinedArgs = String.join(", ", functionArgumentNames);
        template = "function %s(" + joinedArgs + ") {\n" +
                "  return %s\n" +
                "};";
    }

    public static FunctionDefinitionBuilder<DynamicValue<?>> from(String ...functionArgumentNames) {
        return new FunctionDefinitionBuilderDefault(asList(functionArgumentNames));
    }

    public static final FunctionDefinitionBuilder<DynamicValue<?>> CONTEXT_AND_ERROR =
            FunctionDefinitionBuilderDefault.from("context", "error");

    public static final FunctionDefinitionBuilder<DynamicValue<?>> CONTEXT_AND_MESSAGE =
            FunctionDefinitionBuilderDefault.from("context", "message");

    @Override
    public String apply(DynamicValue<?> dynamicValue) {
        String functionBody = ScriptUtils.unwrap(dynamicValue.body());
        return String.format(template, dynamicValue.functionName(), functionBody);
    }
}
