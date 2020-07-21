package com.reedelk.runtime.openapi.v3;

public enum PredefinedSchema {

    NONE(""),

    STRING("{\n" +
            "    \"type\": \"string\"\n" +
            "  }"),
    INTEGER("{\n" +
            "    \"type\": \"integer\"\n" +
            "  }"),

    ARRAY_INTEGER("{\n" +
            "    \"type\": \"array\",\n" +
            "    \"items\": {\n" +
            "      \"type\": \"integer\"\n" +
            "      }" +
            "    }"),

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
