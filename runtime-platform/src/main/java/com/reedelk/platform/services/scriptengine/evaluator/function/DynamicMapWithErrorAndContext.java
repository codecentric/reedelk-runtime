package com.reedelk.platform.services.scriptengine.evaluator.function;

public class DynamicMapWithErrorAndContext extends DynamicMapWithMessageAndContext {

    private static final String TEMPLATE =
            "function %s(context, error) {\n" +
                    "  return %s\n" +
                    "};";

    @Override
    protected String getTemplate() {
        return TEMPLATE;
    }
}
