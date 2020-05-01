package com.reedelk.module.descriptor.analyzer.type;

import com.reedelk.module.descriptor.ModuleDescriptorException;
import com.reedelk.module.descriptor.model.type.TypeFunctionDescriptor;
import io.github.classgraph.ClassInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.reedelk.module.descriptor.analyzer.AnalyzerTestUtils.classInfoOf;
import static com.reedelk.runtime.api.commons.StringUtils.EMPTY;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TypeFunctionAnalyzerTest {

    @Test
    void shouldReturnCorrectFunctionDescriptors() {
        // Given
        ClassInfo classInfo = classInfoOf(TypeFunctionComponent.class);
        TypeFunctionAnalyzer analyzer = new TypeFunctionAnalyzer(classInfo);

        // When
        List<TypeFunctionDescriptor> functions = analyzer.analyze();

        // Then
        assertThat(functions).hasSize(9);
        assertExists(functions, "method0", EMPTY, "method0(String value1)", String.class.getName(), EMPTY, 0);
        assertExists(functions, "method1", "method1('test', 2)", "method1(String value1, int value2)", "byte[]", "My description", 1);
        assertExists(functions, "method2", EMPTY, "method2()", String.class.getName(), "Returns the attributes", 0);
        assertExists(functions, "method3", EMPTY, "method3(String arg0)", boolean.class.getName(), EMPTY, 0);
        assertExists(functions, "method4", EMPTY,"method4(String arg0, int arg1)", int.class.getName(), "My description", 0);
        assertExists(functions, "method5", "TypeFunctionComponent.method5('test', 432)", "method5(String arg0, int arg1)", int.class.getName(), EMPTY, 0);
        assertExists(functions, "method6", EMPTY, "info(String value1, int value2)", "void", "Logs a message with INFO level", 0);
        assertExists(functions, "method7", EMPTY, "method7(String arg0)", TypeFunctionComponent.class.getName(), EMPTY, 2);
        assertExists(functions, "method8", EMPTY, "method8(long arg0)", "byte[]", EMPTY, 0);
    }

    @Test
    void shouldThrowExceptionWhenFunctionNameNotPresentForClassLevelFunction() {
        // Given
        ClassInfo classInfo = classInfoOf(TestFunctionComponentIncorrect.class);
        TypeFunctionAnalyzer analyzer = new TypeFunctionAnalyzer(classInfo);

        // When
        ModuleDescriptorException thrown = assertThrows(ModuleDescriptorException.class, analyzer::analyze);

        // Then
        assertThat(thrown).hasMessage("Name property must be defined for class level @TypeFunction annotation " +
                "(class: com.reedelk.module.descriptor.analyzer.type.TestFunctionComponentIncorrect).");
    }

    private void assertExists(List<TypeFunctionDescriptor> collection,
                              String name,
                              String example,
                              String signature,
                              String returnType,
                              String description,
                              int cursorOffset) {
        TypeFunctionDescriptor expected = create(name, example, signature, returnType, description, cursorOffset);
        assertExists(collection, expected);
    }

    private void assertExists(List<TypeFunctionDescriptor> collection, TypeFunctionDescriptor expected) {
        boolean found = collection.stream().anyMatch(actual -> reflectionEquals(expected, actual));
        assertThat(found).withFailMessage("Could not find expected=" + expected.toString()).isTrue();
    }

    private TypeFunctionDescriptor create(String name,
                                          String example,
                                          String signature,
                                          String returnType,
                                          String description,
                                          int cursorOffset) {
        TypeFunctionDescriptor descriptor = new TypeFunctionDescriptor();
        descriptor.setCursorOffset(cursorOffset);
        descriptor.setDescription(description);
        descriptor.setReturnType(returnType);
        descriptor.setSignature(signature);
        descriptor.setExample(example);
        descriptor.setName(name);
        return descriptor;
    }
}
