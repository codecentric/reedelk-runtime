package de.codecentric.reedelk.platform.module;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ModulesManagerTest {

    private final String component1 = "com.reedelk.platform.testing.AwesomeComponent1";
    private final String component2 = "com.reedelk.platform.testing.AwesomeComponent2";
    private final String component3 = "com.reedelk.platform.testing.AwesomeComponent3";
    private final String component4 = "com.reedelk.platform.testing.AwesomeComponent4";

    private final long moduleId = 232L;
    private final String testModuleName = "TestModule";
    private final String testVersion = "0.9.0";
    private final String testLocation = "file://location/test";

    @Mock
    private ModuleDeserializer deserializer;

    private ModulesManager manager;

    @BeforeEach
    void setUp() {
        manager = new ModulesManager();
    }

    @Test
    void shouldAddModuleCorrectly() {
        // Given
        Module module = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();

        // When
        manager.add(module);

        // Then
        Module returned = manager.getModuleById(moduleId);
        assertThat(returned).isEqualTo(module);
        assertThat(returned.id()).isEqualTo(moduleId);
    }

    @Test
    void shouldRemoveModuleCorrectly() {
        // Given
        Module module = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        manager.add(module);

        // When
        manager.removeModuleById(moduleId);

        // Then
        assertThat(manager.getModuleById(moduleId)).isNull();
    }

    @Test
    void shouldIsModuleStartedReturnFalseWhenNotRegistered() {
        // When
        boolean actualValue = manager.isModuleStarted(Long.MAX_VALUE);

        // Then
        assertThat(actualValue).isFalse();
    }

    @Test
    void shouldIsModuleStartedReturnFalseWhenNotStarted() {
        // Given
        Module module = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        manager.add(module);

        // When
        boolean actualValue = manager.isModuleStarted(moduleId);

        // Then
        assertThat(actualValue).isFalse();
    }

    @Test
    void shouldIsModuleStartedReturnTrueWhenStarted() {
        // Given
        Module module = Module.builder()
                .moduleId(moduleId)
                .name(testModuleName)
                .version(testVersion)
                .deserializer(deserializer)
                .moduleFilePath(testLocation)
                .build();
        manager.add(module);

        module.unresolve(emptyList(), emptyList());
        module.resolve(emptyList());
        module.stop(emptySet());
        module.start(emptySet());

        // When
        boolean actualValue = manager.isModuleStarted(moduleId);

        // Then
        assertThat(actualValue).isTrue();
    }

    @Test
    void shouldAllModulesReturnAllRegisteredModules() {
        // Given
        Module module1 = Module.builder().moduleId(1L).name("TestModule1").moduleFilePath(testLocation).deserializer(deserializer).version(testVersion).build();
        Module module2 = Module.builder().moduleId(2L).name("TestModule2").moduleFilePath(testLocation).deserializer(deserializer).version(testVersion).build();
        Module module3 = Module.builder().moduleId(3L).name("TestModule3").moduleFilePath(testLocation).deserializer(deserializer).version(testVersion).build();

        manager.add(module1);
        manager.add(module2);
        manager.add(module3);

        // When
        Collection<Module> allModules = manager.allModules();

        // Then
        assertThat(allModules).hasSize(3);
        assertThat(allModules).containsExactlyInAnyOrder(module1, module2, module3);
    }

    @Test
    void shouldFindUnresolvedModulesReturnModulesWithStateUnresolved() {
        // Given
        Collection<String> unresolvedComponents = asList(component1, component3);
        Collection<String> resolvedComponents = asList(component2, component4);

        Module module1 = Module.builder().moduleId(1L).name("TestModule1").moduleFilePath(testLocation).deserializer(deserializer).version(testVersion).build();
        module1.unresolve(unresolvedComponents, resolvedComponents);

        Module module2 = Module.builder().moduleId(2L).name("TestModule2").moduleFilePath(testLocation).deserializer(deserializer).version(testVersion).build();

        Module module3 = Module.builder().moduleId(3L).name("TestModule3").moduleFilePath(testLocation).deserializer(deserializer).version(testVersion).build();
        module3.unresolve(unresolvedComponents, resolvedComponents);

        manager.add(module1);
        manager.add(module2);
        manager.add(module3);

        // When
        Collection<Module> unresolvedModules = manager.findUnresolvedModules();

        // Then
        assertThat(unresolvedModules).hasSize(2);
        assertThat(unresolvedModules).containsExactly(module1, module3);
    }

    @Test
    void shouldFindModulesUsingComponentReturnCorrectModulesWhenUnresolved() {
        // Given
        Collection<String> unresolvedComponents = asList(component1, component3);
        Collection<String> resolvedComponents = asList(component2, component4);

        Module module1 = Module.builder().moduleId(1L).name("TestModule1").moduleFilePath(testLocation).deserializer(deserializer).version(testVersion).build();
        module1.unresolve(unresolvedComponents, resolvedComponents);

        Module module2 = Module.builder().moduleId(2L).name("TestModule2").moduleFilePath(testLocation).deserializer(deserializer).version(testVersion).build();
        module2.error(emptyList());

        Module module3 = Module.builder().moduleId(3L).name("TestModule3").moduleFilePath(testLocation).deserializer(deserializer).version(testVersion).build();
        module3.unresolve(unresolvedComponents, resolvedComponents);


        manager.add(module1);
        manager.add(module2);
        manager.add(module3);

        // When
        Collection<Module> modulesUsingComponent = manager.findModulesUsingComponent(component2);

        // Then
        assertThat(modulesUsingComponent).hasSize(2);
        assertThat(modulesUsingComponent).containsExactlyInAnyOrder(module1, module3);
    }

}
