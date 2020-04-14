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
class DefaultConfigurationServiceStringTest extends BaseDefaultConfigurationServiceTest {

    // String getStringFrom(String configPID, String configKey, String defaultValue);

    @Test
    void shouldGetStringFromWithDefaultReturnDefault() {
        // Given
        String customConfigFile = "test.configuration";
        String defaultValue = "my default value";

        doReturn(defaultValue)
                .when(service)
                .getConfigAdminProperty(eq(customConfigFile), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));

        // When
        String actualConfigProperty = service.getStringFrom(customConfigFile, TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo(defaultValue);
    }

    @Test
    void shouldGetStringFromWithDefaultReturnPropertyValue() {
        // Given
        String customConfigFile = "test.configuration";
        String defaultValue = "my default value";

        doReturn("my property value")
                .when(service)
                .getConfigAdminProperty(eq(customConfigFile), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));

        // When
        String actualConfigProperty = service.getStringFrom(customConfigFile, TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo("my property value");
    }


    // String getStringFrom(String configPID, String configKey) throws ConfigPropertyException;

    @Test
    void shouldThrowExceptionWhenPropertyNotPresentInConfigFile() {
        // Given
        String customConfigFile = "test.configuration";

        doThrow(new ConfigPropertyException("Could not find property"))
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(customConfigFile), eq(TEST_CONFIG_KEY), any(InputMapper.class));
        // When
        ConfigPropertyException thrown =
                assertThrows(ConfigPropertyException.class, () -> service.getStringFrom(customConfigFile, TEST_CONFIG_KEY));

        assertThat(thrown).hasMessage("Could not find property");
    }

    @Test
    void shouldReturnPropertyFromConfigFile() {
        // Given
        String customConfigFile = "test.configuration";

        doReturn("MyValue")
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(customConfigFile), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        String actualConfigProperty = service.getStringFrom(customConfigFile, TEST_CONFIG_KEY);

        // Then
        assertThat(actualConfigProperty).isEqualTo("MyValue");
    }

    // String getString(String configKey, String defaultValue);

    @Test
    void shouldReturnSystemStringConfigProperty() {
        // Given
        String expectedValue = "testValue";

        doReturn(expectedValue)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        // When
        String actualConfigProperty = service.getString(TEST_CONFIG_KEY, "DefaultValue");

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnStringConfigPropertyFromConfigurationAdminService() throws IOException {
        // Given
        String expectedValue = "testValue";

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(TEST_CONFIG_KEY, expectedValue);
        mockConfigurationWithProperties(properties);

        // When
        String actualConfigProperty = service.getString(TEST_CONFIG_KEY, "DefaultValue");

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnDefaultStringConfigPropertyWhenDictionaryIsNull() throws IOException {
        // Given
        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);
        mockConfigurationWithProperties(null);

        // When
        String actualConfigProperty = service.getString(TEST_CONFIG_KEY, "MyDefaultValue");

        // Then
        assertThat(actualConfigProperty).isEqualTo("MyDefaultValue");
    }

    @Test
    void shouldReturnDefaultStringConfigPropertyWhenDictionaryDoesNotContainKey() throws IOException {
        // Given
        Dictionary<String, Object> dictionaryNotContainingTargetKey = new Hashtable<>();
        dictionaryNotContainingTargetKey.put("anotherKey", "aString");

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);
        mockConfigurationWithProperties(dictionaryNotContainingTargetKey);

        // When
        String actualConfigProperty = service.getString(TEST_CONFIG_KEY, "MyDefaultValue");

        // Then
        assertThat(actualConfigProperty).isEqualTo("MyDefaultValue");
    }


    // String getString(String configKey) throws ConfigPropertyException;

    @Test
    void shouldThrowExceptionWhenPropertyNotPresentInDefaultConfigFile() {
        // Given
        doThrow(new ConfigPropertyException("Could not find property"))
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));
        // When
        ConfigPropertyException thrown =
                assertThrows(ConfigPropertyException.class, () -> service.getString(TEST_CONFIG_KEY));

        assertThat(thrown).hasMessage("Could not find property");
    }

    @Test
    void shouldReturnPropertyFromDefaultConfigFile() {
        // Given
        doReturn("MyValue")
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        String actualConfigProperty = service.getString(TEST_CONFIG_KEY);

        // Then
        assertThat(actualConfigProperty).isEqualTo("MyValue");
    }
}
