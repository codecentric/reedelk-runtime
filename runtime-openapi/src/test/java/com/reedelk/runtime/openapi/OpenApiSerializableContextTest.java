package com.reedelk.runtime.openapi;

import com.reedelk.runtime.api.resource.ResourceText;
import com.reedelk.runtime.openapi.model.ComponentsObject;
import com.reedelk.runtime.openapi.model.SchemaObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static reactor.core.publisher.Mono.just;

@ExtendWith(MockitoExtension.class)
class OpenApiSerializableContextTest {

    private OpenApiSerializableContext context;

    @Mock
    private ResourceText schema;
    private ComponentsObject componentsObject;

    @BeforeEach
    void setUp() {
        componentsObject = new ComponentsObject();
        context = new OpenApiSerializableContext(componentsObject);
    }

    @Test
    void shouldAddSchemaWhenNotPresentInComponentsObject() {
        // Given
        Mockito.doReturn(just("{}")).when(schema).data();
        Mockito.doReturn("/assets/car.schema.json").when(schema).path();

        // When
        String schemaReference = context.schemaReferenceOf(schema);

        // Then
        Assertions.assertThat(schemaReference).isEqualTo("#/components/schemas/car");

        Map<String, SchemaObject> schemas = componentsObject.getSchemas();
        Assertions.assertThat(schemas).containsOnlyKeys("car");

        SchemaObject schemaObject = schemas.get("car");
        Assertions.assertThat(schemaObject.getSchema()).isEqualTo(schema);
    }

    @Test
    void shouldNotAddSchemaWhenAlreadyPresentInComponentsObject() {
        // Given
        Mockito.doReturn("/assets/car.schema.json").when(schema).path();
        SchemaObject schemaObject = new SchemaObject();
        schemaObject.setSchema(schema);
        componentsObject.getSchemas().put("car", schemaObject);

        // When
        String schemaReference = context.schemaReferenceOf(schema);

        // Then
        Assertions.assertThat(schemaReference).isEqualTo("#/components/schemas/car");
        Assertions.assertThat(componentsObject.getSchemas()).containsOnlyKeys("car");
    }
}
