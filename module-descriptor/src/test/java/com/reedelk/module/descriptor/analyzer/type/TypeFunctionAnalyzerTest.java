package com.reedelk.module.descriptor.analyzer.type;

import com.reedelk.module.descriptor.model.type.TypeFunctionDescriptor;
import io.github.classgraph.ClassInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.reedelk.module.descriptor.analyzer.AnalyzerTestUtils.classInfoOf;
import static com.reedelk.runtime.api.commons.StringUtils.EMPTY;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.assertj.core.api.Assertions.assertThat;

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


        TypeFunctionDescriptor expectedMethod0 = create("method0",
                EMPTY,
                "method0(String value1)",
                String.class.getName(),
                EMPTY,
                0);
        assertExists(functions, expectedMethod0);

        TypeFunctionDescriptor expectedMethod1 = create("method1",
                "method1('test', 2)",
                "method1(String value1, int value2)",
                "byte[]",
                "My description",
                1);
        assertExists(functions, expectedMethod1);

        TypeFunctionDescriptor expectedMethod2 = create("method2",
                EMPTY,
                "method2()",
                String.class.getName(),
                "Returns the attributes",
                0);
        assertExists(functions, expectedMethod2);

        TypeFunctionDescriptor expectedMethod3 = create("method3",
                EMPTY,
                "method3(String arg0)",
                boolean.class.getName(),
                EMPTY,
                0);
        assertExists(functions, expectedMethod3);

        TypeFunctionDescriptor expectedMethod4 = create("method4",
                EMPTY,
                "method4(String arg0, int arg1)",
                int.class.getName(),
                "My description",
                0);
        assertExists(functions, expectedMethod4);

        TypeFunctionDescriptor expectedMethod5 = create("method5",
                "TypeFunctionComponent.method5('test', 432)",
                "method5(String arg0, int arg1)",
                int.class.getName(),
                EMPTY,
                0);
        assertExists(functions, expectedMethod5);

        TypeFunctionDescriptor expectedMethod6 = create("method6",
                EMPTY,
                "info(String value1, int value2)",
                "void",
                "Logs a message with INFO level",
                0);
        assertExists(functions, expectedMethod6);

        TypeFunctionDescriptor expectedMethod7 = create("method7",
                EMPTY,
                "method7(String arg0)",
                TypeFunctionComponent.class.getName(),
                EMPTY,
                2);
        assertExists(functions, expectedMethod7);

        TypeFunctionDescriptor expectedMethod8 = create("method8",
                EMPTY,
                "method8(long arg0)",
                "byte[]",
                EMPTY,
                0);
        assertExists(functions, expectedMethod8);
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
