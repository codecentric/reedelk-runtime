package com.reedelk.module.descriptor.analyzer.component;

import com.reedelk.module.descriptor.ModuleDescriptorException;
import com.reedelk.module.descriptor.fixture.MyAttributes;
import com.reedelk.module.descriptor.model.component.ComponentOutputDescriptor;
import com.reedelk.runtime.api.commons.StringUtils;
import com.reedelk.runtime.api.message.MessageAttributes;
import io.github.classgraph.ClassInfo;
import org.junit.jupiter.api.Test;

import static com.reedelk.module.descriptor.analyzer.AnalyzerTestUtils.classInfoOf;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ComponentOutputAnalyzerTest {

    @Test
    void shouldCorrectlyAnalyzeComponentOutput() {
        // Given
        ClassInfo classInfo = classInfoOf(ComponentOutputAnalyzerComponent.class);
        ComponentOutputAnalyzer analyzer = new ComponentOutputAnalyzer(classInfo);

        // When
        ComponentOutputDescriptor actual = analyzer.analyze();

        // Then
        assertThat(actual).isNotNull();
        assertThat(actual.getAttributes()).isEqualTo(MyAttributes.class.getName());
        assertThat(actual.getDescription()).isEqualTo("My output description");
        assertThat(actual.getPayload()).isEqualTo(asList("long", "java.lang.Byte[]"));
    }

    @Test
    void shouldCorrectlyAnalyzeComponentOutputWithDefaults() {
        // Given
        ClassInfo classInfo = classInfoOf(ComponentOutputAnalyzerComponentDefault.class);
        ComponentOutputAnalyzer analyzer = new ComponentOutputAnalyzer(classInfo);

        // When
        ComponentOutputDescriptor actual = analyzer.analyze();

        // Then
        assertThat(actual).isNotNull();
        assertThat(actual.getAttributes()).isEqualTo(MessageAttributes.class.getName());
        assertThat(actual.getDescription()).isEqualTo(StringUtils.EMPTY);
        assertThat(actual.getPayload()).isEqualTo(singletonList(Object.class.getName()));
    }

    @Test
    void shouldThrowExceptionWhenPayloadHasEmptyTypes() {
        // Given
        ClassInfo classInfo = classInfoOf(ComponentOutputAnalyzerComponentPayloadEmpty.class);
        ComponentOutputAnalyzer analyzer = new ComponentOutputAnalyzer(classInfo);

        // When
        ModuleDescriptorException thrown = assertThrows(ModuleDescriptorException.class, analyzer::analyze);

        // Then
        assertThat(thrown).isNotNull();
        assertThat(thrown).hasMessage("Component Output payload types must not be empty " +
                "(class: com.reedelk.module.descriptor.analyzer.component.ComponentOutputAnalyzerComponentPayloadEmpty).");
    }
}
