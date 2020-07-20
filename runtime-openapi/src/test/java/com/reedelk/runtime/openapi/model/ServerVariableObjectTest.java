package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.openapi.OpenApiJsons;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;

class ServerVariableObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeServerVariableWithAllProperties() {
        // Given
        ServerVariableObject serverVariable = new ServerVariableObject();
        serverVariable.setEnumValues(asList("dev", "uat", "prod"));
        serverVariable.setDescription("Environment variable");
        serverVariable.setDefaultValue("dev");

        // Expect
        assertSerializedCorrectly(serverVariable, OpenApiJsons.ServerVariableObject.WithAllProperties);
    }

    @Test
    void shouldCorrectlySerializeServerWithRequiredValues() {
        // Given
        ServerVariableObject serverVariable = new ServerVariableObject();

        // When
        assertSerializedCorrectly(serverVariable, OpenApiJsons.ServerVariableObject.WithDefaultProperties);
    }
}
