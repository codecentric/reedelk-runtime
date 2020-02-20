package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.module.descriptor.analyzer.ObjectFactories;
import com.reedelk.module.descriptor.analyzer.ScannerTestUtils;
import com.reedelk.module.descriptor.analyzer.component.ComponentAnalyzerContext;
import com.reedelk.module.descriptor.model.PropertyDescriptor;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.FieldInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PropertyAutoCompleteContributorAnalyzerTest {

    @Mock
    private ComponentAnalyzerContext context;

    private PropertyAutoCompleteContributorAnalyzer analyzer = new PropertyAutoCompleteContributorAnalyzer();

    private static ClassInfo componentClassInfo;

    @BeforeAll
    static void beforeAll() {
        ScannerTestUtils.ScanContext scanContext = ScannerTestUtils.scan(TestComponentWithAutoCompleteContributor.class);
        componentClassInfo = scanContext.targetComponentClassInfo;
    }

    @Test
    void shouldCorrectlyCreateAutoCompleteContributorDefinitionWithCustomContributions() {
        // Given
        String propertyName = "propertyWithCustomContributions";
        FieldInfo property = componentClassInfo.getFieldInfo(propertyName);

        PropertyDescriptor.Builder builder =
                PropertyDescriptor.builder()
                        .propertyName(propertyName)
                        .type(ObjectFactories.createTypeDynamicValueDescriptor(DynamicString.class));

        // When
        analyzer.handle(property, builder, context);

        // Then
        PropertyDescriptor descriptor = builder.build();
        AutoCompleteContributorDefinitionMatchers.AutoCompleteContributorDefinitionMatcher matcher = AutoCompleteContributorDefinitionMatchers.with(true, true, false, asList("messages[VARIABLE:Message[]]", "messages.size()[FUNCTION:int]"));
        assertThat(matcher.matches(descriptor.getAutocompleteContributor())).isTrue();
    }

    @Test
    void shouldCorrectlyCreateAutoCompleteContributorDefinitionWithoutMessageContributions() {
        // Given
        String propertyName = "propertyWithoutMessageContributions";
        FieldInfo property = componentClassInfo.getFieldInfo(propertyName);

        PropertyDescriptor.Builder builder =
                PropertyDescriptor.builder()
                        .propertyName(propertyName)
                        .type(ObjectFactories.createTypeDynamicValueDescriptor(DynamicString.class));

        // When
        analyzer.handle(property, builder, context);

        // Then
        PropertyDescriptor descriptor = builder.build();
        AutoCompleteContributorDefinitionMatchers.AutoCompleteContributorDefinitionMatcher matcher = AutoCompleteContributorDefinitionMatchers.with(false,true,false, emptyList());
        assertThat(matcher.matches(descriptor.getAutocompleteContributor())).isTrue();
    }

    @Test
    void shouldCorrectlyCreateAutoCompleteContributorDefinitionWithoutContextContributions() {
        // Given
        String propertyName = "propertyWithoutContextContributions";
        FieldInfo property = componentClassInfo.getFieldInfo(propertyName);

        PropertyDescriptor.Builder builder =
                PropertyDescriptor.builder()
                        .propertyName(propertyName)
                        .type(ObjectFactories.createTypeDynamicValueDescriptor(DynamicString.class));

        // When
        analyzer.handle(property, builder, context);

        // Then
        PropertyDescriptor descriptor = builder.build();
        AutoCompleteContributorDefinitionMatchers.AutoCompleteContributorDefinitionMatcher matcher = AutoCompleteContributorDefinitionMatchers.with(true,false,false, emptyList());
        assertThat(matcher.matches(descriptor.getAutocompleteContributor())).isTrue();
    }

    @Test
    void shouldCorrectlyCreateAutoCompleteContributorDefinitionWithErrorAndWithoutMessageContributions() {
        // Given
        String propertyName = "propertyWithErrorAndWithoutMessageContributions";
        FieldInfo property = componentClassInfo.getFieldInfo(propertyName);

        PropertyDescriptor.Builder builder =
                PropertyDescriptor.builder()
                        .propertyName(propertyName)
                        .type(ObjectFactories.createTypeDynamicValueDescriptor(DynamicString.class));

        // When
        analyzer.handle(property, builder, context);

        // Then
        PropertyDescriptor descriptor = builder.build();
        AutoCompleteContributorDefinitionMatchers.AutoCompleteContributorDefinitionMatcher matcher = AutoCompleteContributorDefinitionMatchers.with(false, true,true, emptyList());
        assertThat(matcher.matches(descriptor.getAutocompleteContributor())).isTrue();
    }

    @Test
    void shouldProvideEmptyAutoCompleteContributorWhenPropertyDoesNotHaveOne() {
        // Given
        String propertyName = "propertyWithoutAutoCompleteContributor";
        FieldInfo property = componentClassInfo.getFieldInfo(propertyName);

        PropertyDescriptor.Builder builder =
                PropertyDescriptor.builder()
                        .propertyName(propertyName)
                        .type(ObjectFactories.createTypeDynamicValueDescriptor(DynamicString.class));

        // When
        analyzer.handle(property, builder, context);

        // Then
        PropertyDescriptor descriptor = builder.build();
        assertThat(descriptor.getAutocompleteContributor()).isNull();
    }
}
