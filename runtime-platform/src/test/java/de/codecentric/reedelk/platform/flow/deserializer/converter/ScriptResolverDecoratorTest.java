package de.codecentric.reedelk.platform.flow.deserializer.converter;

import de.codecentric.reedelk.platform.services.resource.ResourceLoader;
import de.codecentric.reedelk.platform.module.DeSerializedModule;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import de.codecentric.reedelk.runtime.api.resource.ResourceNotFound;
import de.codecentric.reedelk.runtime.api.script.Script;
import de.codecentric.reedelk.runtime.converter.DeserializerConverter;
import de.codecentric.reedelk.runtime.converter.DeserializerConverterContext;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScriptResolverDecoratorTest {

    private final long testModuleId = 10L;
    private final DeserializerConverterContext factoryContext = new DeserializerConverterContext(testModuleId);

    @Mock
    private DeSerializedModule mockDeSerializedModule;

    private ScriptResolverDecorator decorator;

    @BeforeEach
    void setUp() {
        decorator = new ScriptResolverDecorator(DeserializerConverter.getInstance(), mockDeSerializedModule);
    }

    /**
     * We expect that the script resource path in the Script
     * object is replaced with the script function body.
     */
    @Test
    void shouldCorrectlyLoadScriptFunctionBody() {
        // Given
        String propertyName = "integrationScript";

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, "/integration/map_data.js");

        String scriptResource1Body = "return 'map data'";
        String scriptResource2Body = "return 'my script'";
        ResourceLoader resourceLoader1 = mockResourceLoader("/user/local/project/myProject/src/main/resources/scripts/integration/map_data.js", scriptResource1Body);
        ResourceLoader resourceLoader2 = mockResourceLoader("/user/local/project/myProject/src/main/resources/scripts/integration/my_script.js", scriptResource2Body);
        Collection<ResourceLoader> resourceLoaders = Arrays.asList(resourceLoader1, resourceLoader2);

        doReturn(resourceLoaders).when(mockDeSerializedModule).getScripts();

        // When
        Script actualScript = decorator.convert(Script.class, componentDefinition, propertyName, factoryContext);

        // Then
        assertThat(actualScript.functionName()).isNotNull();
        assertThat(actualScript.functionName()).contains("_" + testModuleId + "_"); // make sure that the function UUID contains the module id in its name as well.
        Assertions.assertThat(actualScript.getContext().getModuleId()).isEqualTo(testModuleId);
        assertThat(actualScript.body()).isEqualTo(scriptResource1Body);
    }

    @Test
    void shouldThrowExceptionWhenScriptFunctionBodyCouldNotBeLoadedBecauseScriptPathDoesNotMatchDeSerializedModuleScriptResources() {
        // Given
        String propertyName = "integrationScript";

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, "/integration/not_existent.js");

        String scriptResource1Body = "return 'map data'";
        String scriptResource2Body = "return 'my script'";
        ResourceLoader resourceLoader1 = mockResourceLoader("/user/local/project/myProject/src/main/resources/scripts/integration/map_data.js", scriptResource1Body);
        ResourceLoader resourceLoader2 = mockResourceLoader("/user/local/project/myProject/src/main/resources/scripts/integration/my_script.js", scriptResource2Body);
        Collection<ResourceLoader> resourceLoaders = Arrays.asList(resourceLoader1, resourceLoader2);

        doReturn(resourceLoaders).when(mockDeSerializedModule).getScripts();

        // When
        ResourceNotFound thrown = assertThrows(ResourceNotFound.class,
                () -> decorator.convert(Script.class, componentDefinition, propertyName, factoryContext));

        // Then
        assertThat(thrown).hasMessage("Could not find script named=[/integration/not_existent.js] defined in resources/scripts folder. Please make sure that the referenced script exists.");
    }

    @Test
    void shouldThrowExceptionWhenScriptFunctionBodyCouldNotBeLoadedBecauseScriptPathIsEmpty() {
        // Given
        String propertyName = "integrationScript";

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, "");

        // When
        PlatformException thrown = assertThrows(PlatformException.class,
                () -> decorator.convert(Script.class, componentDefinition, propertyName, factoryContext));

        // Then
        assertThat(thrown).hasMessage("A script resource file must not be null or empty");
    }

    private ResourceLoader mockResourceLoader(String resourcePath, String resourceBody) {
        ResourceLoader resourceLoader = mock(ResourceLoader.class);
        lenient().doReturn(resourcePath).when(resourceLoader).getResourceFilePath();
        lenient().doReturn(resourceBody).when(resourceLoader).bodyAsString();
        return resourceLoader;
    }
}
