package com.reedelk.module.descriptor.json;

import com.reedelk.module.descriptor.ModuleDescriptor;
import com.reedelk.module.descriptor.ModuleDescriptorException;
import com.reedelk.module.descriptor.fixture.TestJson;
import com.reedelk.module.descriptor.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

import static com.reedelk.module.descriptor.fixture.ComponentProperties.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@ExtendWith(MockitoExtension.class)
class JsonProviderTest {

    @Mock
    private Icon icon;
    @Mock
    private Image image;

    private ComponentDescriptor myProcessorComponent;
    private ComponentDescriptor myInboundComponent;
    private AutoCompleteContributorDescriptor autoCompleteContributorDescriptor;

    @BeforeEach
    void setUp() {
        List<ComponentPropertyDescriptor> myProcessorComponentProperties = asList(
                propertyBooleanObject, propertyBoolean, propertyDoubleObject, propertyDouble,
                propertyFloatObject, propertyFloat, propertyIntegerObject, propertyInteger,
                propertyLongObject, propertyLong, propertyString, propertyBigInteger, propertyBigDecimal,
                propertyMap, propertyEnum,
                propertyDynamicLong, propertyDynamicFloat, propertyDynamicDouble, propertyDynamicObject,
                propertyDynamicString, propertyDynamicBoolean, propertyDynamicInteger, propertyDynamicResource,
                propertyDynamicByteArray, propertyDynamicBigInteger, propertyDynamicBigDecimal, propertyDynamicStringMap,
                propertyScript, propertyCombo, propertyPassword, propertyResourceText, propertyResourceBinary, propertyTypeObject);

        myProcessorComponent = new ComponentDescriptor();
        myProcessorComponent.setComponentType(ComponentType.PROCESSOR);
        myProcessorComponent.setDisplayName("Test Processor Component");
        myProcessorComponent.setFullyQualifiedName("com.test.component.TestProcessorComponent");
        myProcessorComponent.setComponentPropertyDescriptors(myProcessorComponentProperties);
        myProcessorComponent.setHidden(true);
        myProcessorComponent.setImage(image);
        myProcessorComponent.setIcon(icon);

        List<ComponentPropertyDescriptor> myInboundComponentProperties = asList(
                propertyStringWithDefaultValue,
                propertyDynamicString,
                propertyMap);
        myInboundComponent = new ComponentDescriptor();
        myInboundComponent.setComponentType(ComponentType.INBOUND);
        myInboundComponent.setDisplayName("Test Inbound Component");
        myInboundComponent.setFullyQualifiedName("com.test.component.TestInboundComponent");
        myInboundComponent.setComponentPropertyDescriptors(myInboundComponentProperties);
        myInboundComponent.setHidden(true);
        myInboundComponent.setImage(image);
        myInboundComponent.setIcon(icon);

        autoCompleteContributorDescriptor = new AutoCompleteContributorDescriptor();
        autoCompleteContributorDescriptor.setContext(true);
        autoCompleteContributorDescriptor.setError(true);
        autoCompleteContributorDescriptor.setMessage(true);
        autoCompleteContributorDescriptor.setContributions(asList("Util[VARIABLE:Util]", "Util.tmpdir()[FUNCTION:String]"));
    }

    @Nested
    @DisplayName("From ModuleDescriptor to JSON Tests")
    class FromModuleDescriptorObjectToJson {

        @Test
        void shouldCorrectlyConvertModuleDescriptorToJson() throws ModuleDescriptorException {
            // Given
            ModuleDescriptor descriptor = new ModuleDescriptor();
            descriptor.setComponentDescriptors(asList(myProcessorComponent, myInboundComponent));
            descriptor.setAutocompleteContributorDescriptors(Collections.singletonList(autoCompleteContributorDescriptor));

            // When
            String serialized = JsonProvider.toJson(descriptor);

            // Then
            assertEquals(TestJson.COMPONENT_WITH_ALL_SUPPORTED_PROPERTIES.get(), serialized, true);
        }
    }

