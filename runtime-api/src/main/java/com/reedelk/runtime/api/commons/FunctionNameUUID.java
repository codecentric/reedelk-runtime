package com.reedelk.runtime.api.commons;

import java.util.UUID;

public class FunctionNameUUID {

    private static final String FUNCTION_NAME_PREFIX = "fun_%d_%s";

    private FunctionNameUUID() {
    }

    public static String generate(ModuleContext context) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return String.format(FUNCTION_NAME_PREFIX, context.getModuleId(), uuid);
    }
}
