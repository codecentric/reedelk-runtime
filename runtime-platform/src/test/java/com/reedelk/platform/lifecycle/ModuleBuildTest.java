package com.reedelk.platform.lifecycle;

import com.reedelk.platform.component.fork.ForkWrapper;
import com.reedelk.platform.component.router.RouterWrapper;
import com.reedelk.platform.flow.Flow;
import com.reedelk.platform.graph.ExecutionNode;
import com.reedelk.platform.graph.ExecutionNode.ReferencePair;
import com.reedelk.platform.module.DeSerializedModule;
import com.reedelk.platform.module.Module;
import com.reedelk.platform.module.ModuleDeserializer;
import com.reedelk.platform.module.ModulesManager;
import com.reedelk.platform.test.utils.AnotherInboundTestComponent;
import com.reedelk.platform.test.utils.TestComponent;
import com.reedelk.platform.test.utils.TestInboundComponent;
import com.reedelk.runtime.api.component.Component;
import com.reedelk.runtime.api.configuration.ConfigurationService;
import com.reedelk.runtime.api.exception.PlatformException;
import com.reedelk.runtime.component.Fork;
import com.reedelk.runtime.component.Router;
import com.reedelk.runtime.component.Stop;
import org.json.JSONException;
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
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceObjects;
import org.osgi.framework.ServiceReference;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.reedelk.platform.module.state.ModuleState.*;
import static com.reedelk.platform.test.utils.Assertions.assertModuleErrorStateWith;
import static com.reedelk.platform.test.utils.TestJson.*;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ModuleBuildTest {

    private final long moduleId = 232L;
    private final String testModuleName = "TestModule";
    private final String testVersion = "0.9.0";
    private final String testLocation = "file://location/test";

    private final Collection<String> unresolvedComponents = asList("com.reedelk.platform.UnresolvedComponent1", "com.reedelk.platform.UnresolvedComponent1");
    private final Collection<String> resolvedComponents = asList("com.reedelk.platform.ResolvedComponent1", "com.reedelk.platform.ResolvedComponent2");

    @Mock
    private Flow flow;
    @Mock
    private Bundle bundle;
    @Mock
    private BundleContext bundleContext;
    @Mock
    private ServiceObjects<?> serviceObjects;
    @Mock
    private ModulesManager modulesManager;
    @Mock
    private ModuleDeserializer deserializer;
    @Mock
    private ConfigurationService configurationService;
    @Mock
    private ServiceReference<Component> serviceReference;
    @Mock
    private Module module;
    @Spy
    private ModuleBuild step;

    @BeforeEach
    void setUp() {
        doReturn(moduleId).when(bundle).getBundleId();
        doReturn(bundle).when(step).bundle();
        doReturn(modulesManager).when(step).modulesManager();
        doReturn(configurationService).when(step).configurationService();
        doReturn(bundleContext).when(bundle).getBundleContext();
        doReturn(bundle).when(bundleContext).getBundle(moduleId);
        doReturn(module).when(modulesManager).getModuleById(moduleId);
        doReturn(moduleId).when(module).id();
    }

    @Test
    void shouldNotBuildModuleAndKeepModuleWithStateInstalled() {
        // Given
        Module inputModule = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();

        // When
        Module module = step.run(inputModule);

        // Then
        assertThat(module.state()).isEqualTo(INSTALLED);
    }

    @Test
    void shouldNotBuildModuleAndKeepModuleWithStateError() {
        // Given
        Module inputModule = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        inputModule.error(new PlatformException("Module in error state!"));

        // When
        Module module = step.run(inputModule);

        // Then
        assertModuleErrorStateWith(module, "Module in error state!");
    }

    @Test
    void shouldNotBuildModuleAndKeepModuleWithStateUnresolved() {
        // Given
        Module inputModule = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        inputModule.unresolve(unresolvedComponents, resolvedComponents);

        // When
        Module module = step.run(inputModule);

        // Then
        assertThat(module.state()).isEqualTo(UNRESOLVED);
    }

    @Test
    void shouldNotBuildModuleAndKeepModuleWithStateStopped() {
        // Given
        Module inputModule = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        inputModule.unresolve(unresolvedComponents, resolvedComponents);
        inputModule.resolve(resolvedComponents);
        inputModule.stop(unmodifiableList(singletonList(flow)));

        // When
        Module module = step.run(inputModule);

        // Then
        assertThat(module.state()).isEqualTo(STOPPED);
    }

    @Test
    void shouldNotBuildModuleAndKeepModuleWithStateStarted() {
        // Given
        Module inputModule = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        inputModule.unresolve(unresolvedComponents, resolvedComponents);
        inputModule.resolve(resolvedComponents);
        inputModule.stop(unmodifiableList(singletonList(flow)));
        inputModule.start(unmodifiableList(singletonList(flow)));

        // When
        Module module = step.run(inputModule);

        // Then
        assertThat(module.state()).isEqualTo(STARTED);
    }

    @Test
    void shouldBuildModuleWhenStateIsResolvedAndTransitionToStopped() {
        // Given
        Module inputModule = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        inputModule.unresolve(unresolvedComponents, resolvedComponents);
        inputModule.resolve(resolvedComponents);

        Set<JSONObject> flows = new HashSet<>();
        flows.add(FLOW_WITH_COMPONENTS.parse());

        DeSerializedModule deSerializedModule = new DeSerializedModule(flows, emptySet(), emptySet(), emptySet(), emptySet());
        doReturn(deSerializedModule).when(deserializer).deserialize();

        mockComponentWithServiceReference(TestInboundComponent.class);
        mockComponentWithServiceReference(TestComponent.class);
        mockComponent(Stop.class);

        // When
        Module module = step.run(inputModule);

        // Then
        assertThat(module.state()).isEqualTo(STOPPED);

        Collection<Flow> constructedFlows = module.flows();
        assertThat(constructedFlows).hasSize(1);

        Flow next = module.flows().iterator().next();
        assertThat(next.getFlowId()).isEqualTo("45a5ce60-5c9d-4075-82ab-d3fa9284f52a");
        assertThat(next.getFlowTitle()).contains("Flow with components title");
        assertThat(next.isStarted()).isFalse();
    }

    @Test
    void shouldTransitionToErrorStateWhenJsonIsNotDeserializable() {
        // Given
        Module inputModule = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        inputModule.unresolve(unresolvedComponents, resolvedComponents);
        inputModule.resolve(resolvedComponents);

        doThrow(new JSONException("JSON could not be parsed")).when(deserializer).deserialize();

        // When
        Module module = step.run(inputModule);

        // Then
        String expectedMessage = "{\n" +
                "  \"moduleName\": \"TestModule\",\n" +
                "  \"errorMessage\": \"JSON could not be parsed\",\n" +
                "  \"moduleId\": 232,\n" +
                "  \"errorType\": \"org.json.JSONException\"\n" +
                "}";
        assertModuleErrorStateWith(module, expectedMessage);
    }

    @Test
    void shouldTransitionToErrorStateAndListAllExceptionFromFlowConstruction() {
        // Given
        Module inputModule = newResolvedModule();

        Set<JSONObject> flows = new HashSet<>();
        flows.add(FLOW_WITH_NOT_WELL_FORMED_FORK.parse());
        flows.add(FLOW_WITH_NOT_WELL_FORMED_ROUTER.parse());

        DeSerializedModule deSerializedModule = new DeSerializedModule(flows, emptySet(), emptySet(), emptySet(), emptySet());
        doReturn(deSerializedModule).when(deserializer).deserialize();

        mockComponentWithServiceReference(AnotherInboundTestComponent.class);
        mockComponentWithServiceReference(TestInboundComponent.class);
        mockComponent(Router.class, RouterWrapper.class);
        mockComponent(Fork.class, ForkWrapper.class);
        mockComponent(Stop.class);

        // When
        Module module = step.run(inputModule);

        // Then
        assertModuleErrorStateWith(module, "{\n" +
                "  \"errorMessage\": \"JSONObject[\\\"fork\\\"] not found.\",\n" +
                "  \"moduleId\": 232,\n" +
                "  \"flowId\": \"45a5ce60-5c9d-4075-82a7-a1cb3662bbb2a\",\n" +
                "  \"errorType\": \"org.json.JSONException\"\n" +
                "}");

        assertModuleErrorStateWith(module, "{\n" +
                "  \"errorMessage\": \"JSONObject[\\\"when\\\"] not found.\",\n" +
                "  \"moduleId\": 232,\n" +
                "  \"flowId\": \"45a5ce60-5c9d-4075-82a7-d3fa9212f52a\",\n" +
                "  \"errorType\": \"org.json.JSONException\"\n" +
                "}");
    }

    @Test
    void shouldTransitionToErrorStateWhenComponentOnInitializeEventThrowsException() {
        // Given
        Module inputModule = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        inputModule.unresolve(unresolvedComponents, resolvedComponents);
        inputModule.resolve(resolvedComponents);

        Set<JSONObject> flows = new HashSet<>();
        flows.add(FLOW_WITH_COMPONENTS.parse());

        DeSerializedModule deSerializedModule = new DeSerializedModule(flows, emptySet(), emptySet(), emptySet(), emptySet());
        doReturn(deSerializedModule).when(deserializer).deserialize();


        mockComponentWithServiceReference(TestInboundComponent.class);

        // The component throws an exception when initialized. We want to make sure
        // that the flow transitions to error state when this happens.
        TestComponent spyTestComponent = spy(new TestComponent());
        doThrow(new NullPointerException("x.y.z was null"))
                .when(spyTestComponent)
                .initialize();
        mockComponentWithServiceReference(spyTestComponent, TestComponent.class);

        mockComponent(Stop.class);

        // When
        Module module = step.run(inputModule);

        // Then
        assertModuleErrorStateWith(module, "{\n" +
                "  \"errorMessage\": \"x.y.z was null\",\n" +
                "  \"flowTitle\": \"Flow with components title\",\n" +
                "  \"moduleId\": 232,\n" +
                "  \"flowId\": \"45a5ce60-5c9d-4075-82ab-d3fa9284f52a\",\n" +
                "  \"errorType\": \"java.lang.NullPointerException\"\n" +
                "}");
    }

    @Test
    void shouldTransitionToErrorStateWhenComponentOnInitializeEventThrowsThrowable() {
        // Given
        Module inputModule = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        inputModule.unresolve(unresolvedComponents, resolvedComponents);
        inputModule.resolve(resolvedComponents);

        Set<JSONObject> flows = new HashSet<>();
        flows.add(FLOW_WITH_COMPONENTS.parse());

        DeSerializedModule deSerializedModule = new DeSerializedModule(flows, emptySet(), emptySet(), emptySet(), emptySet());
        doReturn(deSerializedModule).when(deserializer).deserialize();


        mockComponentWithServiceReference(TestInboundComponent.class);

        // The component throws a Throwable exception when initialized.
        // We want to make sure that the flow transitions to error state when this happens.
        TestComponent spyTestComponent = spy(new TestComponent());
        doThrow(new NoClassDefFoundError("javax.xml.Transformer"))
                .when(spyTestComponent)
                .initialize();
        mockComponentWithServiceReference(spyTestComponent, TestComponent.class);

        mockComponent(Stop.class);

        // When
        Module module = step.run(inputModule);

        // Then
        assertModuleErrorStateWith(module, "{\n" +
                "  \"errorMessage\": \"javax.xml.Transformer\",\n" +
                "  \"flowTitle\": \"Flow with components title\",\n" +
                "  \"moduleId\": 232,\n" +
                "  \"flowId\": \"45a5ce60-5c9d-4075-82ab-d3fa9284f52a\",\n" +
                "  \"errorType\": \"java.lang.NoClassDefFoundError\"\n" +
                "}");
    }

    private <T extends Component> void mockComponentWithServiceReference(Class<T> clazz) {
        try {
            T component = clazz.getConstructor().newInstance();
            mockComponentWithServiceReference(component, clazz);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            fail("mock component with service reference", e);
        }
    }

    private <T extends Component> void mockComponentWithServiceReference(T component, Class<T> componentClazz) {
        ReferencePair<Component> referencePair = new ReferencePair<>(component, serviceReference);
        ExecutionNode componentExecutionNode = new ExecutionNode(referencePair);
        mockInstantiateComponent(componentExecutionNode, componentClazz);
    }

    private <T extends Component> void mockComponent(Class<T> clazz, Class<? extends T> realInstance) {
        try {
            T component = realInstance.getConstructor().newInstance();
            ReferencePair<Component> referencePair = new ReferencePair<>(component);
            ExecutionNode componentExecutionNode = new ExecutionNode(referencePair);
            mockInstantiateComponent(componentExecutionNode, clazz);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            fail("mock component", e);
        }
    }

    private <T extends Component> void mockComponent(Class<T> clazz) {
        try {
            T component = clazz.getConstructor().newInstance();
            ReferencePair<Component> referencePair = new ReferencePair<>(component);
            ExecutionNode componentExecutionNode = new ExecutionNode(referencePair);
            mockInstantiateComponent(componentExecutionNode, clazz);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            fail("mock component", e);
        }
    }

    private void mockInstantiateComponent(ExecutionNode componentExecutionNode, Class<?> clazz) {
        doReturn(componentExecutionNode)
                .when(modulesManager)
                .instantiateComponent(bundleContext, clazz.getName());

        if (componentExecutionNode.getComponentReference().getServiceReference() != null) {
            doReturn(serviceObjects)
                    .when(bundleContext)
                    .getServiceObjects(serviceReference);
        }
    }

    private Module newResolvedModule() {
        Module inputModule = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        inputModule.unresolve(unresolvedComponents, resolvedComponents);
        inputModule.resolve(resolvedComponents);
        return inputModule;
    }
}
