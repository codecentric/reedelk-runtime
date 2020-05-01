package com.reedelk.module.descriptor.analyzer.component;

import com.reedelk.module.descriptor.analyzer.ScannerTestUtils;
import com.reedelk.module.descriptor.fixture.ClassWithComponentInputAndOutputAnnotation;
import com.reedelk.module.descriptor.fixture.MyAttributes;
import com.reedelk.module.descriptor.model.component.ComponentOutputDescriptor;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class ComponentOutputAnalyzerTest {

    private static ComponentOutputAnalyzer analyzer;
    private static ScanResult scanResult;

    @BeforeAll
    static void beforeAll() {
        scanResult = ScannerTestUtils.scanWithResult(ClassWithComponentInputAndOutputAnnotation.class);
        ClassInfo classInfo = scanResult.getClassInfo(ClassWithComponentInputAndOutputAnnotation.class.getName());
        analyzer = new ComponentOutputAnalyzer(classInfo);
    }

    @Test
    void shouldCorrectlyAnalyzeComponentInput() {
        // When
        ComponentOutputDescriptor actual = analyzer.analyze();

        // Then
        assertThat(actual).isNotNull();
        assertThat(actual.getAttributes()).isEqualTo(MyAttributes.class.getName());
        assertThat(actual.getDescription()).isEqualTo("My output description");
        assertThat(actual.getPayload()).isEqualTo(asList("long", "java.lang.Byte[]"));
    }
}
