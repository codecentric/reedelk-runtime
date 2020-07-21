package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.OpenApiJsons;
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
        assertSerializeJSON(serverVariable, OpenApiJsons.ServerVariableObject.WithAllPropertiesJson);
        assertSerializeYAML(serverVariable, OpenApiJsons.ServerVariableObject.WithAllPropertiesYaml);
    }

    @Test
    void shouldCorrectlySerializeServerWithRequiredValues() {
        // Given
        ServerVariableObject serverVariable = new ServerVariableObject();

        // When
        assertSerializeJSON(serverVariable, OpenApiJsons.ServerVariableObject.WithDefaultPropertiesJson);
        assertSerializeYAML(serverVariable, OpenApiJsons.ServerVariableObject.WithDefaultPropertiesYaml);
    }
}
