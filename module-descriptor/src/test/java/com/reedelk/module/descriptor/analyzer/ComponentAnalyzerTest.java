package com.reedelk.module.descriptor.analyzer;

import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzer;
import com.reedelk.module.descriptor.analyzer.property.PropertyAnalyzer;
import com.reedelk.module.descriptor.model.component.ComponentDescriptor;
import com.reedelk.module.descriptor.model.component.ComponentType;
import com.reedelk.module.descriptor.model.property.PropertyDescriptor;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.FieldInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ComponentAnalyzerTest {

    @Mock
    private PropertyAnalyzer propertyAnalyzer;
    @Mock
    private PropertyDescriptor descriptor1;
    @Mock
    private PropertyDescriptor descriptor2;
    @Mock
    private PropertyDescriptor descriptor3;

    private ComponentAnalyzer analyzer;
    private ClassInfo componentClassInfo;

    @BeforeEach
    void setUp() {
        componentClassInfo = AnalyzerTestUtils.classInfoOf(TestComponent.class);

        doReturn(of(descriptor1), of(descriptor2), of(descriptor3), empty())
                .when(propertyAnalyzer).analyze(any(FieldInfo.class));

        analyzer = new ComponentAnalyzer(propertyAnalyzer);
    }

    @Test
    void shouldCorrectlyBuildComponentDescriptorFromClassInfo() {
        // Given
        ClassInfo testComponentClassInfo = componentClassInfo;

        // When
        ComponentDescriptor descriptor = analyzer.analyze(testComponentClassInfo);

        // Then
        assertThat(descriptor.isHidden()).isFalse();
        assertThat(descriptor.getProperties().size()).isEqualTo(3);
        assertThat(descriptor.getDisplayName()).isEqualTo("Test Component");
        assertThat(descriptor.getDescription()).isEqualTo("My test component description");
        assertThat(descriptor.getType()).isEqualTo(ComponentType.PROCESSOR);
        assertThat(descriptor.getFullyQualifiedName()).isEqualTo(TestComponent.class.getName());
    }
}
