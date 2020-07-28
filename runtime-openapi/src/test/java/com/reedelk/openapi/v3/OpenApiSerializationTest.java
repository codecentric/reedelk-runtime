package com.reedelk.openapi.v3;

import com.reedelk.openapi.OpenApiSerializableContext1;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

public class OpenApiSerializationTest {

    private OpenApiObjectAbstract openApiObject;
    private OpenApiSerializableContext1 context;

    @BeforeEach
    void setUp() {
        context = new OpenApiSerializableContext1();

        Schema schema = new Schema("mySchemaId", Fixture.Schemas.Pet.string());

        MediaTypeObject mediaTypeObject = new MediaTypeObject();
        mediaTypeObject.setSchema(schema, context);

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

        openApiObject = new OpenApiObjectAbstract();
        openApiObject.setBasePath("/api/v3");
        openApiObject.setPaths(pathsObject);
    }

    // TODO: Fixme
    /**
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
    }*/
}
