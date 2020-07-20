package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.api.resource.ResourceText;
import com.reedelk.runtime.openapi.OpenApiJsons;
import com.reedelk.runtime.openapi.OpenApiSerializableContext;
import com.reedelk.runtime.openapi.PredefinedSchema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static reactor.core.publisher.Mono.just;

@ExtendWith(MockitoExtension.class)
class ComponentsObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeSchema() {
        // Given
        ResourceText stringSchema = Mockito.mock(ResourceText.class);
        Mockito.doReturn(just(PredefinedSchema.STRING.schema())).when(stringSchema).data();
        SchemaObject stringSchemaObject = new SchemaObject();
        stringSchemaObject.setSchema(stringSchema);

        ResourceText integerSchema = Mockito.mock(ResourceText.class);
        Mockito.doReturn(just(PredefinedSchema.INTEGER.schema())).when(integerSchema).data();
        SchemaObject integerSchemaObject = new SchemaObject();
        integerSchemaObject.setSchema(integerSchema);

        ComponentsObject componentsObject = new ComponentsObject();

        Map<String, SchemaObject> schemas = componentsObject.getSchemas();
        schemas.put("MyString", stringSchemaObject);
        schemas.put("MyInteger", integerSchemaObject);


        OpenApiSerializableContext context = new OpenApiSerializableContext(componentsObject);

        // Expect
        assertSerializedCorrectly(context, componentsObject, OpenApiJsons.ComponentsObject.WithSampleSchemas);
    }

    @Test
    void shouldCorrectlySerializeWhenNoSchemas() {
        // Given
        ComponentsObject componentsObject = new ComponentsObject();
        OpenApiSerializableContext context = new OpenApiSerializableContext(componentsObject);

        // Expect
        assertSerializedCorrectly(context, componentsObject, OpenApiJsons.ComponentsObject.WithNoSchemas);
    }
}
