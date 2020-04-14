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

class DefaultConfigurationServiceDoubleTest extends BaseDefaultConfigurationServiceTest {

    // double getDoubleFrom(String configPID, String configKey, double defaultValue);

    @Test
    void shouldGetDoubleFromWithDefaultReturnDefault() {
        // Given
        String customConfigFile = "test.configuration";
        double defaultValue = 43.23d;

        doReturn(defaultValue)
                .when(service)
                .getConfigAdminProperty(eq(customConfigFile), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));

        // When
        double actualConfigProperty = service.getDoubleFrom(customConfigFile, TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo(defaultValue);
    }

    @Test
    void shouldGetDoubleFromWithDefaultReturnPropertyValue() {
        // Given
        String customConfigFile = "test.configuration";
        double defaultValue = 5822.234d;

        doReturn(23.3d)
                .when(service)
                .getConfigAdminProperty(eq(customConfigFile), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));

        // When
        double actualConfigProperty = service.getDoubleFrom(customConfigFile, TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo(23.3d);
    }

    // double getDoubleFrom(String configPID, String configKey) throws ConfigPropertyException;

    @Test
    void shouldThrowExceptionWhenPropertyNotPresentInConfigFile() {
        // Given
        String customConfigFile = "test.configuration";

        doThrow(new ConfigPropertyException("Could not find property"))
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(customConfigFile), eq(TEST_CONFIG_KEY), any(InputMapper.class));
        // When
        ConfigPropertyException thrown =
                assertThrows(ConfigPropertyException.class, () -> service.getDoubleFrom(customConfigFile, TEST_CONFIG_KEY));

        assertThat(thrown).hasMessage("Could not find property");
    }

    @Test
    void shouldReturnPropertyFromConfigFile() {
        // Given
        String customConfigFile = "test.configuration";

        doReturn(88.23d)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(customConfigFile), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        double actualConfigProperty = service.getDoubleFrom(customConfigFile, TEST_CONFIG_KEY);

        // Then
        assertThat(actualConfigProperty).isEqualTo(88.23d);
    }

    // double getDouble(String configKey, double defaultValue);

    @Test
    void shouldReturnSystemDoubleConfigProperty() {
        // Given
        double expectedValue = 1223.245d;

        doReturn(expectedValue)
                .when(service)
                .getDoubleSystemProperty(TEST_CONFIG_KEY);

        // When
        double actualConfigProperty = service.getDouble(TEST_CONFIG_KEY, 7777.23d);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnDoubleConfigPropertyFromConfigurationAdminServiceWhenDoubleAlready() throws IOException {
        // Given
        double expectedValue = 111333.1d;

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(TEST_CONFIG_KEY, expectedValue);
        mockConfigurationWithProperties(properties);

        // When
        double actualConfigProperty = service.getDouble(TEST_CONFIG_KEY, 888.2d);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnDoubleConfigPropertyFromConfigurationAdminServiceWhenString() throws IOException {
        // Given
        double expectedValue = 111333.43d;

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(TEST_CONFIG_KEY, Double.toString(expectedValue));
        mockConfigurationWithProperties(properties);

        // When
        double actualConfigProperty = service.getDouble(TEST_CONFIG_KEY, 11109.1d);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnDefaultDoubleConfigProperty() throws IOException {
        // Given
        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);
        mockConfigurationWithProperties(null);

        // When
        double actualConfigProperty = service.getDouble(TEST_CONFIG_KEY, 653333.3d);

        // Then
        assertThat(actualConfigProperty).isEqualTo(653333.3d);
    }

    // double getDouble(String configKey) throws ConfigPropertyException;

    @Test
    void shouldThrowExceptionWhenPropertyNotPresentInDefaultConfigFile() {
        // Given
        doThrow(new ConfigPropertyException("Could not find property"))
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        ConfigPropertyException thrown =
                assertThrows(ConfigPropertyException.class, () -> service.getDouble(TEST_CONFIG_KEY));

        assertThat(thrown).hasMessage("Could not find property");
    }

    @Test
    void shouldReturnPropertyFromDefaultConfigFile() {
        // Given
        doReturn(35000.1d)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        double actualConfigProperty = service.getDouble(TEST_CONFIG_KEY);

        // Then
        assertThat(actualConfigProperty).isEqualTo(35000.1d);
    }

    // double get(String configKey, Class<T> type)

    @Test
    void shouldReturnDoubleFromGenericConfigProviderAndDefaultConfigFile() {
        // Given
        double expectedValue = 35000.1d;

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        double actualConfigProperty = service.get(TEST_CONFIG_KEY, double.class);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    // double get(String configKey, T defaultValue, Class<T> type)

    @Test
    void shouldReturnDoubleDefaultValueFromGenericConfigProviderAndDefaultConfigFile() {
        // Given
        double defaultValue = 35000.14323d;

        doReturn(defaultValue)
                .when(service)
                .getConfigAdminProperty(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));

        // When
        double actualConfigProperty = service.get(TEST_CONFIG_KEY, defaultValue, double.class);

        // Then
        assertThat(actualConfigProperty).isEqualTo(defaultValue);
    }
}
