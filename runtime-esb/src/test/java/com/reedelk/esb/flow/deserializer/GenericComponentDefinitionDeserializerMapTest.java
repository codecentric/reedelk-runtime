package com.reedelk.esb.flow.deserializer;

import com.reedelk.esb.test.utils.ComponentsBuilder;
import com.reedelk.esb.test.utils.TestComponentWithMapProperty;
import com.reedelk.esb.test.utils.TestImplementor;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericComponentDefinitionDeserializerMapTest extends AbstractGenericComponentDefinitionDeserializerTest {

    @Test
    void shouldCorrectlyMapJSONObjectToMapWithStringValues() {
        // Given
        JSONObject nestedObject = new JSONObject();
        nestedObject.put("property1", "property 1");
        nestedObject.put("property2", "property 2");
        nestedObject.put("property3", "property 3");
        JSONObject componentDefinition = ComponentsBuilder.forComponent(TestComponentWithMapProperty.class)
                .with("mapStringStringProperty", nestedObject)
                .build();

        mockComponent(TestComponentWithMapProperty.class);

        // When
        deserializer.deserialize(componentDefinition, mockExecutionNode.getComponent());

        // Then
        TestComponentWithMapProperty component = (TestComponentWithMapProperty) mockExecutionNode.getComponent();
        Map<String, String> mappedMap = component.getMapStringStringProperty();
        assertThat(mappedMap).isNotNull();
        assertThat(mappedMap).hasSize(3);
        assertThat(mappedMap.get("property1")).isEqualTo("property 1");
        assertThat(mappedMap.get("property2")).isEqualTo("property 2");
        assertThat(mappedMap.get("property3")).isEqualTo("property 3");
    }

    @Test
    void shouldCorrectlyMapJSONObjectToCustomImplementorProperty() {
        // Given
        JSONObject implementorA = new JSONObject();
        implementorA.put("property1", "Property 1 a");
        implementorA.put("property2", 19932);

        JSONObject nestedObject = new JSONObject();
        nestedObject.put("keyA", implementorA);

        JSONObject componentDefinition = ComponentsBuilder.forComponent(TestComponentWithMapProperty.class)
                .with("mapStringCustomImplementorProperty", nestedObject)
                .build();

        mockComponent(TestComponentWithMapProperty.class);
        mockImplementor(TestImplementor.class);

        // When
        deserializer.deserialize(componentDefinition, mockExecutionNode.getComponent());

        // Then
        TestComponentWithMapProperty component = (TestComponentWithMapProperty) mockExecutionNode.getComponent();
        Map<String, TestImplementor> mappedMap = component.getMapStringCustomImplementorProperty();
        assertThat(mappedMap).isNotNull();
        assertThat(mappedMap).hasSize(1);
        assertThat(mappedMap).containsKeys("keyA");

        TestImplementor testImplementorA = mappedMap.get("keyA");
        assertThat(testImplementorA.getProperty1()).isEqualTo("Property 1 a");
        assertThat(testImplementorA.getProperty2()).isEqualTo(new BigDecimal("19932"));
    }
}
