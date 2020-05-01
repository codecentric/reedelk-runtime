package com.reedelk.module.descriptor.analyzer.component;

import com.reedelk.module.descriptor.analyzer.ScannerTestUtils;
import com.reedelk.module.descriptor.fixture.ClassWithComponentInputAndOutputAnnotation;
import com.reedelk.module.descriptor.model.component.ComponentInputDescriptor;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class ComponentInputAnalyzerTest {

    private static ComponentInputAnalyzer analyzer;
    private static ScanResult scanResult;

    @BeforeAll
    static void beforeAll() {
        scanResult = ScannerTestUtils.scanWithResult(ClassWithComponentInputAndOutputAnnotation.class);
        ClassInfo classInfo = scanResult.getClassInfo(ClassWithComponentInputAndOutputAnnotation.class.getName());
        analyzer = new ComponentInputAnalyzer(classInfo);
    }

    @Test
    void shouldCorrectlyAnalyzeComponentInput() {
        // When
        ComponentInputDescriptor actual = analyzer.analyze();

        // Then
        assertThat(actual).isNotNull();
        assertThat(actual.getDescription()).isEqualTo("My input description");
        assertThat(actual.getPayload()).isEqualTo(asList("java.lang.String", "byte[]"));
    }
}
