package com.reedelk.module.descriptor.analyzer.component;

import com.reedelk.module.descriptor.analyzer.AnalyzerTestUtils;
import com.reedelk.module.descriptor.model.component.ComponentInputDescriptor;
import io.github.classgraph.ClassInfo;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class ComponentInputAnalyzerTest {

    @Test
    void shouldCorrectlyAnalyzeComponentInput() {
        // Given
        ClassInfo classInfo = AnalyzerTestUtils.classInfoOf(ComponentInputAnalyzerComponent.class);
        ComponentInputAnalyzer analyzer = new ComponentInputAnalyzer(classInfo);

        // When
        ComponentInputDescriptor actual = analyzer.analyze();

        // Then
        assertThat(actual).isNotNull();
        assertThat(actual.getDescription()).isEqualTo("My input description");
        assertThat(actual.getPayload()).isEqualTo(asList("java.lang.String", "byte[]"));
    }
}
