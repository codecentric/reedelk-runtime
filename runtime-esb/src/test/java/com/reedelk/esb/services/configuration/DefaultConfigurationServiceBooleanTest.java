package com.reedelk.esb.services.configuration;

import com.reedelk.runtime.api.exception.ConfigPropertyException;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import static com.reedelk.esb.services.configuration.DefaultConfigurationService.InputMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings("unchecked")
class DefaultConfigurationServiceBooleanTest extends BaseDefaultConfigurationServiceTest {

    // boolean getBooleanFrom(String configPID, String configKey, boolean defaultValue);

    @Test
    void shouldGetBooleanFromWithDefaultReturnDefault() {
        // Given
        String customConfigFile = "test.configuration";
        boolean defaultValue = true;

        doReturn(defaultValue)
                .when(service)
                .getConfigAdminProperty(eq(customConfigFile), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));

        // When
        boolean actualConfigProperty = service.getBooleanFrom(customConfigFile, TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo(defaultValue);
    }

    @Test
    void shouldGetBooleanFromWithDefaultReturnPropertyValue() {
        // Given
        String customConfigFile = "test.configuration";
        boolean defaultValue = true;

        doReturn(false)
                .when(service)
                .getConfigAdminProperty(eq(customConfigFile), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));

        // When
        boolean actualConfigProperty = service.getBooleanFrom(customConfigFile, TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isFalse();
    }

    // boolean getBooleanFrom(String configPID, String configKey) throws ConfigPropertyException;

    @Test
    void shouldThrowExceptionWhenPropertyNotPresentInConfigFile() {
        // Given
        String customConfigFile = "test.configuration";

        doThrow(new ConfigPropertyException("Could not find property"))
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(customConfigFile), eq(TEST_CONFIG_KEY), any(InputMapper.class));
        // When
        ConfigPropertyException thrown =
                assertThrows(ConfigPropertyException.class, () -> service.getBooleanFrom(customConfigFile, TEST_CONFIG_KEY));

        assertThat(thrown).hasMessage("Could not find property");
    }

    @Test
    void shouldReturnPropertyFromConfigFile() {
        // Given
        String customConfigFile = "test.configuration";

        doReturn(true)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(customConfigFile), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        boolean actualConfigProperty = service.getBooleanFrom(customConfigFile, TEST_CONFIG_KEY);

        // Then
        assertThat(actualConfigProperty).isTrue();
    }

    // boolean getBoolean(String configKey, boolean defaultValue);

    @Test
    void shouldReturnSystemBooleanConfigProperty() {
        // Given
        boolean expectedValue = true;

        doReturn(expectedValue)
                .when(service)
                .getBooleanSystemProperty(TEST_CONFIG_KEY);

        // When
        boolean actualConfigProperty = service.getBoolean(TEST_CONFIG_KEY, false);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnBooleanConfigPropertyFromConfigurationAdminServiceWhenBooleanAlready() throws IOException {
        // Given
        boolean expectedValue = true;

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(TEST_CONFIG_KEY, expectedValue);
        mockConfigurationWithProperties(properties);

        // When
        boolean actualConfigProperty = service.getBoolean(TEST_CONFIG_KEY, false);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnBooleanConfigPropertyFromConfigurationAdminServiceWhenString() throws IOException {
        // Given
        boolean expectedValue = true;

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(TEST_CONFIG_KEY, Boolean.toString(expectedValue));
        mockConfigurationWithProperties(properties);

        // When
        boolean actualConfigProperty = service.getBoolean(TEST_CONFIG_KEY, false);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnDefaultBooleanConfigProperty() throws IOException {
        // Given
        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);
        mockConfigurationWithProperties(null);

        // When
        boolean actualConfigProperty = service.getBoolean(TEST_CONFIG_KEY, true);

        // Then
        assertThat(actualConfigProperty).isEqualTo(true);
    }

    // boolean getBoolean(String configKey) throws ConfigPropertyException;

    @Test
    void shouldThrowExceptionWhenPropertyNotPresentInDefaultConfigFile() {
        // Given
        doThrow(new ConfigPropertyException("Could not find property"))
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));
        // When
        ConfigPropertyException thrown =
                assertThrows(ConfigPropertyException.class, () -> service.getBoolean(TEST_CONFIG_KEY));

        assertThat(thrown).hasMessage("Could not find property");
    }

    @Test
    void shouldReturnPropertyFromDefaultConfigFile() {
        // Given
        doReturn(true)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        boolean actualConfigProperty = service.getBoolean(TEST_CONFIG_KEY);

        // Then
        assertThat(actualConfigProperty).isTrue();
    }
}
