package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.Matchers;
import com.reedelk.module.descriptor.analyzer.Matcher;
import com.reedelk.module.descriptor.analyzer.ScannerTestUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.AutocompleteVariableDescriptor;
import com.reedelk.module.descriptor.model.PropertyDescriptor;
import com.reedelk.module.descriptor.model.TypeScriptDescriptor;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
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
class AutocompleteVariableAnalyzerTest {

    @Mock
    private ComponentAnalyzerContext context;

    private AutocompleteVariableAnalyzer analyzer = new AutocompleteVariableAnalyzer();

    private static ClassInfo componentClassInfo;

    @BeforeAll
    static void beforeAll() {
        ScannerTestUtils.ScanContext scanContext = ScannerTestUtils.scan(TestComponentWithScriptSignature.class);
        componentClassInfo = scanContext.targetComponentClassInfo;
    }

    @Test
    void shouldCorrectlyCreateAutocompleteVariables() {
        // Given
        String propertyName = "scriptPropertyWithScriptSignature";
        FieldInfo property = componentClassInfo.getFieldInfo(propertyName);

        PropertyDescriptor.Builder builder =
                PropertyDescriptor.builder()
                        .name(propertyName)
                        .type(new TypeScriptDescriptor());

        // When
        analyzer.handle(property, builder, context);

        // Then
        PropertyDescriptor descriptor = builder.build();

        List<AutocompleteVariableDescriptor> autocompleteVariables = descriptor.getAutocompleteVariables();
        assertThat(autocompleteVariables).hasSize(3);

        AutocompleteVariableDescriptor arg1 = new AutocompleteVariableDescriptor();
        arg1.setName("arg1");
        arg1.setType(String.class.getSimpleName());
        assertThatExists(autocompleteVariables, arg1);

        AutocompleteVariableDescriptor arg2 = new AutocompleteVariableDescriptor();
        arg2.setName("arg2");
        arg2.setType(Message.class.getSimpleName());
        assertThatExists(autocompleteVariables, arg2);

        AutocompleteVariableDescriptor arg3 = new AutocompleteVariableDescriptor();
        arg3.setName("arg3");
        arg3.setType(FlowContext.class.getSimpleName());
        assertThatExists(autocompleteVariables, arg3);
    }

    private void assertThatExists(List<AutocompleteVariableDescriptor> descriptors, AutocompleteVariableDescriptor expected) {
        Matcher<AutocompleteVariableDescriptor> matcher = Matchers.ofAutocompleteVariableDescriptor(expected);
        boolean matches = descriptors.stream().anyMatch(matcher::matches);
        assertThat(matches)
                .withFailMessage("Could not find: " + expected + " from collection: " + descriptors)
                .isTrue();
    }
}
