package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.api.resource.ResourceText;
import com.reedelk.runtime.openapi.OpenApiJsons;
import com.reedelk.runtime.openapi.OpenApiSerializableContext;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static reactor.core.publisher.Mono.just;

class RequestBodyObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeRequestBodyWithAllProperties() {
        // Given
        ResourceText example = Mockito.mock(ResourceText.class);
        Mockito.doReturn(just(OpenApiJsons.Examples.JsonPet.string())).when(example).data();

        MediaTypeObject mediaTypeObject = new MediaTypeObject();
        mediaTypeObject.setExample(example);
        Map<String, MediaTypeObject> content = new HashMap<>();
        content.put("application/json", mediaTypeObject);

        RequestBodyObject requestBody = new RequestBodyObject();
        requestBody.setDescription("My request body");
        requestBody.setRequired(true);
        requestBody.setContent(content);

        // Expect
        OpenApiJsons.RequestBodyObject withAllProperties = OpenApiJsons.RequestBodyObject.WithAllProperties;

        ComponentsObject componentsObject = new ComponentsObject();
        OpenApiSerializableContext context = new OpenApiSerializableContext(componentsObject);
        JSONObject actualObject = requestBody.serialize(context);
        JSONObject expectedObject = new JSONObject(withAllProperties.string());
        assertSameExamples(actualObject, expectedObject);
    }

    @Test
    void shouldCorrectlySerializeRequestBodyWithDefaults() {
        // Given
        RequestBodyObject requestBody = new RequestBodyObject();

        // Expect
        assertSerializedCorrectly(requestBody, OpenApiJsons.RequestBodyObject.WithDefault);
    }

    private void assertSameExamples(JSONObject object1, JSONObject object2) {
        JSONObject contentObject1 = object1.getJSONObject("content");
        JSONObject applicationJsonObject1 = contentObject1.getJSONObject("application/json");
        String applicationJsonExample1 = applicationJsonObject1.getString("example");

        JSONObject contentObject2 = object2.getJSONObject("content");
        JSONObject applicationJsonObject2 = contentObject2.getJSONObject("application/json");
        String applicationJsonExample2 = applicationJsonObject2.getString("example");
        Assertions.assertThat(applicationJsonExample1).isEqualToIgnoringNewLines(applicationJsonExample2);
    }
}
