package de.codecentric.reedelk.runtime.api.commons;

import de.codecentric.reedelk.runtime.api.commons.ModuleContext;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import de.codecentric.reedelk.runtime.api.exception.ComponentConfigurationException;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import org.junit.jupiter.api.Test;

import static de.codecentric.reedelk.runtime.api.commons.ComponentPrecondition.Configuration.requireNotNull;
import static de.codecentric.reedelk.runtime.api.commons.ComponentPrecondition.Configuration.requireNotNullOrBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurationPreconditionsTest {

    @Test
    void shouldRequireNotNullOrBlankDynamicStringRequireNotNullOrBlankThrowException() {
        // Given
        DynamicString emptyDynamicString = DynamicString.from("#[]", new ModuleContext(10L));

        // When
        ComponentConfigurationException thrown = assertThrows(ComponentConfigurationException.class,
                () -> requireNotNullOrBlank(MyComponent.class, emptyDynamicString, "Expected not empty!"));

        // Then
        assertThat(thrown).hasMessage("MyComponent (de.codecentric.reedelk.runtime.api.commons.ConfigurationPreconditionsTest$MyComponent) has a configuration error: Expected not empty!");
    }

    @Test
    void shouldRequireNotNullOrBlankDynamicStringReturnOriginalValue() {
        // Given
        DynamicString notEmptyDynamicString = DynamicString.from("#[context.correlationId]", new ModuleContext(10L));

        // When
        DynamicString actual = requireNotNullOrBlank(MyComponent.class, notEmptyDynamicString, "Expected not empty!");

        // Then
        assertThat(actual).isEqualTo(notEmptyDynamicString);
    }

    @Test
    void shouldRequireNotNullOrBlankDynamicObjectRequireNotNullOrBlankThrowException() {
        // Given
        DynamicObject emptyDynamicObject = DynamicObject.from("#[]", new ModuleContext(10L));

        // When
        ComponentConfigurationException thrown = assertThrows(ComponentConfigurationException.class,
                () -> requireNotNullOrBlank(MyComponent.class, emptyDynamicObject, "Expected not empty!"));

        // Then
        assertThat(thrown).hasMessage("MyComponent (de.codecentric.reedelk.runtime.api.commons.ConfigurationPreconditionsTest$MyComponent) has a configuration error: Expected not empty!");
    }

    @Test
    void shouldRequireNotNullOrBlankDynamicObjectReturnOriginalValue() {
        // Given
        DynamicObject notEmptyDynamicObject = DynamicObject.from("#[['one','two','three']]", new ModuleContext(10L));

        // When
        DynamicObject actual = requireNotNullOrBlank(MyComponent.class, notEmptyDynamicObject, "Expected not empty!");

        // Then
        assertThat(actual).isEqualTo(notEmptyDynamicObject);
    }

    @Test
    void shouldRequireNotNullDynamicObjectReturnOriginalValue() {
        // Given
        DynamicObject notEmptyDynamicObject = DynamicObject.from("#[['one','two','three']]", new ModuleContext(10L));

        // When
        DynamicObject actual = requireNotNull(MyComponent.class, notEmptyDynamicObject, "Expected not empty!");

        // Then
        assertThat(actual).isEqualTo(notEmptyDynamicObject);
    }

    @Test
    void shouldRequireNotNullDynamicObjectThrowException() {
        // Given
        DynamicObject nullDynamicObject = null;

        // When
        ComponentConfigurationException thrown = assertThrows(ComponentConfigurationException.class,
                () -> requireNotNull(MyComponent.class, nullDynamicObject, "Expected not null!"));

        // Then
        assertThat(thrown).hasMessage("MyComponent (de.codecentric.reedelk.runtime.api.commons.ConfigurationPreconditionsTest$MyComponent) has a configuration error: Expected not null!");
    }

    static class MyComponent implements Implementor {
    }
}
