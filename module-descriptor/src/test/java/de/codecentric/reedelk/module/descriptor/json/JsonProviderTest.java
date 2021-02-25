package de.codecentric.reedelk.module.descriptor.json;

import de.codecentric.reedelk.module.descriptor.ModuleDescriptorException;
import de.codecentric.reedelk.module.descriptor.fixture.ComponentProperties;
import de.codecentric.reedelk.module.descriptor.fixture.TestJson;
import de.codecentric.reedelk.module.descriptor.model.ModuleDescriptor;
import de.codecentric.reedelk.module.descriptor.model.component.ComponentDescriptor;
import de.codecentric.reedelk.module.descriptor.model.component.ComponentType;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyDescriptor;
import de.codecentric.reedelk.module.descriptor.model.type.TypeDescriptor;
import de.codecentric.reedelk.module.descriptor.model.type.TypeFunctionDescriptor;
import de.codecentric.reedelk.module.descriptor.model.type.TypePropertyDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@Disabled
@ExtendWith(MockitoExtension.class)
class JsonProviderTest {

    private ModuleDescriptor moduleDescriptor;

    @BeforeEach
    void setUp() {

        List<PropertyDescriptor> myProcessorComponentProperties = asList(
                ComponentProperties.propertyBooleanObject, ComponentProperties.propertyBoolean, ComponentProperties.propertyDoubleObject, ComponentProperties.propertyDouble,
                ComponentProperties.propertyFloatObject, ComponentProperties.propertyFloat, ComponentProperties.propertyIntegerObject, ComponentProperties.propertyInteger,
                ComponentProperties.propertyLongObject, ComponentProperties.propertyLong, ComponentProperties.propertyString, ComponentProperties.propertyBigInteger, ComponentProperties.propertyBigDecimal,
                ComponentProperties.propertyMap, ComponentProperties.propertyEnum,
                ComponentProperties.propertyDynamicLong, ComponentProperties.propertyDynamicFloat, ComponentProperties.propertyDynamicDouble, ComponentProperties.propertyDynamicObject,
                ComponentProperties.propertyDynamicString, ComponentProperties.propertyDynamicBoolean, ComponentProperties.propertyDynamicInteger, ComponentProperties.propertyDynamicResource,
                ComponentProperties.propertyDynamicByteArray, ComponentProperties.propertyDynamicBigInteger, ComponentProperties.propertyDynamicBigDecimal, ComponentProperties.propertyDynamicStringMap,
                ComponentProperties.propertyScript, ComponentProperties.propertyCombo, ComponentProperties.propertyPassword, ComponentProperties.propertyResourceText, ComponentProperties.propertyResourceBinary, ComponentProperties.propertyTypeObject);

        ComponentDescriptor myProcessorComponent = new ComponentDescriptor();
        myProcessorComponent.setType(ComponentType.PROCESSOR);
        myProcessorComponent.setDisplayName("Test Processor Component");
        myProcessorComponent.setFullyQualifiedName("com.test.component.TestProcessorComponent");
        myProcessorComponent.setProperties(myProcessorComponentProperties);
        myProcessorComponent.setHidden(true);

        List<PropertyDescriptor> myInboundComponentProperties = asList(
                ComponentProperties.propertyStringWithInitValue,
                ComponentProperties.propertyDynamicString,
                ComponentProperties.propertyMap);
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
