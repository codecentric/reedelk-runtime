package com.reedelk.esb.services.resource;

import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.resource.DynamicResource;
import com.reedelk.runtime.api.resource.ResourceFile;
import com.reedelk.runtime.api.resource.ResourceNotFound;
import com.reedelk.runtime.api.resource.ResourceService;
import com.reedelk.runtime.api.script.ScriptEngineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyZeroInteractions;

@ExtendWith(MockitoExtension.class)
class DefaultResourceServiceTest {

    private final long testModuleId = 234L;
    private final ModuleContext moduleContext = new ModuleContext(testModuleId);

    @Mock
    private Message message;
    @Mock
    private FlowContext flowContext;
    @Mock
    private ScriptEngineService scriptEngineService;

    private final int testBufferSize = 500;

    private ResourceService fileProvider;

    @BeforeEach
    void setUp() {
        fileProvider = new DefaultResourceService(scriptEngineService);
    }

    @Test
    void shouldCorrectlyReturnFileBytes() {
        // Given
        String content = "my content";
        DynamicResource dynamicResource = DynamicResourceFrom("#['myTemplate' + '.html']", content);

        doReturn(Optional.of("myTemplate.html"))
                .when(scriptEngineService)
                .evaluate(dynamicResource, flowContext, message);

        // When
        ResourceFile<byte[]> resourceFile = fileProvider.find(dynamicResource, testBufferSize, flowContext, message);


        // Then
        Publisher<byte[]> fileDataStream = resourceFile.data();
        StepVerifier.create(fileDataStream)
                .expectNextMatches(bytes -> Arrays.equals(content.getBytes(), bytes))
                .verifyComplete();
        assertThat(resourceFile.path()).isEqualTo("myTemplate.html");
    }

    @Test
    void shouldThrowFileNotFoundExceptionDynamicResourceIsNull() {
        // Given
        DynamicResource dynamicResource = null;

        // When
        ResourceNotFound thrown = assertThrows(ResourceNotFound.class,
                () -> fileProvider.find(dynamicResource, testBufferSize, flowContext, message));

        // Then
        assertThat(thrown).isNotNull();
        assertThat(thrown).hasMessage("Resource could not be found: dynamic resource object was null");
        verifyZeroInteractions(scriptEngineService);
    }

    @Test
    void shouldThrowFileNotFoundExceptionWhenDynamicResourceEvaluatesEmpty() {
        // Given
        DynamicResource dynamicResource = DynamicResourceFrom(null, "anything");

        doReturn(Optional.empty())
                .when(scriptEngineService)
                .evaluate(dynamicResource, flowContext, message);

        // When
        ResourceNotFound thrown = assertThrows(ResourceNotFound.class,
                () -> fileProvider.find(dynamicResource, testBufferSize, flowContext, message));

        // Then
        assertThat(thrown).isNotNull();
        assertThat(thrown).hasMessage("Resource could not be found: dynamic resource path was=[null]");
    }

    @Test
    void shouldRethrowExceptionThrownWhenResourceLoaded() {
        // Given
        DynamicResource dynamicResource = DynamicResource.from("does not matter", moduleContext);
        DynamicResource dynamicResourceProxy = new TestDynamicResourceProxyThrowingNotFoundExceptionResource(dynamicResource);

        doReturn(Optional.of("/assets/templates/hello-template.html"))
                .when(scriptEngineService)
                .evaluate(dynamicResourceProxy, flowContext, message);

        // When
        ResourceNotFound thrown = assertThrows(ResourceNotFound.class,
                () -> fileProvider.find(dynamicResourceProxy, testBufferSize, flowContext, message));

        // Then
        assertThat(thrown).isNotNull();
        assertThat(thrown).hasMessage("Could not find resource xyz");
    }

    private DynamicResource DynamicResourceFrom(Object body, String wantedContent) {
        DynamicResource dynamicResource = DynamicResource.from(body, moduleContext);
        return new TestDynamicResourceProxy(dynamicResource, wantedContent);
    }

    class TestDynamicResourceProxy extends DynamicResource {

        private final String expectedResult;

        TestDynamicResourceProxy(DynamicResource original, String expectedResult) {
            super(original);
            this.expectedResult = expectedResult;
        }

        @Override
        public Publisher<byte[]> data(String evaluatedPath, int readBufferSize) {
            return Mono.just(expectedResult.getBytes());
        }
    }

    class TestDynamicResourceProxyThrowingNotFoundExceptionResource extends DynamicResource {

        TestDynamicResourceProxyThrowingNotFoundExceptionResource(DynamicResource original) {
            super(original);
        }

        @Override
        public Publisher<byte[]> data(String evaluatedPath, int readBufferSize) {
            throw new ResourceNotFound("Could not find resource xyz");
        }
    }
}