package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.v3.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenApiSerializationTest {

    private OpenApiObject openApiObject;
    private OpenApiSerializableContext context;

    @BeforeEach
    void setUp() {
        context = new OpenApiSerializableContext();

        SchemaReference schemaReference = new SchemaReference("mySchemaId", Fixture.Schemas.Pet.string());

        MediaTypeObject mediaTypeObject = new MediaTypeObject();
        mediaTypeObject.setSchema(schemaReference, context);

        Map<String, MediaTypeObject> contentTypeMediaTypeObject = new HashMap<>();
        contentTypeMediaTypeObject.put("application/json", mediaTypeObject);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setDescription("Content description");
        responseObject.setContent(contentTypeMediaTypeObject);

        Map<String, ResponseObject> responseCodeResponseMap = new HashMap<>();
        responseCodeResponseMap.put("200", responseObject);

        OperationObject getOperation = new OperationObject();
        getOperation.setOperationId("getOperation");
        getOperation.setDescription("GET Operation Description");
        getOperation.setSummary("GET Operation Summary");
        getOperation.setResponses(responseCodeResponseMap);

        Map<RestMethod, OperationObject> methodsAndOperations = new HashMap<>();
        methodsAndOperations.put(RestMethod.GET, getOperation);

        Map<String, Map<RestMethod, OperationObject>> pathsMap = new HashMap<>();
        pathsMap.put("/order", methodsAndOperations);

        PathsObject pathsObject = new PathsObject();
        pathsObject.setPaths(pathsMap);

        openApiObject = new OpenApiObject();
        openApiObject.setBasePath("/api/v3");
        openApiObject.setPaths(pathsObject);
    }

    @Test
    void shouldCorrectlySerializeAsJSON() {
        // When
        String actual = openApiObject.toJson(context);

        // Then
        JSONAssert.assertEquals(Fixture.EndToEnd.SAMPLE_JSON.string(), actual, JSONCompareMode.STRICT);
    }

    @Test
    void shouldCorrectlySerializeAsYAML() {
        // When
        String actual = openApiObject.toYaml(context);

        // Then
        String expected = Fixture.EndToEnd.SAMPLE_YAML.string();
        assertThat(actual).isEqualToNormalizingWhitespace(expected);
    }
}
