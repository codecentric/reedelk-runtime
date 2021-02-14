package de.codecentric.reedelk.platform.lifecycle;

import de.codecentric.reedelk.platform.module.DeSerializedModule;
import de.codecentric.reedelk.platform.module.Module;
import de.codecentric.reedelk.platform.module.ModuleDeserializer;
import de.codecentric.reedelk.platform.module.ModulesManager;
import de.codecentric.reedelk.platform.module.state.ModuleState;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.osgi.framework.Bundle;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static de.codecentric.reedelk.platform.test.utils.Assertions.assertModuleErrorStateWith;
import static de.codecentric.reedelk.platform.test.utils.TestJson.*;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ModuleValidateTest {

    private final long moduleId = 232L;
    private final String testModuleName = "TestModule";
    private final String testVersion = "0.9.0";
    private final String testLocation = "file://location/test";

    @Mock
    private Bundle bundle;
    @Mock
    private ModulesManager modulesManager;
    @Mock
    private ModuleDeserializer deserializer;
    @Spy
    private ModuleValidate step;

    @BeforeEach
    void setUp() {
        doReturn(bundle).when(step).bundle();
        doReturn(modulesManager).when(step).modulesManager();
    }

    @Test
    void shouldTransitionToErrorStateWhenThereAreTwoFlowsWithSameId() {
        // Given
        Module inputModule = newInstalledModule();

        Set<JSONObject> flows = new HashSet<>();
        flows.add(FLOW_WITH_COMPONENTS.parse()); // by adding the same flow twice we simulate two flows with the same ID.
        flows.add(FLOW_WITH_COMPONENTS.parse());

        DeSerializedModule deSerializedModule = new DeSerializedModule(flows, emptySet(), emptySet(), emptySet(), emptySet());
        doReturn(deSerializedModule).when(deserializer).deserialize();

        // When
        Module module = step.run(inputModule);

        // Then
        assertModuleErrorStateWith(module, "Error validating module with name=[TestModule]: There are at least two flows with the same ID. Flow IDs must be unique.");
    }

    @Test
    void shouldTransitionToErrorStateWhenFlowDoesNotContainAnId() {
        // Given
        Module inputModule = newInstalledModule();

        Set<JSONObject> flows = Collections.singleton(FLOW_WITHOUT_ID.parse());

        DeSerializedModule deSerializedModule = new DeSerializedModule(flows, emptySet(), emptySet(), emptySet(), emptySet());
        doReturn(deSerializedModule).when(deserializer).deserialize();

        // When
        Module module = step.run(inputModule);

        // Then
        assertModuleErrorStateWith(module, "Error validating module with name=[TestModule]: The 'id' property must be defined and not empty in any JSON flow definition.");
    }

    @Test
    void shouldValidateModuleSuccessfully() {
        // Given
        Module inputModule = newInstalledModule();

        Set<JSONObject> flows = new HashSet<>();
        flows.add(FLOW_WITH_COMPONENTS.parse());
        flows.add(FLOW_WITH_ROUTER.parse());

        DeSerializedModule deSerializedModule = new DeSerializedModule(flows, emptySet(), emptySet(), emptySet(), emptySet());
        doReturn(deSerializedModule).when(deserializer).deserialize();

        // When
        Module module = step.run(inputModule);

        // Then
        assertThat(module.state()).isEqualTo(ModuleState.INSTALLED);
    }

    private Module newInstalledModule() {
        return Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
    }
}
