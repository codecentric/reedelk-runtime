package de.codecentric.reedelk.module.descriptor.analyzer.component;

import de.codecentric.reedelk.module.descriptor.ModuleDescriptorException;
import de.codecentric.reedelk.module.descriptor.analyzer.AnalyzerTestUtils;
import de.codecentric.reedelk.module.descriptor.fixture.MyAttributes;
import de.codecentric.reedelk.module.descriptor.model.component.ComponentOutputDescriptor;
import de.codecentric.reedelk.runtime.api.commons.StringUtils;
import de.codecentric.reedelk.runtime.api.message.MessageAttributes;
import io.github.classgraph.ClassInfo;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ComponentOutputAnalyzerTest {

    @Test
    void shouldCorrectlyAnalyzeComponentOutput() {
        // Given
        ClassInfo classInfo = AnalyzerTestUtils.classInfoOf(ComponentOutputAnalyzerComponent.class);
        ComponentOutputAnalyzer analyzer = new ComponentOutputAnalyzer(classInfo);

        // When
        ComponentOutputDescriptor actual = analyzer.analyze();

        // Then
        assertThat(actual).isNotNull();
        assertThat(actual.getDescription()).isEqualTo("My output description");
        assertThat(actual.getPayload()).isEqualTo(asList("long", "java.lang.Byte[]"));
        assertThat(actual.getAttributes()).isEqualTo(singletonList(MyAttributes.class.getName()));
    }

    @Test
    void shouldCorrectlyAnalyzeComponentOutputWithDefaults() {
        // Given
        ClassInfo classInfo = AnalyzerTestUtils.classInfoOf(ComponentOutputAnalyzerComponentDefault.class);
        ComponentOutputAnalyzer analyzer = new ComponentOutputAnalyzer(classInfo);

        // When
        ComponentOutputDescriptor actual = analyzer.analyze();

        // Then
        assertThat(actual).isNotNull();
        assertThat(actual.getDescription()).isEqualTo(StringUtils.EMPTY);
        assertThat(actual.getPayload()).isEqualTo(singletonList(Object.class.getName()));
        assertThat(actual.getAttributes()).isEqualTo(singletonList(MessageAttributes.class.getName()));
    }

    @Test
    void shouldThrowExceptionWhenPayloadHasEmptyTypes() {
        // Given
        ClassInfo classInfo = AnalyzerTestUtils.classInfoOf(ComponentOutputAnalyzerComponentPayloadEmpty.class);
        ComponentOutputAnalyzer analyzer = new ComponentOutputAnalyzer(classInfo);

        // When
        ModuleDescriptorException thrown = assertThrows(ModuleDescriptorException.class, analyzer::analyze);

        // Then
        assertThat(thrown).isNotNull();
        assertThat(thrown).hasMessage("Component Output payload types must not be empty " +
                "(class: de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentOutputAnalyzerComponentPayloadEmpty).");
    }
}
