package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.component.ComponentRegistry;
import de.codecentric.reedelk.platform.module.DeSerializedModule;
import de.codecentric.reedelk.platform.module.Module;
import de.codecentric.reedelk.platform.module.ModuleDeserializer;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import de.codecentric.reedelk.platform.module.state.ModuleState;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static de.codecentric.reedelk.platform.test.utils.Assertions.assertModuleErrorStateWith;
import static de.codecentric.reedelk.platform.test.utils.TestJson.*;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ModuleResolveDependenciesTest {

    @Mock
    private ModuleDeserializer deserializer;
    @Mock
    private ComponentRegistry componentRegistry;

    private ModuleResolveDependencies step;
    private Module aModule;

    @BeforeEach
    void setUp() {
        step = Mockito.spy(new ModuleResolveDependencies());
        doReturn(componentRegistry).when(step).componentRegistry();
        aModule = Module.builder()
                .name("test")
                .moduleId(23L)
                .version("0.9.0")
                .moduleFilePath("file:/module/path")
                .deserializer(deserializer)
                .build();
    }

    @Test
    void shouldReturnModuleWithStateInstalledWhenNoFlowsArePresent() {
        // Given
        DeSerializedModule deSerializedModule = new DeSerializedModule(emptySet(), emptySet(), emptySet(), emptySet(), emptySet());
        doReturn(deSerializedModule).when(deserializer).deserialize();

        // When
        Module module = step.run(aModule);

        // Then
        assertThat(module).isNotNull();
        assertThat(module.state()).isEqualTo(ModuleState.INSTALLED);
    }

    @Test
    void shouldReturnModuleWithStateUnresolvedWhenNotAllFlowComponentsArePresent() {
        // Given
        doReturn(singletonList("de.codecentric.reedelk.platform.not.found.Component"))
                .when(componentRegistry)
                .unregisteredComponentsOf(anyCollection());

        Set<JSONObject> flows = new HashSet<>();
        flows.add(FLOW_WITH_ROUTER.parse());

        DeSerializedModule deSerializedModule = new DeSerializedModule(flows, emptySet(), emptySet(), emptySet(), emptySet());
        doReturn(deSerializedModule).when(deserializer).deserialize();

        // When
        Module module = step.run(aModule);

        // Then
        assertThat(module).isNotNull();
        assertThat(module.state()).isEqualTo(ModuleState.UNRESOLVED);
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldReturnModuleWithStateUnresolvedWhenNotAllSubFlowComponentsArePresent() {
        // Given
        doAnswer(invocation -> {
            final Collection<String> argument = (Collection<String>) (invocation.getArguments())[0];
            if (argument.containsAll(asList("de.codecentric.reedelk.platform.test.utils.SubFlowComponent", "de.codecentric.reedelk.platform.test.utils.AnotherSubFlowComponent"))) {
                return singletonList("de.codecentric.reedelk.platform.test.utils.SubFlowComponent");
            }
            return emptyList();
        }).when(componentRegistry)
                .unregisteredComponentsOf(ArgumentMatchers.anyCollection());


        Set<JSONObject> flows = new HashSet<>();
        flows.add(FLOW_WITH_COMPONENTS.parse());

        Set<JSONObject> subFlows = new HashSet<>();
        subFlows.add(SUBFLOW_WITH_COMPONENTS.parse());

        DeSerializedModule deSerializedModule = new DeSerializedModule(flows, subFlows, emptySet(), emptySet(), emptySet());
        doReturn(deSerializedModule).when(deserializer).deserialize();

        // When
        Module module = step.run(aModule);

        // Then
        assertThat(module).isNotNull();
        assertThat(module.state()).isEqualTo(ModuleState.UNRESOLVED);
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldReturnModuleWithStateUnresolvedWhenNotAllConfigComponentsArePresent() {
        // Given
        doAnswer(invocation -> {
            final Collection<String> originalArgument = (Collection<String>) (invocation.getArguments())[0];
            if (originalArgument.contains("de.codecentric.reedelk.platform.test.utils.TestConfiguration")) {
                return singletonList("de.codecentric.reedelk.platform.test.utils.TestConfiguration");
            }
            return emptyList();
        }).when(componentRegistry)
                .unregisteredComponentsOf(ArgumentMatchers.anyCollection());

        Set<JSONObject> flows = new HashSet<>();
        flows.add(FLOW_WITH_COMPONENTS.parse());

        Set<JSONObject> config = new HashSet<>();
        config.add(CONFIG.parse());

        DeSerializedModule deSerializedModule = new DeSerializedModule(flows, emptySet(), config, emptySet(), emptySet());
        doReturn(deSerializedModule).when(deserializer).deserialize();

        // When
        Module module = step.run(aModule);

        // Then
        assertThat(module).isNotNull();
        assertThat(module.state()).isEqualTo(ModuleState.UNRESOLVED);
    }

    @Test
    void shouldReturnModuleWithStateResolvedWhenAllComponentsArePresent() {
        // Given
        doReturn(emptyList())
                .when(componentRegistry)
                .unregisteredComponentsOf(anyCollection());

        Set<JSONObject> flows = new HashSet<>();
        flows.add(FLOW_WITH_ROUTER.parse());

        DeSerializedModule deSerializedModule = new DeSerializedModule(flows, emptySet(), emptySet(), emptySet(), emptySet());
        doReturn(deSerializedModule).when(deserializer).deserialize();

        // When
        Module module = step.run(aModule);

        // Then
        assertThat(module).isNotNull();
        assertThat(module.state()).isEqualTo(ModuleState.RESOLVED);
    }

    @Test
    void shouldReturnModuleWithStateErrorWhenDeserializationThrowsException() {
        // Given
        doThrow(new JSONException("Could not deserialize module")).when(deserializer).deserialize();

        // When
        Module module = step.run(aModule);

        // Then
        String expectedMessage = "{\n" +
                "  \"moduleName\": \"test\",\n" +
                "  \"errorMessage\": \"Could not deserialize module\",\n" +
                "  \"moduleId\": 23,\n" +
                "  \"errorType\": \"org.json.JSONException\"\n" +
                "}";
        assertModuleErrorStateWith(module, expectedMessage);
    }

    @Test
    void shouldReturnModuleWithStateUnresolvedWithCorrectResolvedAndUnresolvedSets() {
        // Given
        doReturn(asList("de.codecentric.reedelk.platform.test.utils.AnotherTestComponent", "de.codecentric.reedelk.platform.test.utils.TestInboundComponent"))
                .when(componentRegistry)
                .unregisteredComponentsOf(anyCollection());

        Set<JSONObject> flows = new HashSet<>();
        flows.add(FLOW_WITH_ROUTER.parse());

        DeSerializedModule deSerializedModule = new DeSerializedModule(flows, emptySet(), emptySet(), emptySet(), emptySet());
        doReturn(deSerializedModule).when(deserializer).deserialize();

        // When
        Module module = step.run(aModule);

        // Then
        assertThat(module.state()).isEqualTo(ModuleState.UNRESOLVED);
        Collection<String> unresolvedComponents = module.unresolvedComponents();
        Collection<String> resolvedComponents = module.resolvedComponents();

        assertThat(unresolvedComponents).containsExactlyInAnyOrder(
                "de.codecentric.reedelk.platform.test.utils.AnotherTestComponent",
                "de.codecentric.reedelk.platform.test.utils.TestInboundComponent");

        assertThat(resolvedComponents).containsExactlyInAnyOrder(
                "de.codecentric.reedelk.runtime.component.Router");
    }

    @Test
    void shouldNotResolveDependenciesWhenModuleWithStateError() {
        // Given
        aModule.error(new PlatformException("Module in error state!"));

        // When
        Module module = step.run(aModule);

        // Then
        assertModuleErrorStateWith(module, "Module in error state!");
        verifyNoMoreInteractions(componentRegistry, deserializer);
    }
}
