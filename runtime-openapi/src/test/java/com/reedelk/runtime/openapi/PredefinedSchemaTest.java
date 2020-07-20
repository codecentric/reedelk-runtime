package com.reedelk.runtime.openapi;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class PredefinedSchemaTest {

    @Test
    void shouldParseStringCorrectly() {
        // Given
        JSONObject object = new JSONObject(PredefinedSchema.STRING.schema());

        // Then
        Assertions.assertThat(object).isNotNull();
    }

    @Test
    void shouldParseArrayStringCorrectly() {
        // Given
        JSONObject object = new JSONObject(PredefinedSchema.ARRAY_STRING.schema());

        // Then
        Assertions.assertThat(object).isNotNull();
    }

    @Test
    void shouldParseIntegerCorrectly() {
        // Given
        JSONObject object = new JSONObject(PredefinedSchema.INTEGER.schema());

        // Then
        Assertions.assertThat(object).isNotNull();
    }

    @Test
    void shouldParseIntegerArrayCorrectly() {
        // Given
        JSONObject object = new JSONObject(PredefinedSchema.ARRAY_INTEGER.schema());

        // Then
        Assertions.assertThat(object).isNotNull();
    }
}