    @Nested
    @DisplayName("From JSON to ModuleDescriptor Tests")
    class FromJsonToModuleDescriptorObject {

        @Test
        void shouldCorrectlyConvertJsonToModuleDescriptor() throws ModuleDescriptorException {
            // Given
            String input = TestJson.COMPONENT_WITH_ALL_SUPPORTED_PROPERTIES.get();

            // When
            ModuleDescriptor descriptor = JsonProvider.fromJson(input);

            // Then
            assertThat(descriptor).isNotNull();

            // Should contain two modules
            List<ComponentDescriptor> componentDescriptors = descriptor.getComponentDescriptors();
            assertThatExistsComponent(componentDescriptors, myInboundComponent);
            assertThatExistsComponent(componentDescriptors, myProcessorComponent);
        }

        private void assertThatExistsComponent(List<ComponentDescriptor> descriptors, ComponentDescriptor wanted) {
            Optional<ComponentDescriptor> maybeMatchingDescriptor =
                    findComponentDescriptorByQualifiedName(descriptors, wanted.getFullyQualifiedName());
            assertThat(maybeMatchingDescriptor).isPresent();
            ComponentDescriptor matchingDescriptor = maybeMatchingDescriptor.get();
            assertSame(matchingDescriptor, wanted);
        }

        private void assertSame(ComponentDescriptor c1, ComponentDescriptor c2) {
            assertThat(c1.getFullyQualifiedName()).isEqualTo(c2.getFullyQualifiedName());
            assertThat(c1.getComponentType()).isEqualTo(c2.getComponentType());
            assertThat(c1.getDisplayName()).isEqualTo(c2.getDisplayName());
            assertThat(c1.isHidden()).isEqualTo(c2.isHidden());
            assertSame(c1.getComponentPropertyDescriptors(), c2.getComponentPropertyDescriptors());
        }

        private void assertSame(List<ComponentPropertyDescriptor> propertyDescriptors1, List<ComponentPropertyDescriptor> propertyDescriptors2) {
            assertThat(propertyDescriptors1.size()).isEqualTo(propertyDescriptors2.size());
            propertyDescriptors2.forEach(componentPropertyDescriptor -> {
                Optional<ComponentPropertyDescriptor> maybeDescriptor =
                        findPropertyDescriptorMatching(propertyDescriptors1, componentPropertyDescriptor);
                assertThat(maybeDescriptor)
                        .withFailMessage("Could not find matching descriptor for property: " +
                                componentPropertyDescriptor.getPropertyName())
                        .isPresent();
            });
        }

        private Optional<ComponentPropertyDescriptor> findPropertyDescriptorMatching(List<ComponentPropertyDescriptor> descriptors, ComponentPropertyDescriptor target) {
            return descriptors.stream().filter(current -> {
                        boolean sameHint = Objects.equals(current.getHintValue(), target.getHintValue());
                        boolean sameDisplayName = Objects.equals(current.getDisplayName(), target.getDisplayName());
                        boolean samePropertyName = Objects.equals(current.getPropertyName(), target.getPropertyName());
                        boolean sameDefaultValue = Objects.equals(current.getDefaultValue(), target.getDefaultValue());
                        boolean samePropertyInfo = Objects.equals(current.getPropertyInfo(), target.getPropertyInfo());
                        boolean sameWhenDescriptors = sameWhens(current.getWhenDescriptors(), target.getWhenDescriptors());
                        boolean samePropertyType = sameType(current.getPropertyType(), target.getPropertyType());
                        boolean sameScriptSignature = same(current.getScriptSignatureDescriptor(), target.getScriptSignatureDescriptor());
                        boolean sameAutoCompleteContributor = same(current.getAutoCompleteContributorDescriptor(), target.getAutoCompleteContributorDescriptor());
                        return sameHint &&
                                sameDisplayName &&
                                samePropertyName &&
                                sameDefaultValue &&
                                samePropertyInfo &&
                                samePropertyType &&
                                sameWhenDescriptors &&
                                sameScriptSignature &&
                                sameAutoCompleteContributor;
                    }).findFirst();
        }

