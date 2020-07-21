package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.OpenApiJsons;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class ServerObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeServerWithAllProperties() {
        // Given
        ServerObject server = new ServerObject();
        server.setUrl("https://{environment}.{domain}.com/v1");
        server.setDescription("Development server");

        ServerVariableObject environmentVariable = new ServerVariableObject();
        environmentVariable.setEnumValues(Arrays.asList("dev", "uat", "prod"));
        environmentVariable.setDescription("Environment variable");
        environmentVariable.setDefaultValue("dev");

        ServerVariableObject domainVariable = new ServerVariableObject();
        domainVariable.setEnumValues(Arrays.asList("localhost", "mydomain1", "mydomain2"));
        domainVariable.setDescription("Domain variable");
        domainVariable.setDefaultValue("localhost");

        Map<String, ServerVariableObject> variables = new HashMap<>();
        variables.put("environment", environmentVariable);
        variables.put("domain", domainVariable);
        server.setVariables(variables);

        // Expect
        assertSerializeJSON(server, OpenApiJsons.ServerObject.WithAllPropertiesJson);
        assertSerializeYAML(server, OpenApiJsons.ServerObject.WithAllPropertiesYaml);
    }

    @Test
    void shouldCorrectlySerializeServerWithRequiredValues() {
        // Given
        ServerObject server = new ServerObject();
        server.setUrl("/");

        // Expect
        assertSerializeJSON(server, OpenApiJsons.ServerObject.WithDefaultPropertiesJson);
        assertSerializeYAML(server, OpenApiJsons.ServerObject.WithDefaultPropertiesYaml);
    }
}
