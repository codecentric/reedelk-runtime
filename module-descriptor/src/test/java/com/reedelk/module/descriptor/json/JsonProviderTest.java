package com.reedelk.module.descriptor.json;

import com.reedelk.module.descriptor.ModuleDescriptorException;
import com.reedelk.module.descriptor.fixture.ClassWithTypeAnnotations;
import com.reedelk.module.descriptor.fixture.TestJson;
import com.reedelk.module.descriptor.model.ModuleDescriptor;
import com.reedelk.module.descriptor.model.component.ComponentDescriptor;
import com.reedelk.module.descriptor.model.component.ComponentType;
import com.reedelk.module.descriptor.model.property.PropertyDescriptor;
import com.reedelk.module.descriptor.model.type.TypeDescriptor;
import com.reedelk.module.descriptor.model.type.TypeFunctionDescriptor;
import com.reedelk.module.descriptor.model.type.TypePropertyDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

import static com.reedelk.module.descriptor.fixture.ComponentProperties.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@ExtendWith(MockitoExtension.class)
class JsonProviderTest {

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


    @Test
    void shouldCorrectlyConvertModuleDescriptorToJson() throws ModuleDescriptorException {
        // Given
        TypePropertyDescriptor propertyDescriptor = new TypePropertyDescriptor();
        propertyDescriptor.setDescription("The correlation ID");
        propertyDescriptor.setExample("context.correlationId");
        propertyDescriptor.setType(String.class.getName());
        propertyDescriptor.setName("correlationId");

        TypeFunctionDescriptor functionDescriptor = new TypeFunctionDescriptor();
        functionDescriptor.setExample("Utils.myFunction('myKey')");
        functionDescriptor.setSignature("myFunction(String key)");
        functionDescriptor.setReturnType(String.class.getName());
        functionDescriptor.setDescription("My function");
        functionDescriptor.setName("myFunction");
        functionDescriptor.setCursorOffset(3);

        TypeDescriptor typeDescriptor = new TypeDescriptor();
        typeDescriptor.setDisplayName("Utils");
        typeDescriptor.setGlobal(true);
        typeDescriptor.setListItemType(Map.class.getName());

        typeDescriptor.setFullyQualifiedName("com.test.Utils");
        typeDescriptor.setProperties(singletonList(propertyDescriptor));
        typeDescriptor.setFunctions(singletonList(functionDescriptor));


        ModuleDescriptor descriptor = new ModuleDescriptor();
        descriptor.setComponents(asList(myProcessorComponent, myInboundComponent));
        descriptor.setTypes(singletonList(typeDescriptor));
        descriptor.setDisplayName("Module XYZ");
        descriptor.setName("module-xyz");

        // When
        String serialized = JsonProvider.toJson(descriptor);

        // Then
        assertEquals(TestJson.COMPONENT_WITH_ALL_SUPPORTED_PROPERTIES.get(), serialized, true);
    }

    @Test
    void shouldCorrectlyConvertJsonToModuleDescriptor() {
        // Given
        String input = TestJson.COMPONENT_WITH_ALL_SUPPORTED_PROPERTIES.get();

        // When
        ModuleDescriptor descriptor = JsonProvider.fromJson(input);

        // Then
        assertThat(descriptor).isNotNull();

        // Should contain two modules

        /**
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
         */
    }
}
