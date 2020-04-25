package com.reedelk.platform.flow.deserializer.converter;

import com.reedelk.runtime.converter.DeserializerConverter;
import com.reedelk.runtime.converter.DeserializerConverterContext;
import com.reedelk.runtime.system.api.SystemProperty;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SystemConfigPropertyReplacerDecoratorTest {

    @Mock
    private SystemProperty systemPropertyService;

    private final long testModuleId = 10L;

    private DeserializerConverter decorator;
    private DeserializerConverterContext deserializerConverterContext = new DeserializerConverterContext(testModuleId);

    @BeforeEach
    void setUp() {
        decorator = new SystemConfigPropertyReplacerDecorator(systemPropertyService, DeserializerConverter.getInstance());
    }

    @Test
    void shouldReturnIntPropertyValueWhenItIsNotRuntimeConfigProperty() {
        // Given
        String configKey = "myProperty";
        int expectedValue = 54;

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(configKey, expectedValue);

        // When
        Object typeInstance = decorator.convert(int.class, componentDefinition, configKey, deserializerConverterContext);

        // Then
        assertThat(typeInstance).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnStringPropertyValueWhenItIsNotRuntimeConfigProperty() {
        // Given
        String configKey = "myProperty";
        String expectedValue = "${my.config.value}";

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(configKey, expectedValue);

        // When
        Object typeInstance = decorator.convert(String.class, componentDefinition, configKey, deserializerConverterContext);

        // Then
        assertThat(typeInstance).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnNullWhenPropertyValueIsNull() {
        // Given
        String configKey = "myProperty";
        Object expectedValue = JSONObject.NULL;

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(configKey, expectedValue);

        // When
        Object typeInstance = decorator.convert(String.class, componentDefinition, configKey, deserializerConverterContext);

        // Then
        assertThat(typeInstance).isNull();
    }

    @Test
    void shouldReplaceRuntimeConfigPropertyCorrectly() {
        // Given
        String propertyName = "myProperty";
        String expectedValue = "/var/runtime/config";

        doReturn(expectedValue)
                .when(systemPropertyService)
                .configDirectory();

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, "${RUNTIME_CONFIG}/my-certificate.key");

        // When
        Object typeInstance = decorator.convert(String.class, componentDefinition, propertyName, deserializerConverterContext);

        // Then
        assertThat(typeInstance).isEqualTo(expectedValue + "/my-certificate.key");
    }

    @Test
    void shouldReplaceRuntimeHomePropertyCorrectly() {
        // Given
        String propertyName = "myProperty";
        String expectedValue = "/var/runtime";

        doReturn(expectedValue)
                .when(systemPropertyService)
                .homeDirectory();

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, "${RUNTIME_HOME}/subdirectory");

        // When
        Object typeInstance = decorator.convert(String.class, componentDefinition, propertyName, deserializerConverterContext);

        // Then
        assertThat(typeInstance).isEqualTo(expectedValue + "/subdirectory");
    }

    @Test
    void shouldReplaceRuntimeModulesPropertyCorrectly() {
        // Given
        String propertyName = "myProperty";
        String expectedValue = "/var/runtime/modules";

        doReturn(expectedValue)
                .when(systemPropertyService)
                .modulesDirectory();

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, "${RUNTIME_MODULES}/subdirectory");

        // When
        Object typeInstance = decorator.convert(String.class, componentDefinition, propertyName, deserializerConverterContext);

        // Then
        assertThat(typeInstance).isEqualTo(expectedValue + "/subdirectory");
    }

    @Test
    void shouldReplaceRuntimeVersionPropertyCorrectly() {
        // Given
        String propertyName = "myProperty";
        String expectedValue = "1.0.0";

        doReturn(expectedValue)
                .when(systemPropertyService)
                .version();

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, "${RUNTIME_VERSION}");

        // When
        Object typeInstance = decorator.convert(String.class, componentDefinition, propertyName, deserializerConverterContext);

        // Then
        assertThat(typeInstance).isEqualTo(expectedValue);
    }
}
