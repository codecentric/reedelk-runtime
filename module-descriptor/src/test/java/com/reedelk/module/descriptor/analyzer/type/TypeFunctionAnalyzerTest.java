package com.reedelk.module.descriptor.analyzer.type;

import com.reedelk.module.descriptor.analyzer.ScannerTestUtils;
import com.reedelk.module.descriptor.fixture.ClassWithTypeAnnotations;
import com.reedelk.module.descriptor.model.type.TypeFunctionDescriptor;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TypeFunctionAnalyzerTest {

    private static TypeFunctionAnalyzer analyzer;
    private static ScanResult scanResult;

    @BeforeAll
    static void beforeAll() {
        scanResult = ScannerTestUtils.scanWithResult(ClassWithTypeAnnotations.class);
        ClassInfo classInfo = scanResult.getClassInfo(ClassWithTypeAnnotations.class.getName());
        analyzer = new TypeFunctionAnalyzer(classInfo);
    }

    @Test
    void shouldReturnCorrectFunctionDescriptors() {
        // When
        List<TypeFunctionDescriptor> functions = analyzer.analyze();

        // Then
        assertThat(functions).hasSize(5);
    }
}
