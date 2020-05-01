package com.reedelk.module.descriptor.analyzer.type;

import com.reedelk.module.descriptor.model.type.TypeDescriptor;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.reedelk.module.descriptor.analyzer.AnalyzerTestUtils.scanResultOf;
import static org.assertj.core.api.Assertions.assertThat;

class TypeAnalyzerTest {

    @Test
    void shouldReturnCorrectTypeDescriptors() {
        // Given
        ScanResult scanResult = scanResultOf(TypeAnalyzerComponent.class);
        TypeAnalyzer analyzer = new TypeAnalyzer(scanResult);
        // When
        List<TypeDescriptor> types = analyzer.analyze();

        // Then
        assertThat(types.size()).isEqualTo(1);
        TypeDescriptor descriptor = types.get(0);
        assertThat(descriptor.getDescription()).isEqualTo("TypeAnalyzerComponent description");
        assertThat(descriptor.getDisplayName()).isEqualTo("MyTypeAnalyzerComponent");
        assertThat(descriptor.getListItemType()).isEqualTo(Map.class.getName());
        assertThat(descriptor.getType()).isEqualTo(TypeAnalyzerComponent.class.getName());
        assertThat(descriptor.getFunctions()).hasSize(1);
        assertThat(descriptor.getProperties()).hasSize(1);
    }
}
