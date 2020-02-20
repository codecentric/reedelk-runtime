package com.reedelk.module.descriptor.json;

import com.reedelk.module.descriptor.ModuleDescriptor;
import com.reedelk.module.descriptor.ModuleDescriptorException;
import com.reedelk.module.descriptor.analyzer.ModuleAnalyzer;
import com.reedelk.module.descriptor.model.AutoCompleteContributorDescriptor;
import com.reedelk.module.descriptor.model.ComponentDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ModuleDescriptorAnalyzerTest {

    private ModuleAnalyzer scanner;

    @BeforeEach
    void setUp() {
        scanner = new ModuleAnalyzer();
    }

    @Test
    void shouldCorrectlyDeserializeFromJarFile() throws ModuleDescriptorException {
        // Given
        URL targetJarURL = ModuleDescriptorAnalyzerTest.class.getResource("/sample-module-xyz.jar");

        // When
        ModuleDescriptor descriptor = scanner.from(targetJarURL.getPath());

        // Then
        List<ComponentDescriptor> componentDescriptors =
                descriptor.getComponents();
        assertThat(componentDescriptors).hasSize(1);

        List<AutoCompleteContributorDescriptor> autocompleteContributorDescriptors =
                descriptor.getAutocompleteContributors();
        assertThat(autocompleteContributorDescriptors).hasSize(1);
    }
}
