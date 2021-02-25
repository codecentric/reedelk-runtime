package de.codecentric.reedelk.module.descriptor.analyzer.property;

import de.codecentric.reedelk.module.descriptor.analyzer.ScannerTestUtils;
import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.model.property.PrimitiveDescriptor;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyDescriptor;
import de.codecentric.reedelk.module.descriptor.model.property.WhenDescriptor;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.FieldInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WhenAnalyzerTest {

    @Mock
    private ComponentAnalyzerContext context;

    private WhenAnalyzer analyzer = new WhenAnalyzer();

    private static ClassInfo componentClassInfo;

    @BeforeAll
    static void beforeAll() {
        ScannerTestUtils.ScanContext scanContext = ScannerTestUtils.scan(TestComponentWithWhen.class);
        componentClassInfo = scanContext.targetComponentClassInfo;
    }

    @Test
    void shouldCorrectlyCreateWhenDefinition() {
        // Given
        String propertyName = "property2";
        FieldInfo property = componentClassInfo.getFieldInfo(propertyName);

        PropertyDescriptor.Builder builder =
                PropertyDescriptor.builder()
                        .name(propertyName)
                        .type(new PrimitiveDescriptor());

        // When
        analyzer.handle(property, builder, context);

        // Then
        PropertyDescriptor descriptor = builder.build();
        List<WhenDescriptor> whenDescriptors = descriptor.getWhens();
        assertThat(whenDescriptors).hasSize(1);

        assertThatExistsMatching(whenDescriptors, "property1", "ITEM1");
    }

    @Test
    void shouldCorrectlyCreateWhenDefinitions() {
        // Given
        String propertyName = "property3";
        FieldInfo property = componentClassInfo.getFieldInfo(propertyName);

        PropertyDescriptor.Builder builder =
                PropertyDescriptor.builder()
                        .name(propertyName)
                        .type(new PrimitiveDescriptor());

        // When
        analyzer.handle(property, builder, context);

        // Then
        PropertyDescriptor descriptor = builder.build();
        List<WhenDescriptor> whenDescriptors = descriptor.getWhens();
        assertThat(whenDescriptors).hasSize(2);

        assertThatExistsMatching(whenDescriptors, "property1", "ITEM2");
        assertThatExistsMatching(whenDescriptors, "property2", "ITEM4");
    }

    @Test
    void shouldReturnEmptyWhenDefinitionsWhenAnnotationNotPresentOnProperty() {
        // Given
        String propertyName = "property1";
        FieldInfo property = componentClassInfo.getFieldInfo(propertyName);

        PropertyDescriptor.Builder builder =
                PropertyDescriptor.builder()
                        .name(propertyName)
                        .type(new PrimitiveDescriptor());

        // When
        analyzer.handle(property, builder, context);

        // Then
        PropertyDescriptor descriptor = builder.build();
        List<WhenDescriptor> whenDescriptors = descriptor.getWhens();
        assertThat(whenDescriptors).isEmpty();
    }

    private void assertThatExistsMatching(List<WhenDescriptor> whenDescriptors, String wantedPropertyName, String wantedPropertyValue) {
        boolean matches = whenDescriptors.stream()
                .anyMatch(whenDescriptor ->
                        wantedPropertyName.equals(whenDescriptor.getPropertyName()) &&
                                wantedPropertyValue.equals(whenDescriptor.getPropertyValue()));
        assertThat(matches).isTrue();
    }
}
