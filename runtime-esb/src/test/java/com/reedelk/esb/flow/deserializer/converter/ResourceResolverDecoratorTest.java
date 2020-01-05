package com.reedelk.esb.flow.deserializer.converter;

import com.reedelk.esb.module.DeSerializedModule;
import com.reedelk.esb.module.Module;
import com.reedelk.esb.services.resource.ResourceLoader;
import com.reedelk.runtime.api.resource.DynamicResource;
import com.reedelk.runtime.api.resource.ResourceBinary;
import com.reedelk.runtime.api.resource.ResourceNotFound;
import com.reedelk.runtime.api.resource.ResourceText;
import com.reedelk.runtime.converter.DeserializerConverter;
import com.reedelk.runtime.converter.DeserializerConverterContext;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceResolverDecoratorTest {

    private final int testBufferSize = 500;
    private final long testModuleId = 10L;
    private final String testModuleName = "Test module";
    private final DeserializerConverterContext factoryContext = new DeserializerConverterContext(testModuleId);

    @Mock
    private DeSerializedModule mockDeSerializedModule;
    @Mock
    private Module mockModule;

    private ResourceResolverDecorator decorator;
    private ResourceLoader testResourceLoader;

    @BeforeEach
    void setUp() {
        decorator = new ResourceResolverDecorator(DeserializerConverter.getInstance(), mockDeSerializedModule, mockModule);
        testResourceLoader = createResourceLoaderWith("/sample/path/file.txt", "my test file");
    }

    @Test
    void shouldIsPrimitiveDelegate() {
        // When
        boolean actual = decorator.isPrimitive(String.class);

        // Then
        assertThat(actual).isTrue();
    }

    // Happy cases

    @Test
    void shouldCorrectlyLoadResourceText() {
        // Given
        String content = "my sample content";
        String propertyName = "myResourceText";
        String propertyValue = "/assets/template.html";

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, propertyValue);

        doReturn(asList(testResourceLoader, createResourceLoaderWith(propertyValue, content)))
                .when(mockDeSerializedModule)
                .getResources();

        // When
        ResourceText actualResource = decorator.convert(ResourceText.class, componentDefinition, propertyName, factoryContext);

        // Then
        assertThat(actualResource.path()).isEqualTo(propertyValue);
        StepVerifier.create(actualResource.data())
                .expectNext(content)
                .verifyComplete();
    }

    @Test
    void shouldCorrectlyLoadResourceBinary() {
        // Given
        String content = "my sample content";
        String propertyName = "myResourceText";
        String propertyValue = "/assets/template.html";

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, propertyValue);

        doReturn(asList(testResourceLoader, createResourceLoaderWith(propertyValue, content)))
                .when(mockDeSerializedModule)
                .getResources();

        // When
        ResourceBinary actualResource = decorator.convert(ResourceBinary.class, componentDefinition, propertyName, factoryContext);

        // Then
        assertThat(actualResource.path()).isEqualTo(propertyValue);
        StepVerifier.create(actualResource.data())
                .expectNextMatches(bytes -> Arrays.equals(content.getBytes(), bytes))
                .verifyComplete();
    }

    @Test
    void shouldCorrectlyLoadDynamicResource() {
        // Given
        String content = "my sample content";
        String propertyName = "myResourceText";
        String propertyValue = "/assets/template.html";

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, propertyValue);

        doReturn(asList(testResourceLoader, createResourceLoaderWith(propertyValue, content)))
                .when(mockDeSerializedModule)
                .getResources();

        // When
        DynamicResource actualResource = decorator.convert(DynamicResource.class, componentDefinition, propertyName, factoryContext);
        Publisher<byte[]> data = actualResource.data(propertyValue, testBufferSize);

        // Then
        StepVerifier.create(data)
                .expectNextMatches(bytes -> Arrays.equals(content.getBytes(), bytes))
                .verifyComplete();
    }

    // Not happy case

    @Test
    void shouldCreateResourceTextThrowResourceNotFound() {
        // Given
        doReturn(testModuleId).when(mockModule).id();
        doReturn(testModuleName).when(mockModule).name();

        String propertyName = "myResourceText";
        String propertyValue = "/assets/template.html";

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, propertyValue);

        doReturn(singletonList(testResourceLoader))
                .when(mockDeSerializedModule)
                .getResources();

        // When
        ResourceNotFound thrown = assertThrows(ResourceNotFound.class,
                () -> decorator.convert(ResourceText.class, componentDefinition, propertyName, factoryContext));


        // Then
        assertThat(thrown).hasMessage("Could not find resource with path=[/assets/template.html] in module with id=[10], name=[Test module] defined in the project's 'src/main/resources' folder. Please make sure that the referenced resource exists at the given path.");
    }

    @Test
    void shouldCreateResourceBinaryThrowResourceNotFound() {
        // Given
        doReturn(testModuleId).when(mockModule).id();
        doReturn(testModuleName).when(mockModule).name();

        String propertyName = "myResourceText";
        String propertyValue = "/assets/template.html";

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, propertyValue);

        doReturn(singletonList(testResourceLoader))
                .when(mockDeSerializedModule)
                .getResources();

        // When
        ResourceNotFound thrown = assertThrows(ResourceNotFound.class,
                () -> decorator.convert(ResourceBinary.class, componentDefinition, propertyName, factoryContext));


        // Then
        assertThat(thrown).hasMessage("Could not find resource with path=[/assets/template.html] in module with id=[10], name=[Test module] defined in the project's 'src/main/resources' folder. Please make sure that the referenced resource exists at the given path.");
    }

    @Test
    void shouldCreateDynamicResourceThrowResourceNotFound() {
        // Given
        doReturn(testModuleId).when(mockModule).id();
        doReturn(testModuleName).when(mockModule).name();

        String propertyName = "myResourceText";
        String propertyValue = "#['/assets/' + 'template' + '.html']";
        String evaluatedPropertyValue = "/assets/template.html";

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, propertyValue);

        doReturn(singletonList(testResourceLoader))
                .when(mockDeSerializedModule)
                .getResources();

        DynamicResource actualResource = decorator.convert(DynamicResource.class, componentDefinition, propertyName, factoryContext);

        // When
        ResourceNotFound thrown = assertThrows(ResourceNotFound.class,
                () -> actualResource.data(evaluatedPropertyValue, testBufferSize));

        // Then
        assertThat(thrown)
                .hasMessage("Could not find resource with path=[/assets/template.html] " +
                        "(evaluated from=#['/assets/' + 'template' + '.html']) in module with id=[10], " +
                        "name=[Test module] defined in the project's 'src/main/resources' folder. " +
                        "Please make sure that the referenced resource exists at the given path.");
    }

    private ResourceLoader createResourceLoaderWith(String resourcePath, String resourceBody) {
        ResourceLoader resourceLoader = mock(ResourceLoader.class);
        lenient()
                .doReturn("/Users/user/project/src/main/resources" + resourcePath)
                .when(resourceLoader)
                .getResourceFilePath();
        lenient()
                .doReturn(Mono.just(resourceBody.getBytes()))
                .when(resourceLoader)
                .body();
        lenient()
                .doReturn(Mono.just(resourceBody.getBytes()))
                .when(resourceLoader)
                .body(testBufferSize);
        return resourceLoader;
    }
}