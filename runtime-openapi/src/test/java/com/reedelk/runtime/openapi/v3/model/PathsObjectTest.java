package com.reedelk.runtime.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.Fixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PathsObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializePathsWithDefault() {
        // Given
        PathsObject paths = new PathsObject();

        // Expect
        assertSerializeJSON(paths, Fixture.PathsObject.WithDefaultPaths);
    }
/**
    @Test
    void shouldCorrectlySerializePathsWithOperationObjectForPath() {
        // Given
        ResourceText successResponse1Example = mock(ResourceText.class);
        doReturn(Mono.just("{ \"name\": \"Mark\" }")).when(successResponse1Example).data();

        MediaTypeObject successResponse1 = new MediaTypeObject();
        successResponse1.setExample(successResponse1Example);

        Map<String, MediaTypeObject> responses200Contents = of("application/json", successResponse1);

        ResponseObject response200 = new ResponseObject();
        response200.setDescription("200 Response");
        response200.setContent(responses200Contents);

        ResponseObject response401 = new ResponseObject();
        response401.setDescription("401 Response");

        OperationObject operationObject = new OperationObject();
        operationObject.setDescription("My response description");
        operationObject.setOperationId("myOperationId");
        operationObject.setSummary("My summary");
        operationObject.setResponses(of("200", response200, "401", response401));
        operationObject.setTags(Arrays.asList("tag1", "tag2"));

        Map<RestMethod, OperationObject> methodOperation = new HashMap<>();
        methodOperation.put(RestMethod.GET, operationObject);

        PathsObject paths = new PathsObject();
        paths.getPaths().put("/mypath", methodOperation);


        // Expect
        assertSerializedCorrectly(paths, OpenApiJsons.PathsObject.WithOperation);
    }*/
}
