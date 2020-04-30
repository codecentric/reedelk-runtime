package com.reedelk.module.descriptor.analyzer.autocomplete;

import com.reedelk.module.descriptor.analyzer.ScannerTestUtils;
import com.reedelk.module.descriptor.analyzer.type.TypeFunctionAnalyzer;
import com.reedelk.module.descriptor.fixture.ClassWithTypeAnnotations;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TypeFunctionDescriptorAnalyzerTest {

    private static TypeFunctionAnalyzer analyzer;

    @BeforeAll
    static void beforeAll() {
        ScanResult scanResult = ScannerTestUtils.scanWithResult(ClassWithTypeAnnotations.class);
        analyzer = new TypeFunctionAnalyzer(scanResult.getClassInfo(ClassWithTypeAnnotations.class.getName()));
    }
//TODO: Fixme.
    /**
    @Test
    void shouldCorrectlyAnalyzeAutocompleteItems() {
        // When
        List<AutocompleteItemDescriptor> descriptors = analyzer.analyze();

        // Then
        assertThat(descriptors).hasSize(5);

        AutocompleteItemDescriptor expected = AutocompleteItemDescriptor.create()
                .cursorOffset(0)
                .returnType("String")
                .token("correlationId")
                .example(StringUtils.EMPTY)
                .type(TestClassWithAutocompleteType.class.getSimpleName())
                .description("Returns the correlation id").signature("correlationId")
                .build();
        assertThatExists(descriptors, expected);

        expected = AutocompleteItemDescriptor.create()
                .cursorOffset(2)
                .token("contains")
                .returnType("boolean")
                .example(StringUtils.EMPTY)
                .signature("contains('')")
                .type(TestClassWithAutocompleteType.class.getSimpleName())
                .description("Return the message typed content containing metadata")
                .build();
        assertThatExists(descriptors, expected);

        expected = AutocompleteItemDescriptor.create()
                .cursorOffset(0)
                .token("attributes")
                .returnType("String")
                .example("message.attributes()")
                .signature("attributes()")
                .type(TestClassWithAutocompleteType.class.getSimpleName())
                .description("Returns the attributes")
                .build();
        assertThatExists(descriptors, expected);

        expected = AutocompleteItemDescriptor.create()
                .cursorOffset(0)
                .token("builderMethod")
                .example(StringUtils.EMPTY)
                .signature("builderMethod('')")
                .description("My description")
                .type(TestClassWithAutocompleteType.class.getSimpleName())
                .returnType(TestClassWithAutocompleteType.class.getSimpleName())
                .build();
        assertThatExists(descriptors, expected);

        expected = AutocompleteItemDescriptor.create()
                .cursorOffset(2)
                .token("info")
                .example(StringUtils.EMPTY)
                .returnType("void")
                .signature("info('')")
                .description("Logs a message with INFO level")
                .type(TestClassWithAutocompleteType.class.getSimpleName())
                .build();
        assertThatExists(descriptors, expected);
    }

    private void assertThatExists(List<AutocompleteItemDescriptor> descriptors, AutocompleteItemDescriptor expected) {
        Matcher<AutocompleteItemDescriptor> matcher = Matchers.ofAutocompleteItemDescriptor(expected);
        boolean matches = descriptors.stream().anyMatch(matcher::matches);
        assertThat(matches)
                .withFailMessage("Could not find: " + expected + " from collection: " + descriptors)
                .isTrue();
    }*/
}
