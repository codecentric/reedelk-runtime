package com.reedelk.module.descriptor.json;

import com.reedelk.module.descriptor.ModuleDescriptorException;
import com.reedelk.module.descriptor.analyzer.ModuleDescriptorAnalyzer;
import com.reedelk.module.descriptor.model.ModuleDescriptor;
import com.reedelk.module.descriptor.model.component.ComponentDescriptor;
import com.reedelk.module.descriptor.model.component.ComponentType;
import com.reedelk.module.descriptor.model.property.*;
import com.reedelk.runtime.api.flow.FlowContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

class ModuleDescriptorAnalyzerTest {

    private ModuleDescriptorAnalyzer scanner;
    private ModuleDescriptor expected;

    @BeforeEach
    void setUp() {
        scanner = new ModuleDescriptorAnalyzer();
        expected = createTestModuleDescriptor();
    }

    @Test
    void shouldCorrectlyDeserializeFromJarFile() throws ModuleDescriptorException {
        // Given
        URL targetJarURL = ModuleDescriptorAnalyzerTest.class.getResource("/sample-module-xyz.jar");

        // When
        Optional<ModuleDescriptor> actual = scanner.from(targetJarURL.getPath());

        // Then
        assertThat(actual).isPresent();
        assertThat(expected.toString()).isEqualTo(actual.get().toString());
    }

    private static ModuleDescriptor createTestModuleDescriptor() {
        PropertyTypeDescriptor doublePrimitive = new PrimitiveDescriptor();
        doublePrimitive.setType(Double.class);

        ScriptSignatureDescriptor signatureDescriptor = new ScriptSignatureDescriptor();
        signatureDescriptor.setArguments(asList(
                new ScriptSignatureArgument("arg0", FlowContext.class.getName()),
                new ScriptSignatureArgument("arg1", Exception.class.getName())));

        WhenDescriptor whenDescriptor = new WhenDescriptor();
        whenDescriptor.setPropertyName("myProperty");
        whenDescriptor.setPropertyValue("VALUE1");
        List<WhenDescriptor> whenDescriptors = singletonList(whenDescriptor);

        PropertyDescriptor propertyDescriptor1 = new PropertyDescriptor();
        propertyDescriptor1.setScriptSignature(signatureDescriptor);
        propertyDescriptor1.setWhens(whenDescriptors);
        propertyDescriptor1.setType(doublePrimitive);
        propertyDescriptor1.setDescription("Property Double Object Info");
        propertyDescriptor1.setDisplayName("Property Double Object");
        propertyDescriptor1.setName("propertyDoubleObject");
        propertyDescriptor1.setDefaultValue("10.110");
        propertyDescriptor1.setHintValue("1111.2222");
        propertyDescriptor1.setInitValue("234.553");
        propertyDescriptor1.setExample("77.12");

        ComponentDescriptor component1 = new ComponentDescriptor();
        component1.setDisplayName("Test Processor Component");
        component1.setType(ComponentType.PROCESSOR);
        component1.setFullyQualifiedName("com.test.component.TestProcessorComponent");
        component1.setProperties(singletonList(propertyDescriptor1));

        ModuleDescriptor expected = new ModuleDescriptor();
        expected.setComponents(singletonList(component1));
        return expected;
    }
}
