package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.ScannerTestUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.property.PropertyDescriptor;
import com.reedelk.module.descriptor.model.property.TypeScriptDescriptor;
import com.reedelk.module.descriptor.model.script.ScriptVariableDescriptor;
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
class ScriptVariableDescriptorAnalyzerTest {

    @Mock
    private ComponentAnalyzerContext context;

    private ScriptVariableAnalyzer analyzer = new ScriptVariableAnalyzer();

    private static ClassInfo componentClassInfo;

    @BeforeAll
    static void beforeAll() {
        ScannerTestUtils.ScanContext scanContext = ScannerTestUtils.scan(TestComponentWithScriptSignature.class);
        componentClassInfo = scanContext.targetComponentClassInfo;
    }

    @Test
    void shouldCorrectlyCreateScriptVariableDescriptors() {
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

        List<ScriptVariableDescriptor> scriptVariableDescriptors = descriptor.getScriptVariables();
        assertThat(scriptVariableDescriptors).hasSize(3);

        ScriptVariableDescriptor arg1 = new ScriptVariableDescriptor();
        arg1.setName("arg1");
        arg1.setType(String.class.getSimpleName());

        ScriptVariableDescriptor arg2 = new ScriptVariableDescriptor();
        arg2.setName("arg2");
        arg2.setType(Message.class.getSimpleName());

        ScriptVariableDescriptor arg3 = new ScriptVariableDescriptor();
        arg3.setName("arg3");
        arg3.setType(FlowContext.class.getSimpleName());
    }
}
