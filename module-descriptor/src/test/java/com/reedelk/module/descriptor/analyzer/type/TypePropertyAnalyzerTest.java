package com.reedelk.module.descriptor.analyzer.type;

import com.reedelk.module.descriptor.analyzer.AnalyzerTestUtils;
import com.reedelk.module.descriptor.model.type.TypePropertyDescriptor;
import io.github.classgraph.ClassInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TypePropertyAnalyzerTest {

    @Test
    void shouldReturnCorrectPropertyDescriptors() {
        // Given
        ClassInfo classInfo = AnalyzerTestUtils.classInfoOf(TypePropertyComponent.class);
        TypePropertyAnalyzer analyzer = new TypePropertyAnalyzer(classInfo);

        // When
        List<TypePropertyDescriptor> properties = analyzer.analyze();

        // Then
        assertThat(properties).hasSize(4);
    }
}
