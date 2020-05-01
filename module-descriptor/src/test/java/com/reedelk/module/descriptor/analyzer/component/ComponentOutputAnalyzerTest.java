package com.reedelk.module.descriptor.analyzer.component;

import com.reedelk.module.descriptor.fixture.MyAttributes;
import com.reedelk.module.descriptor.model.component.ComponentOutputDescriptor;
import io.github.classgraph.ClassInfo;
import org.junit.jupiter.api.Test;

import static com.reedelk.module.descriptor.analyzer.AnalyzerTestUtils.classInfoOf;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

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
}
