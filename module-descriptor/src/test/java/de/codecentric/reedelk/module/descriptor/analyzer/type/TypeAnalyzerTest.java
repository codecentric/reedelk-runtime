package de.codecentric.reedelk.module.descriptor.analyzer.type;

import de.codecentric.reedelk.module.descriptor.model.type.TypeDescriptor;
import de.codecentric.reedelk.runtime.api.message.MessageAttributes;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static de.codecentric.reedelk.module.descriptor.analyzer.AnalyzerTestUtils.scanResultOf;
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
        assertThat(types.size()).isEqualTo(2); // TypeAnalyzerComponent and superclass MessageAttributes.

        TypeDescriptor descriptor = types.stream().filter(typeDescriptor -> typeDescriptor.getType()
                .equals(TypeAnalyzerComponent.class.getName())).findFirst().get();

        assertThat(descriptor.getDescription()).isEqualTo("TypeAnalyzerComponent description");
        assertThat(descriptor.getDisplayName()).isEqualTo("MyTypeAnalyzerComponent");
        assertThat(descriptor.getExtendsType()).isEqualTo(MessageAttributes.class.getName());
        assertThat(descriptor.getType()).isEqualTo(TypeAnalyzerComponent.class.getName());
        assertThat(descriptor.getListItemType()).isEqualTo(Map.class.getName());
        assertThat(descriptor.getProperties()).hasSize(1);
        assertThat(descriptor.getFunctions()).hasSize(1);
    }
}
