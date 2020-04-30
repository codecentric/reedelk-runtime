package com.reedelk.module.descriptor.json;

import com.reedelk.module.descriptor.fixture.ClassWithTypeAnnotations;
import com.reedelk.module.descriptor.model.component.ComponentDescriptor;
import com.reedelk.module.descriptor.model.component.ComponentType;
import com.reedelk.module.descriptor.model.property.PropertyDescriptor;
import com.reedelk.module.descriptor.model.type.TypeFunctionDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.reedelk.module.descriptor.fixture.ComponentProperties.*;
import static java.util.Arrays.asList;

@ExtendWith(MockitoExtension.class)
class JsonProviderTest {

    // TODO: Add these tests.
    @Mock
    private Icon icon;
    @Mock
    private Image image;

    private static TypeFunctionDescriptor create() {
        TypeFunctionDescriptor functionDescriptor = new TypeFunctionDescriptor();
        functionDescriptor.setName("builderMethod");
        functionDescriptor.setCursorOffset(0);
        functionDescriptor.setSignature("builderMethod(value: string)");
        functionDescriptor.setDescription("My description");
        functionDescriptor.setReturnType(ClassWithTypeAnnotations.class.getSimpleName());
        return functionDescriptor;
    }

    private ComponentDescriptor myProcessorComponent;
    private ComponentDescriptor myInboundComponent;
    private TypeFunctionDescriptor functionDescriptor1;


