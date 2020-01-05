package com.reedelk.esb.services.scriptengine.evaluator.function;

public class DynamicMapWithErrorAndContext extends DynamicMapWithMessageAndContext {

    private static final String TEMPLATE =
            "function %s(error, context) {\n" +
                    "  return %s\n" +
                    "};";

    @Override
    protected String getTemplate() {
        return TEMPLATE;
    }
}
