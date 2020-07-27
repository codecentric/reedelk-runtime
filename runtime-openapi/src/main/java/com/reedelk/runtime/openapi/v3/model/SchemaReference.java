package com.reedelk.runtime.openapi.v3.model;

public class SchemaReference {

    private final String schemaId;
    private final String schemaData;

    public SchemaReference(String schemaId, String schemaData) {
        this.schemaId = schemaId;
        this.schemaData = schemaData;
    }

    public String getSchemaId() {
        return schemaId;
    }

    public String getSchemaData() {
        return schemaData;
    }

}
