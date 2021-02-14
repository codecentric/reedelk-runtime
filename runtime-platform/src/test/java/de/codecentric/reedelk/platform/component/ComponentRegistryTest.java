package de.codecentric.reedelk.platform.component;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class ComponentRegistryTest {

    private final String PREDEFINED_1 = "com.reedelk.platform.predefined.component1";
    private final String PREDEFINED_2 = "com.reedelk.platform.predefined.component2";
    private final String PREDEFINED_3 = "com.reedelk.platform.predefined.component3";

    @Test
    void shouldRegistryRegisterPredefinedComponentsCorrectly() {
        // Given
        Collection<String> predefinedComponents = asList(
                PREDEFINED_1, PREDEFINED_2, PREDEFINED_3);

        // When
        ComponentRegistry registry = new ComponentRegistry(predefinedComponents);

        // Then
        assertThat(registry.registeredComponents)
                .containsExactlyInAnyOrder(PREDEFINED_1, PREDEFINED_2, PREDEFINED_3);
    }

    @Test
    void shouldRegistryRegisterComponentCorrectly() {
        // Given
        ComponentRegistry registry = new ComponentRegistry(Collections.emptyList());

        // When
        registry.registerComponent(PREDEFINED_2);

        // Then
        assertThat(registry.registeredComponents).contains(PREDEFINED_2);
    }

    @Test
    void shouldRegistryUnregisterComponentCorrectly() {
        // Given
        ComponentRegistry registry = new ComponentRegistry(Collections.emptyList());
        registry.registerComponent(PREDEFINED_3);

        assumeTrue(registry.registeredComponents.contains(PREDEFINED_3));

        // When
        registry.unregisterComponent(PREDEFINED_3);

        // Then
        assertThat(registry.registeredComponents).doesNotContain(PREDEFINED_3);
    }

    @Test
    void shouldUnregisteredComponentsOfReturnCorrectly() {
        // Given
        ComponentRegistry registry = new ComponentRegistry(asList(
                PREDEFINED_1, PREDEFINED_2));

        Collection<String> resolvedComponents = asList(
                PREDEFINED_1, PREDEFINED_2, PREDEFINED_3);

        // When
        Collection<String> actualUnregisteredComponents = registry.unregisteredComponentsOf(resolvedComponents);

        // Then
        assertThat(actualUnregisteredComponents).containsExactly(PREDEFINED_3);

    }

}
