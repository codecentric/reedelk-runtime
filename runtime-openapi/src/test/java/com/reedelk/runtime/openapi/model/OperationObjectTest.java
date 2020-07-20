package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.openapi.OpenApiJsons;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class OperationObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeOperationWithAllProperties() {
        // Given
        ResponseObject response1 = new ResponseObject();
        ResponseObject response2 = new ResponseObject();
        Map<String, ResponseObject> responseObjectMap = new HashMap<>();
        responseObjectMap.put("200", response1);
        responseObjectMap.put("500", response2);

        ParameterObject parameter1 = new ParameterObject();
        parameter1.setName("param1");
        ParameterObject parameter2 = new ParameterObject();
        parameter2.setName("param2");

        RequestBodyObject requestBody = new RequestBodyObject();
        requestBody.setDescription("My request body");
        requestBody.setRequired(true);

        OperationObject operation = new OperationObject();
        operation.setTags(Arrays.asList("tag1", "tag2"));
        operation.setSummary("My summary");
        operation.setOperationId("myOperationId");
        operation.setDescription("My operation description");
        operation.setParameters(Arrays.asList(parameter1, parameter2));
        operation.setDeprecated(true);
        operation.setResponses(responseObjectMap);
        operation.setRequestBody(requestBody);

        // Expect
        assertSerializedCorrectly(operation, OpenApiJsons.OperationObject.WithAllProperties);
    }

    @Test
    void shouldCorrectlySerializeOperationWithDefault() {
        // Given
        OperationObject operation = new OperationObject();

        // Expect
        assertSerializedCorrectly(operation, OpenApiJsons.OperationObject.WithDefault);
    }
}
