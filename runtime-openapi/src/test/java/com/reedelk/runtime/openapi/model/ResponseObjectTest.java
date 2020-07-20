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

import static com.reedelk.runtime.openapi.OpenApiJsons.Examples;
import static reactor.core.publisher.Mono.just;

class ResponseObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeResponseWithAllProperties() {
        // Given
        ResourceText jsonExample = Mockito.mock(ResourceText.class);
        Mockito.doReturn(just(Examples.JsonPet.string())).when(jsonExample).data();

        ResourceText xmlExample = Mockito.mock(ResourceText.class);
        Mockito.doReturn(just(Examples.NoteXml.string())).when(xmlExample).data();

        MediaTypeObject jsonMediaType = new MediaTypeObject();
        jsonMediaType.setExample(jsonExample);

        MediaTypeObject xmlMediaType = new MediaTypeObject();
        xmlMediaType.setExample(xmlExample);

        Map<String, MediaTypeObject> content = new HashMap<>();
        content.put("application/json", jsonMediaType);
        content.put("text/xml", xmlMediaType);

        HeaderObject header1 = new HeaderObject();
        header1.setDescription("My header 1");

        HeaderObject header2 = new HeaderObject();
        header2.setDescription("My header 2");

        Map<String, HeaderObject> headers = new HashMap<>();
        headers.put("x-my-header1", header1);
        headers.put("x-my-header2", header2);

        ResponseObject response = new ResponseObject();
        response.setDescription("My response description");
        response.setContent(content);
        response.setHeaders(headers);

        // Expect
        OpenApiJsons.ResponseBodyObject withAllProperties = OpenApiJsons.ResponseBodyObject.WithAllProperties;

        ComponentsObject componentsObject = new ComponentsObject();
        OpenApiSerializableContext context = new OpenApiSerializableContext(componentsObject);
        JSONObject actualObject = response.serialize(context);
        JSONObject expectedObject = new JSONObject(withAllProperties.string());
        assertSameExamples(actualObject, expectedObject);
    }

    @Test
    void shouldCorrectlySerializeResponseBodyWithDefaults() {
        // Given
        ResponseObject response = new ResponseObject();

        // Expect
        assertSerializedCorrectly(response, OpenApiJsons.ResponseBodyObject.WithDefault);
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
