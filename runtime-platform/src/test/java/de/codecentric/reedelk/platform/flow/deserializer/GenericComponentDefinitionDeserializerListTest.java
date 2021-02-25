package de.codecentric.reedelk.platform.flow.deserializer;

import de.codecentric.reedelk.platform.test.utils.ComponentsBuilder;
import de.codecentric.reedelk.platform.test.utils.TestComponentWithListProperty;
import de.codecentric.reedelk.platform.test.utils.TestImplementor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericComponentDefinitionDeserializerListTest extends AbstractGenericComponentDefinitionDeserializerTest {

    @Test
    void shouldCorrectlyDeserializeJSONArrayToListWithStringValues() {
        // Given
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("one");
        jsonArray.put("two");
        jsonArray.put("three");

        JSONObject componentDefinition = ComponentsBuilder.forComponent(TestComponentWithListProperty.class)
                .with("listStringProperty", jsonArray)
                .build();

        mockComponent(TestComponentWithListProperty.class);

        // When
        deserializer.deserialize(componentDefinition, mockExecutionNode.getComponent());

        // Then
        TestComponentWithListProperty component =
                (TestComponentWithListProperty) mockExecutionNode.getComponent();
        List<String> listOfStrings = component.getListStringProperty();
        assertThat(listOfStrings).isNotNull();
        assertThat(listOfStrings).hasSize(3);
        assertThat(listOfStrings).containsExactlyInAnyOrder("one", "two", "three");
    }

    @Test
    void shouldCorrectlyDeserializeJSONArrayToCustomImplementorProperty() {
        // Given
        JSONObject implementorA = new JSONObject();
        implementorA.put("property1", "Property 1 a");
        implementorA.put("property2", 19932);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(implementorA);

        JSONObject componentDefinition = ComponentsBuilder.forComponent(TestComponentWithListProperty.class)
                .with("listCustomImplementorProperty", jsonArray)
                .build();

        mockComponent(TestComponentWithListProperty.class);
        mockImplementor(TestImplementor.class);

        // When
        deserializer.deserialize(componentDefinition, mockExecutionNode.getComponent());

        // Then
        TestComponentWithListProperty component = (TestComponentWithListProperty) mockExecutionNode.getComponent();
        List<TestImplementor> listOfImplementors = component.getListCustomImplementorProperty();
        assertThat(listOfImplementors).isNotNull();
        assertThat(listOfImplementors).hasSize(1);

        TestImplementor testImplementor1 = listOfImplementors.get(0);

        assertThat(testImplementor1.getProperty1()).isEqualTo("Property 1 a");
        assertThat(testImplementor1.getProperty2()).isEqualTo(new BigDecimal("19932"));
    }
}
