package com.reedelk.runtime.openapi;

import com.reedelk.runtime.api.annotation.DisplayName;

public enum PredefinedSchema {

    @DisplayName("Custom Schema")
    NONE(""),
    @DisplayName("String")
    STRING("{\n" +
            "    \"type\": \"string\"\n" +
            "  }"),
    @DisplayName("Integer")
    INTEGER("{\n" +
            "    \"type\": \"integer\"\n" +
            "  }"),
    @DisplayName("Array of Integers")
    ARRAY_INTEGER("{\n" +
            "    \"type\": \"array\",\n" +
            "    \"items\": {\n" +
            "      \"type\": \"integer\"\n" +
            "      }" +
            "    }"),
    @DisplayName("Array of Strings")
    ARRAY_STRING("{\n" +
            "    \"type\": \"array\",\n" +
            "    \"items\": {\n" +
            "      \"type\": \"string\"\n" +
            "      }" +
            "    }");

    private String schema;

    PredefinedSchema(String schema) {
        this.schema = schema;
    }

    public String schema() {
        return schema;
    }
}
