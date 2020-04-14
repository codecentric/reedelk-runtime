package com.reedelk.platform.services.configuration;

import com.reedelk.runtime.api.exception.ConfigPropertyException;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

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

@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings("unchecked")
class DefaultConfigurationServiceLongTest extends BaseDefaultConfigurationServiceTest {

    // long getLongFrom(String configPID, String configKey, long defaultValue);

    @Test
    void shouldGetLongFromWithDefaultReturnDefault() {
        // Given
        String customConfigFile = "test.configuration";
        long defaultValue = 43L;

        doReturn(defaultValue)
                .when(service)
                .getConfigAdminProperty(eq(customConfigFile), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));

        // When
        long actualConfigProperty = service.getLongFrom(customConfigFile, TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo(defaultValue);
    }

    @Test
    void shouldGetLongFromWithDefaultReturnPropertyValue() {
        // Given
        String customConfigFile = "test.configuration";
        long defaultValue = 7643L;

        doReturn(67L)
                .when(service)
                .getConfigAdminProperty(eq(customConfigFile), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));

        // When
        long actualConfigProperty = service.getLongFrom(customConfigFile, TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo(67L);
    }

    // long getLongFrom(String configPID, String configKey) throws ConfigPropertyException;

    @Test
    void shouldThrowExceptionWhenPropertyNotPresentInConfigFile() {
        // Given
        String customConfigFile = "test.configuration";

        doThrow(new ConfigPropertyException("Could not find property"))
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(customConfigFile), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        ConfigPropertyException thrown =
                assertThrows(ConfigPropertyException.class, () -> service.getLongFrom(customConfigFile, TEST_CONFIG_KEY));

        assertThat(thrown).hasMessage("Could not find property");
    }

    @Test
    void shouldReturnPropertyFromConfigFile() {
        // Given
        String customConfigFile = "test.configuration";

        doReturn(88L)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(customConfigFile), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        long actualConfigProperty = service.getLongFrom(customConfigFile, TEST_CONFIG_KEY);

        // Then
        assertThat(actualConfigProperty).isEqualTo(88L);
    }

    // long getLong(String configKey, long defaultValue);

    @Test
    void shouldReturnSystemLongConfigProperty() {
        // Given
        long expectedValue = 54L;

        doReturn(expectedValue)
                .when(service)
                .getLongSystemProperty(TEST_CONFIG_KEY);

        // When
        long actualConfigProperty = service.getLong(TEST_CONFIG_KEY, 11L);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnLongConfigPropertyFromConfigurationAdminServiceWhenLongAlready() throws IOException {
        // Given
        long expectedValue = 658L;

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(TEST_CONFIG_KEY, expectedValue);
        mockConfigurationWithProperties(properties);

        // When
        long actualConfigProperty = service.getLong(TEST_CONFIG_KEY, 88L);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnLongConfigPropertyFromConfigurationAdminServiceWhenString() throws IOException {
        // Given
        long expectedValue = Long.MAX_VALUE;

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(TEST_CONFIG_KEY, Long.toString(expectedValue));
        mockConfigurationWithProperties(properties);

        // When
        long actualConfigProperty = service.getLong(TEST_CONFIG_KEY, 11109L);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnDefaultLongConfigProperty() throws IOException {
        // Given
        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);
        mockConfigurationWithProperties(null);

        // When
        long actualConfigProperty = service.getLong(TEST_CONFIG_KEY, Long.MIN_VALUE);

        // Then
        assertThat(actualConfigProperty).isEqualTo(Long.MIN_VALUE);
    }

    // long getLong(String configKey) throws ConfigPropertyException;

    @Test
    void shouldThrowExceptionWhenPropertyNotPresentInDefaultConfigFile() {
        // Given
        doThrow(new ConfigPropertyException("Could not find property"))
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));
        // When
        ConfigPropertyException thrown =
                assertThrows(ConfigPropertyException.class, () -> service.getLong(TEST_CONFIG_KEY));

        assertThat(thrown).hasMessage("Could not find property");
    }

    @Test
    void shouldReturnPropertyFromDefaultConfigFile() {
        // Given
        doReturn(35000L)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        Long actualConfigProperty = service.getLong(TEST_CONFIG_KEY);

        // Then
        assertThat(actualConfigProperty).isEqualTo(35000L);
    }
}
