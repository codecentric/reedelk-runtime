package de.codecentric.reedelk.platform.services.configuration;

import de.codecentric.reedelk.runtime.api.exception.ConfigurationPropertyException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings("unchecked")
class DefaultConfigurationServiceIntegerTest extends BaseDefaultConfigurationServiceTest {

    // int getIntFrom(String configPID, String configKey, int defaultValue);

    @Test
    void shouldGetIntFromWithDefaultReturnDefault() {
        // Given
        String customConfigFile = "test.configuration";
        int defaultValue = 43;

        doReturn(defaultValue)
                .when(service)
                .getConfigAdminProperty(eq(customConfigFile), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(DefaultConfigurationService.InputMapper.class));

        // When
        int actualConfigProperty = service.getIntFrom(customConfigFile, TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo(defaultValue);
    }

    @Test
    void shouldGetIntFromWithDefaultReturnPropertyValue() {
        // Given
        String customConfigFile = "test.configuration";
        int defaultValue = 7643;

        doReturn(67)
                .when(service)
                .getConfigAdminProperty(eq(customConfigFile), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(DefaultConfigurationService.InputMapper.class));

        // When
        int actualConfigProperty = service.getIntFrom(customConfigFile, TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo(67);
    }

    // int getIntFrom(String configPID, String configKey) throws ConfigPropertyException;

    @Test
    void shouldThrowExceptionWhenPropertyNotPresentInConfigFile() {
        // Given
        String customConfigFile = "test.configuration";

        Mockito.doThrow(new ConfigurationPropertyException("Could not find property"))
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(customConfigFile), eq(TEST_CONFIG_KEY), any(DefaultConfigurationService.InputMapper.class));
        // When
        ConfigurationPropertyException thrown =
                assertThrows(ConfigurationPropertyException.class, () -> service.getIntFrom(customConfigFile, TEST_CONFIG_KEY));

        assertThat(thrown).hasMessage("Could not find property");
    }

    @Test
    void shouldReturnPropertyFromConfigFile() {
        // Given
        String customConfigFile = "test.configuration";

        doReturn(88)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(customConfigFile), eq(TEST_CONFIG_KEY), any(DefaultConfigurationService.InputMapper.class));

        // When
        int actualConfigProperty = service.getIntFrom(customConfigFile, TEST_CONFIG_KEY);

        // Then
        assertThat(actualConfigProperty).isEqualTo(88);
    }

    // int getInt(String configKey, int defaultValue);

    @Test
    void shouldReturnSystemIntConfigProperty() {
        // Given
        int expectedValue = 23434;

        doReturn(expectedValue)
                .when(service)
                .getIntSystemProperty(TEST_CONFIG_KEY);

        // When
        int actualConfigProperty = service.getInt(TEST_CONFIG_KEY, 7777);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnIntConfigPropertyFromConfigurationAdminServiceWhenIntAlready() throws IOException {
        // Given
        int expectedValue = 111333;

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(TEST_CONFIG_KEY, expectedValue);
        mockConfigurationWithProperties(properties);

        // When
        int actualConfigProperty = service.getInt(TEST_CONFIG_KEY, 888);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnIntConfigPropertyFromConfigurationAdminServiceWhenString() throws IOException {
        // Given
        int expectedValue = 111333;

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(TEST_CONFIG_KEY, Integer.toString(expectedValue));
        mockConfigurationWithProperties(properties);

        // When
        int actualConfigProperty = service.getInt(TEST_CONFIG_KEY, 11109);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnDefaultIntConfigProperty() throws IOException {
        // Given
        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);
        mockConfigurationWithProperties(null);

        // When
        int actualConfigProperty = service.getInt(TEST_CONFIG_KEY, 653333);

        // Then
        assertThat(actualConfigProperty).isEqualTo(653333);
    }

    // int getInt(String configKey) throws ConfigPropertyException;

    @Test
    void shouldThrowExceptionWhenPropertyNotPresentInDefaultConfigFile() {
        // Given
        doThrow(new ConfigurationPropertyException("Could not find property"))
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(DefaultConfigurationService.InputMapper.class));
        // When
        ConfigurationPropertyException thrown =
                assertThrows(ConfigurationPropertyException.class, () -> service.getInt(TEST_CONFIG_KEY));

        assertThat(thrown).hasMessage("Could not find property");
    }

    @Test
    void shouldReturnPropertyFromDefaultConfigFile() {
        // Given
        doReturn(35000)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(DefaultConfigurationService.InputMapper.class));

        // When
        int actualConfigProperty = service.getInt(TEST_CONFIG_KEY);

        // Then
        assertThat(actualConfigProperty).isEqualTo(35000);
    }
}
