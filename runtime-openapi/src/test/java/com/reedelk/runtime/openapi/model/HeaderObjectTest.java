package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.api.resource.ResourceText;
import com.reedelk.runtime.openapi.OpenApiJsons;
import com.reedelk.runtime.openapi.PredefinedSchema;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static reactor.core.publisher.Mono.just;

class HeaderObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeHeaderWithAllPropertiesAndDefaultSchema() {
        // Given
        HeaderObject header = new HeaderObject();
        header.setAllowReserved(true);
        header.setDeprecated(true);
        header.setExplode(true);
        header.setExample("my header value");
        header.setDescription("My header description");
        header.setStyle(ParameterStyle.spaceDelimited);
        header.setPredefinedSchema(PredefinedSchema.ARRAY_STRING);

        // Expect
        assertSerializedCorrectly(header, OpenApiJsons.HeaderObject.WithAllPropertiesAndDefaultSchema);
    }

    @Test
    void shouldCorrectlySerializeHeaderWithCustomSchema() {
        // Given
        ResourceText schema = Mockito.mock(ResourceText.class);
        Mockito.doReturn(just(OpenApiJsons.Schemas.Pet.string())).when(schema).data();
        Mockito.doReturn("/assets/pet.schema.json").when(schema).path();

        HeaderObject header = new HeaderObject();
        header.setAllowReserved(true);
        header.setDeprecated(true);
        header.setExplode(true);
        header.setExample("my header value");
        header.setDescription("My header description");
        header.setStyle(ParameterStyle.spaceDelimited);
        header.setPredefinedSchema(PredefinedSchema.NONE);
        header.setSchema(schema);

        // Expect
        assertSerializedCorrectly(header, OpenApiJsons.HeaderObject.WithAllPropertiesAndCustomSchema);
    }

    @Test
    void shouldCorrectlySerializeHeaderWithDefaults() {
        // Given
        HeaderObject header = new HeaderObject();

        // Expect
        assertSerializedCorrectly(header, OpenApiJsons.HeaderObject.WithDefaults);
    }
}