    @BeforeEach
    void setUp() {
        List<PropertyDescriptor> myProcessorComponentProperties = asList(
                propertyBooleanObject, propertyBoolean, propertyDoubleObject, propertyDouble,
                propertyFloatObject, propertyFloat, propertyIntegerObject, propertyInteger,
                propertyLongObject, propertyLong, propertyString, propertyBigInteger, propertyBigDecimal,
                propertyMap, propertyEnum,
                propertyDynamicLong, propertyDynamicFloat, propertyDynamicDouble, propertyDynamicObject,
                propertyDynamicString, propertyDynamicBoolean, propertyDynamicInteger, propertyDynamicResource,
                propertyDynamicByteArray, propertyDynamicBigInteger, propertyDynamicBigDecimal, propertyDynamicStringMap,
                propertyScript, propertyCombo, propertyPassword, propertyResourceText, propertyResourceBinary, propertyTypeObject);

        myProcessorComponent = new ComponentDescriptor();
        myProcessorComponent.setType(ComponentType.PROCESSOR);
        myProcessorComponent.setDisplayName("Test Processor Component");
        myProcessorComponent.setFullyQualifiedName("com.test.component.TestProcessorComponent");
        myProcessorComponent.setProperties(myProcessorComponentProperties);
        myProcessorComponent.setHidden(true);
        myProcessorComponent.setImage(image);
        myProcessorComponent.setIcon(icon);

        List<PropertyDescriptor> myInboundComponentProperties = asList(
                propertyStringWithInitValue,
                propertyDynamicString,
                propertyMap);
        myInboundComponent = new ComponentDescriptor();
        myInboundComponent.setType(ComponentType.INBOUND);
        myInboundComponent.setDisplayName("Test Inbound Component");
        myInboundComponent.setFullyQualifiedName("com.test.component.TestInboundComponent");
        myInboundComponent.setProperties(myInboundComponentProperties);
        myInboundComponent.setHidden(true);
        myInboundComponent.setImage(image);
        myInboundComponent.setIcon(icon);
    }
    // TODO: Finish me
    /**
     String serialized = JsonProvider.toJson(descriptor);

    @Nested
    @DisplayName("From ModuleDescriptor to JSON Tests")
    class FromModuleDescriptorObjectToJson {

        @Test
        void shouldCorrectlyConvertModuleDescriptorToJson() throws ModuleDescriptorException {
            // Given
            ModuleDescriptor descriptor = new ModuleDescriptor();
            descriptor.setComponents(asList(myProcessorComponent, myInboundComponent));
            descriptor.setAutocompleteItems(asList(autocompleteItem1, autocompleteItem2));
            descriptor.setAutocompleteTypes(asList(autocompleteType1, autocompleteType2));

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
        void shouldCorrectlyConvertJsonToModuleDescriptor() {
            // Given
            String input = TestJson.COMPONENT_WITH_ALL_SUPPORTED_PROPERTIES.get();

            // When
            ModuleDescriptor descriptor = JsonProvider.fromJson(input);

            // Then
            assertThat(descriptor).isNotNull();

            // Should contain two modules
            List<ComponentDescriptor> componentDescriptors = descriptor.getComponents();
            assertThatExistsComponent(componentDescriptors, myInboundComponent);
            assertThatExistsComponent(componentDescriptors, myProcessorComponent);

            // Should contain two autocomplete items
            List<AutocompleteItemDescriptor> autocompleteItems = descriptor.getAutocompleteItems();
            assertThat(autocompleteItems).hasSize(2);
            assertThatExists(autocompleteItems, autocompleteItem1);
            assertThatExists(autocompleteItems, autocompleteItem2);

            List<AutocompleteTypeDescriptor> autocompleteTypes = descriptor.getAutocompleteTypes();
            assertThat(autocompleteTypes).hasSize(2);
            assertThatExists(autocompleteTypes, autocompleteType1);
            assertThatExists(autocompleteTypes, autocompleteType2);
        }

        private void assertThatExists(List<AutocompleteTypeDescriptor> descriptors, AutocompleteTypeDescriptor expected) {
            Matcher<AutocompleteTypeDescriptor> matcher = Matchers.ofAutocompleteTypeDescriptor(expected);
            boolean matches = descriptors.stream().anyMatch(matcher::matches);
            assertThat(matches)
                    .withFailMessage("Could not find: " + expected + " from collection: " + descriptors)
                    .isTrue();
        }

        private void assertThatExists(List<AutocompleteItemDescriptor> descriptors, AutocompleteItemDescriptor expected) {
            Matcher<AutocompleteItemDescriptor> matcher = Matchers.ofAutocompleteItemDescriptor(expected);
            boolean matches = descriptors.stream().anyMatch(matcher::matches);
            assertThat(matches)
                    .withFailMessage("Could not find: " + expected + " from collection: " + descriptors)
                    .isTrue();
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
            assertThat(c1.getType()).isEqualTo(c2.getType());
            assertThat(c1.getDisplayName()).isEqualTo(c2.getDisplayName());
            assertThat(c1.isHidden()).isEqualTo(c2.isHidden());
            assertSame(c1.getProperties(), c2.getProperties());
        }

        private void assertSame(List<PropertyDescriptor> propertyDescriptors1, List<PropertyDescriptor> propertyDescriptors2) {
            assertThat(propertyDescriptors1.size()).isEqualTo(propertyDescriptors2.size());
            propertyDescriptors2.forEach(componentPropertyDescriptor -> {
                Optional<PropertyDescriptor> maybeDescriptor =
                        findPropertyDescriptorMatching(propertyDescriptors1, componentPropertyDescriptor);
                assertThat(maybeDescriptor)
                        .withFailMessage("Could not find matching descriptor for property: " +
                                componentPropertyDescriptor.getName())
                        .isPresent();
            });
        }

        private Optional<PropertyDescriptor> findPropertyDescriptorMatching(List<PropertyDescriptor> descriptors, PropertyDescriptor target) {
            return descriptors.stream().filter(current -> {
                boolean sameExample = Objects.equals(current.getExample(), target.getExample());
                boolean sameHint = Objects.equals(current.getHintValue(), target.getHintValue());
                boolean sameDisplayName = Objects.equals(current.getDisplayName(), target.getDisplayName());
                boolean sameDefaultValue = Objects.equals(current.getDefaultValue(), target.getDefaultValue());
                boolean samePropertyName = Objects.equals(current.getName(), target.getName());
                boolean sameInitValue = Objects.equals(current.getInitValue(), target.getInitValue());
                boolean samePropertyDescription = Objects.equals(current.getDescription(), target.getDescription());
                boolean sameWhenDescriptors = sameWhens(current.getWhens(), target.getWhens());
                boolean samePropertyType = sameType(current.getType(), target.getType());
                boolean sameScriptSignature = same(current.getScriptSignature(), target.getScriptSignature());
                return sameHint &&
                        sameExample &&
                        sameDisplayName &&
                        sameDefaultValue &&
                        samePropertyName &&
                        sameInitValue &&
                        samePropertyDescription &&
                        samePropertyType &&
                        sameWhenDescriptors &&
                        sameScriptSignature;
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

        private boolean same(ScriptSignatureDescriptor s1, ScriptSignatureDescriptor s2) {
            if (s1 == null) return s2 == null;
            if (s2 == null) return false;
            return Objects.equals(s1.getArguments(), s2.getArguments());
        }

        private boolean sameType(PropertyTypeDescriptor t1, PropertyTypeDescriptor t2) {
            if (t1 == null) return t2 == null;
            if (t2 == null) return false;
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
    */
}
