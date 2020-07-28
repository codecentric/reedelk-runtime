package com.reedelk.openapi.v3.model;

import com.reedelk.openapi.v3.Fixture;
import com.reedelk.openapi.v3.HeaderObject;
import com.reedelk.openapi.v3.ParameterStyle;
import org.junit.jupiter.api.Test;

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

        // Expect
        assertSerializeJSON(header, Fixture.HeaderObject.WithAllPropertiesAndDefaultSchema);
    }

    /**
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
    }**/

    @Test
    void shouldCorrectlySerializeHeaderWithDefaults() {
        // Given
        HeaderObject header = new HeaderObject();

        // Expect
        assertSerializeJSON(header, Fixture.HeaderObject.WithDefaults);
    }
}
