package com.reedelk.openapi.v3.model;

import com.reedelk.openapi.OpenApiSerializableContext1;
import com.reedelk.openapi.v3.ComponentsObject;
import com.reedelk.openapi.v3.Fixture;
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
        OpenApiSerializableContext1 context = new OpenApiSerializableContext1();

        // Expect
        assertSerializeJSON(context, componentsObject, Fixture.ComponentsObject.WithNoSchemas);
    }
}
