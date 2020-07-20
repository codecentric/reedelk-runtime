package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.api.commons.ImmutableMap;
import com.reedelk.runtime.openapi.OpenApiJsons;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static java.util.Arrays.asList;

class ServerObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeServerWithAllProperties() {
        // Given
        ServerObject server = new ServerObject();
        server.setUrl("https://{environment}.{domain}.com/v1");
        server.setDescription("Development server");

        ServerVariableObject environmentVariable = new ServerVariableObject();
        environmentVariable.setEnumValues(asList("dev", "uat", "prod"));
        environmentVariable.setDescription("Environment variable");
        environmentVariable.setDefaultValue("dev");

        ServerVariableObject domainVariable = new ServerVariableObject();
        domainVariable.setEnumValues(asList("localhost", "mydomain1", "mydomain2"));
        domainVariable.setDescription("Domain variable");
        domainVariable.setDefaultValue("localhost");

        Map<String, ServerVariableObject> variables = ImmutableMap.of(
                "environment", environmentVariable,
                "domain", domainVariable);
        server.setVariables(variables);

        // Expect
        assertSerializedCorrectly(server, OpenApiJsons.ServerObject.WithAllProperties);
    }

    @Test
    void shouldCorrectlySerializeServerWithRequiredValues() {
        // Given
        ServerObject server = new ServerObject();

        // Expect
        assertSerializedCorrectly(server, OpenApiJsons.ServerObject.WithDefaultProperties);
    }
}
