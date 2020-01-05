package com.reedelk.esb.services.scriptengine.evaluator.function;

import com.reedelk.runtime.api.commons.ScriptUtils;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;

public class DynamicValueWithMessageAndContext implements FunctionDefinitionBuilder<DynamicValue> {

    private static final String TEMPLATE =
            "function %s(message, context) {\n" +
                    "  return %s\n" +
                    "};";

    @Override
    public String from(DynamicValue dynamicValue) {
        String functionBody = ScriptUtils.unwrap(dynamicValue.body());
        return String.format(TEMPLATE, dynamicValue.functionName(), functionBody);
    }
}