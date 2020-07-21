package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.OpenApiJsons;
import com.reedelk.runtime.openapi.v3.PredefinedSchema;
import org.junit.jupiter.api.Test;

class ParameterObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeParameterWithAllProperties() {
        // Given
        ParameterObject parameter = new ParameterObject();
        parameter.setAllowEmptyValue(true);
        parameter.setAllowReserved(true);
        parameter.setDeprecated(true);
        parameter.setRequired(true);
        parameter.setExplode(true);
        parameter.setName("param1");
        parameter.setIn(ParameterLocation.query);
        parameter.setStyle(ParameterStyle.simple);
        parameter.setDescription("My parameter description");
        parameter.setPredefinedSchema(PredefinedSchema.ARRAY_STRING);

        // Expect
        assertSerializedCorrectly(parameter, OpenApiJsons.ParameterObject.WithAllProperties);
    }

    @Test
    void shouldCorrectlySerializeParameterWithDefault() {
        // Given
        ParameterObject parameter = new ParameterObject();

        // Expect
        assertSerializedCorrectly(parameter, OpenApiJsons.ParameterObject.WithDefault);
    }

    @Test
    void shouldSetRequiredTrueWhenParameterInPath() {
        // Given
        ParameterObject parameter = new ParameterObject();
        parameter.setIn(ParameterLocation.path);

        // Expect
        assertSerializedCorrectly(parameter, OpenApiJsons.ParameterObject.WithInPath);
    }
}