        private boolean sameWhens(List<WhenDescriptor> whens1, List<WhenDescriptor> whens2) {
            if (whens1.size() != whens2.size()) return false;
            return whens2.stream().allMatch(whenDescriptor ->
                    findWhenMatching(whens1, whenDescriptor).isPresent());
        }

        private Optional<WhenDescriptor> findWhenMatching(List<WhenDescriptor> whens, WhenDescriptor target) {
            return whens.stream().filter(current -> {
                boolean samePropertyName = Objects.equals(current.getPropertyName(), target.getPropertyName());
                boolean samePropertyValue = Objects.equals(current.getPropertyValue(), target.getPropertyValue());
                return samePropertyName && samePropertyValue;
            }).findFirst();
        }

        private boolean same(AutoCompleteContributorDescriptor auto1, AutoCompleteContributorDescriptor auto2) {
            if (auto1 == null && auto2 == null) return true;
            boolean sameIsContext = Objects.equals(auto1.isContext(), auto2.isContext());
            boolean sameIsMessage = Objects.equals(auto1.isMessage(), auto2.isMessage());
            boolean sameIsError = Objects.equals(auto1.isError(), auto2.isError());
            return sameIsContext && sameIsMessage && sameIsError;
        }

        private boolean same(ScriptSignatureDescriptor s1, ScriptSignatureDescriptor s2) {
            if (s1 == null) return s2 == null;
            return s1.getArguments().equals(s2.getArguments());
        }

        private boolean sameType(TypeDescriptor t1, TypeDescriptor t2) {
            boolean sameType = t1.getType() == t2.getType();
            if (t1 instanceof TypeObjectDescriptor) {
                return sameType && samePropertyType((TypeObjectDescriptor) t1, (TypeObjectDescriptor) t2);
            } else if (t1 instanceof TypeMapDescriptor) {
                return sameType && samePropertyType((TypeMapDescriptor) t1, (TypeMapDescriptor) t2);
            } else if (t1 instanceof TypeComboDescriptor) {
                return sameType && samePropertyType((TypeComboDescriptor) t1, (TypeComboDescriptor) t2);
            } else if (t1 instanceof TypeEnumDescriptor) {
                return sameType && samePropertyType((TypeEnumDescriptor) t1, (TypeEnumDescriptor) t2);
            }
            return sameType;
        }

        private boolean samePropertyType(TypeMapDescriptor t1, TypeMapDescriptor t2) {
            return t1.getTabGroup().equals(t2.getTabGroup());
        }

        private boolean samePropertyType(TypeObjectDescriptor t1, TypeObjectDescriptor t2) {
            boolean sameCollapsible = t1.getCollapsible() == t2.getCollapsible();
            boolean sameShared = t1.getShared() == t2.getShared();
            boolean sameFullyQualifiedName = t1.getTypeFullyQualifiedName().equals(t2.getTypeFullyQualifiedName());
            assertSame(t1.getObjectProperties(), t2.getObjectProperties());
            return sameCollapsible && sameShared && sameFullyQualifiedName;
        }

        private boolean samePropertyType(TypeComboDescriptor t1, TypeComboDescriptor t2) {
            boolean sameValues = Arrays.equals(t1.getComboValues(), t2.getComboValues());
            boolean samePrototype = t1.getPrototype().equals(t2.getPrototype());
            return sameValues && samePrototype;
        }

        private boolean samePropertyType(TypeEnumDescriptor t1, TypeEnumDescriptor t2) {
            return t1.getNameAndDisplayNameMap().equals(t2.getNameAndDisplayNameMap());
        }

        private Optional<ComponentDescriptor> findComponentDescriptorByQualifiedName(List<ComponentDescriptor> descriptors, String componentFullyQualifiedName) {
            return descriptors.stream()
                    .filter(componentDescriptor ->
                            componentFullyQualifiedName.equals(componentDescriptor.getFullyQualifiedName()))
                    .findFirst();
        }
    }
}