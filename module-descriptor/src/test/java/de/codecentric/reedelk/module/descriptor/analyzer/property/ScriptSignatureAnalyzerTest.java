package de.codecentric.reedelk.module.descriptor.analyzer.property;

import de.codecentric.reedelk.module.descriptor.analyzer.ScannerTestUtils;
import de.codecentric.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyDescriptor;
import de.codecentric.reedelk.module.descriptor.model.property.ScriptDescriptor;
import de.codecentric.reedelk.module.descriptor.model.property.ScriptSignatureArgument;
import de.codecentric.reedelk.module.descriptor.model.property.ScriptSignatureDescriptor;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.FieldInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ScriptSignatureAnalyzerTest {

    @Mock
    private ComponentAnalyzerContext context;

    private ScriptSignatureAnalyzer analyzer = new ScriptSignatureAnalyzer();

    private static ClassInfo componentClassInfo;

    @BeforeAll
    static void beforeAll() {
        ScannerTestUtils.ScanContext scanContext = ScannerTestUtils.scan(ScriptSignatureComponent.class);
        componentClassInfo = scanContext.targetComponentClassInfo;
    }

    @Test
    void shouldCorrectlyCreateScriptSignatureDefinition() {
        // Given
        String propertyName = "propertyWithSignature";
        FieldInfo property = componentClassInfo.getFieldInfo(propertyName);

        PropertyDescriptor.Builder builder =
                PropertyDescriptor.builder()
                        .name(propertyName)
                        .type(new ScriptDescriptor());

        // When
        analyzer.handle(property, builder, context);

        // Then
        List<ScriptSignatureArgument> arguments =
                asList(new ScriptSignatureArgument("arg1", String.class.getName()),
                        new ScriptSignatureArgument("arg2", Message.class.getName()),
                        new ScriptSignatureArgument("arg3", FlowContext.class.getName()));

        ScriptSignatureDescriptor descriptor = new ScriptSignatureDescriptor(arguments);

        PropertyDescriptor propertyDescriptor = builder.build();

        String expected = descriptor.toString();
        String actual = propertyDescriptor.getScriptSignature().toString();
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void shouldReturnEmptyScriptSignatureDefinition() {
        // Given
        String propertyName = "propertyWithoutSignature";
        FieldInfo property = componentClassInfo.getFieldInfo(propertyName);

        PropertyDescriptor.Builder builder =
                PropertyDescriptor.builder()
                        .name(propertyName)
                        .type(new ScriptDescriptor());

        // When
        analyzer.handle(property, builder, context);

        // Then
        PropertyDescriptor descriptor = builder.build();
        assertThat(descriptor.getScriptSignature()).isNull();
    }
}
