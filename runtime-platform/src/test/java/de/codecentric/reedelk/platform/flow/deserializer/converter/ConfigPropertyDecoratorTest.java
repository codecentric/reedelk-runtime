package de.codecentric.reedelk.platform.flow.deserializer.converter;

import de.codecentric.reedelk.runtime.api.configuration.ConfigurationService;
import de.codecentric.reedelk.runtime.converter.DeserializerConverter;
import de.codecentric.reedelk.runtime.converter.DeserializerConverterContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfigPropertyDecoratorTest {

    @Mock
    private ConfigurationService configurationService;

    private final long testModuleId = 10L;
    private ConfigPropertyDecorator decorator;
    private DeserializerConverterContext deserializerConverterContext = new DeserializerConverterContext(testModuleId);

    @BeforeEach
    void setUp() {
        decorator = new ConfigPropertyDecorator(configurationService, DeserializerConverter.getInstance());
    }

    @Test
    void shouldDelegateConfigurationServiceWhenPropertyIsConfigProperty() {
        // Given
        String propertyName = "myProperty";
        int expectedValue = 54;

        doReturn(expectedValue)
                .when(configurationService)
                .get("listener.port", int.class);
        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, "${listener.port}");

        // When
        Object typeInstance = decorator.convert(int.class, componentDefinition, propertyName, deserializerConverterContext);

        // Then
        assertThat(typeInstance).isEqualTo(expectedValue);
        verify(configurationService).get("listener.port", int.class);
        verifyNoMoreInteractions(configurationService);
    }

    @Test
    void shouldReturnPropertyValueWhenItIsNotConfigProperty() {
        // Given
        String configKey = "myProperty";
        int expectedValue = 54;

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(configKey, expectedValue);

        // When
        Object typeInstance = decorator.convert(int.class, componentDefinition, configKey, deserializerConverterContext);

        // Then
        assertThat(typeInstance).isEqualTo(expectedValue);
        verifyNoMoreInteractions(configurationService);
    }

    @Test
    void shouldReturnArrayPropertyWhenValueItIsNotConfigProperty() {
        // Given
        int expectedValue = 54;

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(42);
        jsonArray.put("${array.config.property.name}");
        jsonArray.put(100);

        doReturn(expectedValue)
                .when(configurationService)
                .get("array.config.property.name", int.class);

        // When
        Integer instance = decorator.convert(int.class, jsonArray, 1, deserializerConverterContext);

        // Then
        assertThat(instance).isEqualTo(expectedValue);
    }

    @Test
    void shouldUseGivenDefaultConfigPropertyValue() {
        // Given
        int expectedValue = 3212;
        String propertyName = "myProperty";

        doReturn(expectedValue)
                .when(configurationService)
                .get("listener.port", expectedValue, int.class);
        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, "${listener.port:3212}");

        // When
        Object typeInstance = decorator.convert(int.class, componentDefinition, propertyName, deserializerConverterContext);

        // Then
        assertThat(typeInstance).isEqualTo(expectedValue);
        verify(configurationService).get("listener.port", expectedValue, int.class);
        verifyNoMoreInteractions(configurationService);
    }
}
