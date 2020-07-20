package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.api.resource.ResourceText;
import com.reedelk.runtime.openapi.OpenApiJsons;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.reedelk.runtime.api.commons.ImmutableMap.of;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PathsObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializePathsWithDefault() {
        // Given
        PathsObject paths = new PathsObject();

        // Expect
        assertSerializedCorrectly(paths, OpenApiJsons.PathsObject.WithDefaultPaths);
    }

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
    }
}
