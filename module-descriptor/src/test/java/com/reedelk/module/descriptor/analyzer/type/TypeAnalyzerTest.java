package com.reedelk.module.descriptor.analyzer.type;

import com.reedelk.module.descriptor.analyzer.ScannerTestUtils;
import com.reedelk.module.descriptor.fixture.ClassWithTypeAnnotations;
import com.reedelk.module.descriptor.model.type.TypeDescriptor;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TypeAnalyzerTest {

    private static TypeAnalyzer analyzer;
    private static ScanResult scanResult;

    @BeforeAll
    static void beforeAll() {
        scanResult = ScannerTestUtils.scanWithResult(ClassWithTypeAnnotations.class);
        analyzer = new TypeAnalyzer(scanResult);
    }

    @Test
    void shouldReturnCorrectFunctionDescriptors() {
        // When
        List<TypeDescriptor> types = analyzer.analyze();

        // Then
        assertThat(types).hasSize(2);
    }
}
