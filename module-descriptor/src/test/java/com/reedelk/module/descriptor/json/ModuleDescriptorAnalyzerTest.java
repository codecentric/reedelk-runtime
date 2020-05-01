package com.reedelk.module.descriptor.json;

import com.reedelk.module.descriptor.analyzer.ModuleDescriptorAnalyzer;
import org.junit.jupiter.api.BeforeEach;

class ModuleDescriptorAnalyzerTest {

    private ModuleDescriptorAnalyzer scanner;

    @BeforeEach
    void setUp() {
        scanner = new ModuleDescriptorAnalyzer();
    }

    // TODO: Fixme
    /**
    @Test
    void shouldCorrectlyDeserializeFromJarFile() throws ModuleDescriptorException {
        // Given
        URL targetJarURL = ModuleDescriptorAnalyzerTest.class.getResource("/sample-module-xyz.jar");

        // When
        ModuleDescriptor descriptor = scanner.from(targetJarURL.getPath(), "sample-module-xyz");

        // Then
        List<ComponentDescriptor> componentDescriptors = descriptor.getComponents();
        assertThat(componentDescriptors).hasSize(1);
        ComponentDescriptor componentDescriptor = componentDescriptors.get(0);
        assertThat(componentDescriptor.getDisplayName()).isEqualTo("Test Processor Component");
        assertThat(componentDescriptor.getType()).isEqualTo(ComponentType.PROCESSOR);
        assertThat(componentDescriptor.getFullyQualifiedName()).isEqualTo("com.test.component.TestProcessorComponent");
        assertThat(componentDescriptor.isHidden()).isEqualTo(false);

        List<PropertyDescriptor> properties = componentDescriptor.getProperties();
        assertThat(properties).hasSize(1);

        PropertyDescriptor propertyDescriptor = properties.get(0);
        assertThat(propertyDescriptor.getName()).isEqualTo("propertyDoubleObject");
        assertThat(propertyDescriptor.getDisplayName()).isEqualTo("Property Double Object");
        assertThat(propertyDescriptor.getDescription()).isEqualTo("Property Double Object Info");
        assertThat(propertyDescriptor.getInitValue()).isEqualTo("234.553");
        assertThat(propertyDescriptor.getDefaultValue()).isEqualTo("10.110");
        assertThat(propertyDescriptor.getHintValue()).isEqualTo("1111.2222");
        assertThat(propertyDescriptor.getExample()).isEqualTo("77.12");

        TypePrimitiveDescriptor primitiveDescriptor = propertyDescriptor.getType();
        assertThat(primitiveDescriptor.getType()).isEqualTo(Double.class);

        ScriptSignatureDescriptor scriptSignature = propertyDescriptor.getScriptSignature();

        List<ScriptSignatureArgument> arguments = scriptSignature.getArguments();
        ScriptSignatureArgument stringStringPair = arguments.get(0);
        // TODO: Assert that script signature is correct!

        List<WhenDescriptor> whens = propertyDescriptor.getWhens();
        assertThat(whens).hasSize(1);

        WhenDescriptor when = whens.get(0);
        assertThat(when.getPropertyName()).isEqualTo("myProperty");
        assertThat(when.getPropertyValue()).isEqualTo("VALUE1");
    }*/
}
