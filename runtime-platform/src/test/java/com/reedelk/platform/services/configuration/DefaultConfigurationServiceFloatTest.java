package com.reedelk.platform.services.configuration;

import com.reedelk.runtime.api.exception.ConfigPropertyException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import static com.reedelk.platform.services.configuration.DefaultConfigurationService.InputMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

class DefaultConfigurationServiceFloatTest extends BaseDefaultConfigurationServiceTest {

    // float getFloatFrom(String configPID, String configKey, float defaultValue);

    @Test
    void shouldGetFloatFromWithDefaultReturnDefault() {
        // Given
        String customConfigFile = "test.configuration";
        float defaultValue = 43.23f;

        doReturn(defaultValue)
                .when(service)
                .getConfigAdminProperty(eq(customConfigFile), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));

        // When
        float actualConfigProperty = service.getFloatFrom(customConfigFile, TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo(defaultValue);
    }

    @Test
    void shouldGetFloatFromWithDefaultReturnPropertyValue() {
        // Given
        String customConfigFile = "test.configuration";
        float defaultValue = 5822.234f;

        doReturn(23.3f)
                .when(service)
                .getConfigAdminProperty(eq(customConfigFile), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));

        // When
        float actualConfigProperty = service.getFloatFrom(customConfigFile, TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo(23.3f);
    }

    // float getFloatFrom(String configPID, String configKey) throws ConfigPropertyException;

    @Test
    void shouldThrowExceptionWhenPropertyNotPresentInConfigFile() {
        // Given
        String customConfigFile = "test.configuration";

        doThrow(new ConfigPropertyException("Could not find property"))
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(customConfigFile), eq(TEST_CONFIG_KEY), any(InputMapper.class));
        // When
        ConfigPropertyException thrown =
                assertThrows(ConfigPropertyException.class, () -> service.getFloatFrom(customConfigFile, TEST_CONFIG_KEY));

        assertThat(thrown).hasMessage("Could not find property");
    }

    @Test
    void shouldReturnPropertyFromConfigFile() {
        // Given
        String customConfigFile = "test.configuration";

        doReturn(88.23f)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(customConfigFile), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        float actualConfigProperty = service.getFloatFrom(customConfigFile, TEST_CONFIG_KEY);

        // Then
        assertThat(actualConfigProperty).isEqualTo(88.23f);
    }

    // float getFloat(String configKey, float defaultValue);

    @Test
    void shouldReturnSystemFloatConfigProperty() {
        // Given
        float expectedValue = 1223.245f;

        doReturn(expectedValue)
                .when(service)
                .getFloatSystemProperty(TEST_CONFIG_KEY);

        // When
        float actualConfigProperty = service.getFloat(TEST_CONFIG_KEY, 7777.23f);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnFloatConfigPropertyFromConfigurationAdminServiceWhenFloatAlready() throws IOException {
        // Given
        float expectedValue = 111333.1f;

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(TEST_CONFIG_KEY, expectedValue);
        mockConfigurationWithProperties(properties);

        // When
        float actualConfigProperty = service.getFloat(TEST_CONFIG_KEY, 888.2f);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnFloatConfigPropertyFromConfigurationAdminServiceWhenString() throws IOException {
        // Given
        float expectedValue = 111333.43f;

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(TEST_CONFIG_KEY, Float.toString(expectedValue));
        mockConfigurationWithProperties(properties);

        // When
        float actualConfigProperty = service.getFloat(TEST_CONFIG_KEY, 11109.1f);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnDefaultFloatConfigProperty() throws IOException {
        // Given
        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);
        mockConfigurationWithProperties(null);

        // When
        float actualConfigProperty = service.getFloat(TEST_CONFIG_KEY, 653333.3f);

        // Then
        assertThat(actualConfigProperty).isEqualTo(653333.3f);
    }

    // float getFloat(String configKey) throws ConfigPropertyException;

    @Test
    void shouldThrowExceptionWhenPropertyNotPresentInDefaultConfigFile() {
        // Given
        doThrow(new ConfigPropertyException("Could not find property"))
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        ConfigPropertyException thrown =
                assertThrows(ConfigPropertyException.class, () -> service.getFloat(TEST_CONFIG_KEY));

        assertThat(thrown).hasMessage("Could not find property");
    }

    @Test
    void shouldReturnPropertyFromDefaultConfigFile() {
        // Given
        doReturn(35000.1f)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        float actualConfigProperty = service.getFloat(TEST_CONFIG_KEY);

        // Then
        assertThat(actualConfigProperty).isEqualTo(35000.1f);
    }

    // float get(String configKey, Class<T> type)

    @Test
    void shouldReturnFloatFromGenericConfigProviderAndDefaultConfigFile() {
        // Given
        float expectedValue = 35000.1f;

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        float actualConfigProperty = service.get(TEST_CONFIG_KEY, float.class);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    // float get(String configKey, T defaultValue, Class<T> type)

    @Test
    void shouldReturnFloatDefaultValueFromGenericConfigProviderAndDefaultConfigFile() {
        // Given
        float defaultValue = 35000.14323f;

        doReturn(defaultValue)
                .when(service)
                .getConfigAdminProperty(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));

        // When
        float actualConfigProperty = service.get(TEST_CONFIG_KEY, defaultValue, float.class);

        // Then
        assertThat(actualConfigProperty).isEqualTo(defaultValue);
    }
}
