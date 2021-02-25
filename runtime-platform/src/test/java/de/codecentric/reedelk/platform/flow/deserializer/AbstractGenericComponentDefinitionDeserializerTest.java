package de.codecentric.reedelk.platform.flow.deserializer;

import de.codecentric.reedelk.platform.test.utils.*;
import de.codecentric.reedelk.platform.flow.deserializer.converter.DeserializerConverterContextDecorator;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.platform.module.DeSerializedModule;
import de.codecentric.reedelk.platform.module.ModulesManager;
import de.codecentric.reedelk.runtime.api.component.Component;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import de.codecentric.reedelk.runtime.converter.DeserializerConverter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.Bundle;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
public class AbstractGenericComponentDefinitionDeserializerTest {

    private final long testModuleId = 10L;

    @Mock
    protected Bundle mockBundle;
    @Mock
    protected ExecutionNode mockExecutionNode;
    @Mock
    protected ModulesManager mockModulesManager;
    @Mock
    protected DeSerializedModule mockDeSerializedModule;

    protected FlowDeserializerContext context;
    protected GenericComponentDefinitionDeserializer deserializer;

    @BeforeEach
    protected void setUp() {
        DeserializerConverter factory = DeserializerConverter.getInstance();
        factory = new DeserializerConverterContextDecorator(factory, testModuleId);
        context = Mockito.spy(new FlowDeserializerContext(mockBundle, mockModulesManager, mockDeSerializedModule, factory));
        deserializer = new GenericComponentDefinitionDeserializer(mockExecutionNode, context);
    }

    protected TestComponent buildComponentWith(String propertyName, Object propertyValue) {
        JSONObject definition = componentDefinitionWith(propertyName, propertyValue, TestComponent.class);
        TestComponent implementor = new TestComponent();
        deserializer.deserialize(definition, implementor);
        return implementor;
    }

    protected TestComponentWithDynamicValueProperty buildDynamicValueComponentWith(String propertyName, Object propertyValue) {
        JSONObject definition = componentDefinitionWith(propertyName, propertyValue, TestComponentWithDynamicValueProperty.class);
        TestComponentWithDynamicValueProperty implementor = new TestComponentWithDynamicValueProperty();
        deserializer.deserialize(definition, implementor);
        return implementor;
    }

    protected TestComponentWithDynamicMapProperty buildDynamicMapComponentWith(String propertyName, JSONObject propertyValue) {
        JSONObject definition = componentDefinitionWith(propertyName, propertyValue, TestComponentWithDynamicMapProperty.class);
        TestComponentWithDynamicMapProperty implementor = new TestComponentWithDynamicMapProperty();
        deserializer.deserialize(definition, implementor);
        return implementor;
    }

    protected TestComponentWithCollectionProperties buildCollectionComponentWith(String propertyName, Object propertyValue) {
        JSONObject definition = componentDefinitionWith(propertyName, propertyValue, TestComponentWithCollectionProperties.class);
        TestComponentWithCollectionProperties implementor = new TestComponentWithCollectionProperties();
        deserializer.deserialize(definition, implementor);
        return implementor;
    }

    protected JSONObject componentDefinitionWith(String propertyName, Object propertyValue, Class<? extends Component> componentClazz) {
        JSONObject componentDefinition = ComponentsBuilder.forComponent(componentClazz)
                .with(propertyName, propertyValue)
                .build();
        mockComponent(componentClazz);
        return componentDefinition;
    }

    protected JSONArray newArray(Object... values) {
        JSONArray array = new JSONArray();
        stream(values).forEach(array::put);
        return array;
    }

    protected void assertAllItemsOfType(Collection<?> collection, Class<?> expectedType) {
        collection.forEach(item -> assertThat(item).isInstanceOf(expectedType));
    }

    protected void mockComponent(Class<? extends Component> componentClass) {
        Component component = instantiate(componentClass);
        lenient().doReturn(component).when(mockExecutionNode).getComponent();
        lenient().doReturn(mockExecutionNode).when(context).instantiateComponent(componentClass);
    }

    protected void mockImplementor(Class<? extends Implementor> clazz) {
        Implementor implementor = instantiate(clazz);
        lenient().doReturn(implementor).when(context).instantiateImplementor(mockExecutionNode, clazz.getName());
    }

    @SuppressWarnings("unchecked")
    private <T> T instantiate(Class<? extends Implementor> clazz) {
        try {
            return (T) clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }
}
