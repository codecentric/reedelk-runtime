package com.reedelk.module.descriptor.analyzer.type;

import com.reedelk.module.descriptor.analyzer.ScannerTestUtils;
import com.reedelk.module.descriptor.fixture.ClassMapWithTypeAnnotations;
import com.reedelk.module.descriptor.model.type.TypePropertyDescriptor;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TypePropertyAnalyzerTest {

    private static TypePropertyAnalyzer analyzer;
    private static ScanResult scanResult;

    @BeforeAll
    static void beforeAll() {
        scanResult = ScannerTestUtils.scanWithResult(ClassMapWithTypeAnnotations.class);
        ClassInfo classInfo = scanResult.getClassInfo(ClassMapWithTypeAnnotations.class.getName());
        analyzer = new TypePropertyAnalyzer(classInfo);
    }

    @Test
    void shouldReturnCorrectPropertyDescriptors() {
        // When
        List<TypePropertyDescriptor> properties = analyzer.analyze();

        // Then
        assertThat(properties).hasSize(4);
    }
}
