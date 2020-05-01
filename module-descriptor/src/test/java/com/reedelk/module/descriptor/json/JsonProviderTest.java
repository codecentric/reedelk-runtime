package com.reedelk.module.descriptor.json;

import com.reedelk.module.descriptor.ModuleDescriptorException;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static com.reedelk.module.descriptor.fixture.ComponentProperties.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@ExtendWith(MockitoExtension.class)
class JsonProviderTest {

    private ModuleDescriptor moduleDescriptor;

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

        ComponentDescriptor myProcessorComponent = new ComponentDescriptor();
        myProcessorComponent.setType(ComponentType.PROCESSOR);
        myProcessorComponent.setDisplayName("Test Processor Component");
        myProcessorComponent.setFullyQualifiedName("com.test.component.TestProcessorComponent");
        myProcessorComponent.setProperties(myProcessorComponentProperties);
        myProcessorComponent.setHidden(true);

        List<PropertyDescriptor> myInboundComponentProperties = asList(
                propertyStringWithInitValue,
                propertyDynamicString,
                propertyMap);
        ComponentDescriptor myInboundComponent = new ComponentDescriptor();
        myInboundComponent.setType(ComponentType.INBOUND);
        myInboundComponent.setDisplayName("Test Inbound Component");
        myInboundComponent.setFullyQualifiedName("com.test.component.TestInboundComponent");
        myInboundComponent.setProperties(myInboundComponentProperties);
        myInboundComponent.setHidden(true);

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
        typeDescriptor.setDescription("My type description");
        typeDescriptor.setListItemType(Map.class.getName());

        typeDescriptor.setType("com.test.Utils");
        typeDescriptor.setProperties(singletonList(propertyDescriptor));
        typeDescriptor.setFunctions(singletonList(functionDescriptor));

        moduleDescriptor = new ModuleDescriptor();
        moduleDescriptor.setComponents(asList(myProcessorComponent, myInboundComponent));
        moduleDescriptor.setTypes(singletonList(typeDescriptor));
        moduleDescriptor.setDisplayName("Module XYZ");
        moduleDescriptor.setName("module-xyz");
    }

    @Test
    void shouldConvertModuleDescriptorToJson() throws ModuleDescriptorException {
        // When
        String serialized = JsonProvider.toJson(moduleDescriptor);

        // Then
        assertEquals(TestJson.COMPONENT_WITH_ALL_SUPPORTED_PROPERTIES.get(), serialized, true);
    }

    @Test
    void shouldConvertJsonToModuleDescriptor() {
        // Given
        String input = TestJson.COMPONENT_WITH_ALL_SUPPORTED_PROPERTIES.get();

        // When
        ModuleDescriptor actual = JsonProvider.fromJson(input);

        // Then
        // Must keep the toString methods in sync here.
        assertThat(actual.toString()).isEqualTo(moduleDescriptor.toString());
    }
}
