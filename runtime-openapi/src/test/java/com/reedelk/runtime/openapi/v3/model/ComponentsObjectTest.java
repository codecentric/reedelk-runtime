package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.OpenApiJsons;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ComponentsObjectTest extends AbstractOpenApiSerializableTest {

    // TODO: Fixme
    /**
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
    }*/

    @Test
    void shouldCorrectlySerializeWhenNoSchemas() {
        // Given
        ComponentsObject componentsObject = new ComponentsObject();
        OpenApiSerializableContext context = new OpenApiSerializableContext(componentsObject);

        // Expect
        assertSerializedCorrectly(context, componentsObject, OpenApiJsons.ComponentsObject.WithNoSchemas);
    }
}
