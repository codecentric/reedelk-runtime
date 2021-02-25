package de.codecentric.reedelk.platform.services.scriptengine.evaluator.function;

public class DynamicMapWithErrorAndContext extends DynamicMapWithMessageAndContext {

    private static final String TEMPLATE =
            "def %s(context, error) {\n" +
                    "  %s\n" +
                    "}";

    @Override
    protected String getTemplate() {
        return TEMPLATE;
    }
}
