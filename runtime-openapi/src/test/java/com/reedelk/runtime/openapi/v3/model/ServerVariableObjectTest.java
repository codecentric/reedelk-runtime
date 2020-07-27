package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.Fixture;
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
        assertSerializeJSON(serverVariable, Fixture.ServerVariableObject.WithAllPropertiesJson);
        assertSerializeYAML(serverVariable, Fixture.ServerVariableObject.WithAllPropertiesYaml);
    }

    @Test
    void shouldCorrectlySerializeServerWithRequiredValues() {
        // Given
        ServerVariableObject serverVariable = new ServerVariableObject();

        // When
        assertSerializeJSON(serverVariable, Fixture.ServerVariableObject.WithDefaultPropertiesJson);
        assertSerializeYAML(serverVariable, Fixture.ServerVariableObject.WithDefaultPropertiesYaml);
    }
}
