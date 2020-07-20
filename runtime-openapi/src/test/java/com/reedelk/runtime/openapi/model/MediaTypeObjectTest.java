package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.api.resource.ResourceText;
import com.reedelk.runtime.openapi.OpenApiJsons;
import com.reedelk.runtime.openapi.OpenApiSerializableContext;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static reactor.core.publisher.Mono.just;

@ExtendWith(MockitoExtension.class)
public class MediaTypeObjectTest extends AbstractOpenApiSerializableTest {

    @Mock
    private ResourceText schema;
    @Mock
    private ResourceText example;

    @Test
    void shouldCorrectlySerializeMediaTypeWithSchema() {
        // Given
        Mockito.doReturn(just(OpenApiJsons.Schemas.Pet.string())).when(schema).data();

        MediaTypeObject mediaType = new MediaTypeObject();
        mediaType.setSchema(schema);

        // Expect
        assertSerializedCorrectly(mediaType, OpenApiJsons.MediaTypeObject.WithSchema);
    }

    @Test
    void shouldCorrectlySerializeMediaTypeWithExample() {
        // Given
        Mockito.doReturn(just(OpenApiJsons.Examples.JsonPet.string())).when(example).data();

        MediaTypeObject mediaType = new MediaTypeObject();
        mediaType.setExample(example);

        // Expect
        OpenApiJsons.MediaTypeObject withExample = OpenApiJsons.MediaTypeObject.WithExample;

        ComponentsObject componentsObject = new ComponentsObject();
        OpenApiSerializableContext context = new OpenApiSerializableContext(componentsObject);
        JSONObject actualObject = mediaType.serialize(context);
        JSONObject expectedObject = new JSONObject(withExample.string());
        Assertions.assertThat(actualObject.getString("example"))
                .isEqualToIgnoringNewLines(expectedObject.getString("example"));
    }

    @Test
    void shouldCorrectlySerializeMediaTypeWithSchemaAndExample() {
        // Given
        Mockito.doReturn(just(OpenApiJsons.Examples.JsonPet.string())).when(example).data();
        Mockito.doReturn(just(OpenApiJsons.Schemas.Pet.string())).when(schema).data();

        MediaTypeObject mediaType = new MediaTypeObject();
        mediaType.setExample(example);
        mediaType.setSchema(schema);

        // Expect
        OpenApiJsons.MediaTypeObject withSchemaAndExample = OpenApiJsons.MediaTypeObject.WithSchemaAndExample;

        ComponentsObject componentsObject = new ComponentsObject();
        OpenApiSerializableContext context = new OpenApiSerializableContext(componentsObject);
        JSONObject actualObject = mediaType.serialize(context);
        JSONObject expectedObject = new JSONObject(withSchemaAndExample.string());
        Assertions.assertThat(actualObject.getString("example"))
                .isEqualToIgnoringNewLines(expectedObject.getString("example"));
    }

    @Test
    void shouldCorrectlySerializeMediaTypeDefault() {
        // Given
        MediaTypeObject mediaType = new MediaTypeObject();

        // Expect
        assertSerializedCorrectly(mediaType, OpenApiJsons.MediaTypeObject.WithDefault);
    }
}
