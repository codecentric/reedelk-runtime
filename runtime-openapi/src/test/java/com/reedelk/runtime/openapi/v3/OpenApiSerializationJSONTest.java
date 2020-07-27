package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.v3.model.*;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.HashMap;
import java.util.Map;

public class OpenApiSerializationJSONTest {

    @Test
    void shouldCorrectlySerializeAsJSON() {
        // Given
        OpenApiSerializableContext context = new OpenApiSerializableContext();
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
        OpenApiObject openApiObject = new OpenApiObject();
        openApiObject.setBasePath("/api/v3");
        openApiObject.setPaths(pathsObject);

        // When
        String actual = openApiObject.toJson(context);

        // Then
        JSONAssert.assertEquals(Fixture.EndToEnd.SAMPLE.string(), actual, JSONCompareMode.STRICT);
    }
}
