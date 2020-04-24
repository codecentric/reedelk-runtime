package com.reedelk.platform.services.configuration;

import com.reedelk.runtime.api.exception.ConfigurationPropertyException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Dictionary;
import java.util.Hashtable;

import static com.reedelk.platform.services.configuration.DefaultConfigurationService.InputMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

class DefaultConfigurationServiceBigIntegerTest extends BaseDefaultConfigurationServiceTest {

    // BigInteger getBigIntegerFrom(String configPID, String configKey, BigInteger defaultValue);

    @Test
    void shouldGetBigIntegerFromWithDefaultReturnDefault() {
        // Given
        String customConfigFile = "test.configuration";
        BigInteger defaultValue = new BigInteger("294032");

        doReturn(defaultValue)
                .when(service)
                .getConfigAdminProperty(eq(customConfigFile), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));

        // When
        BigInteger actualConfigProperty = service.getBigIntegerFrom(customConfigFile, TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo(defaultValue);
    }

    @Test
    void shouldGetBigIntegerFromWithDefaultReturnPropertyValue() {
        // Given
        String customConfigFile = "test.configuration";
        BigInteger defaultValue = new BigInteger("5822234");
        BigInteger expectedValue = new BigInteger("233");

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminProperty(eq(customConfigFile), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));

        // When
        BigInteger actualConfigProperty = service.getBigIntegerFrom(customConfigFile, TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    // BigInteger getBigIntegerFrom(String configPID, String configKey) throws ConfigPropertyException;

    @Test
    void shouldThrowExceptionWhenPropertyNotPresentInConfigFile() {
        // Given
        String customConfigFile = "test.configuration";

        doThrow(new ConfigurationPropertyException("Could not find property"))
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(customConfigFile), eq(TEST_CONFIG_KEY), any(InputMapper.class));
        // When
        ConfigurationPropertyException thrown =
                assertThrows(ConfigurationPropertyException.class, () -> service.getBigIntegerFrom(customConfigFile, TEST_CONFIG_KEY));

        assertThat(thrown).hasMessage("Could not find property");
    }

    @Test
    void shouldReturnPropertyFromConfigFile() {
        // Given
        String customConfigFile = "test.configuration";
        BigInteger expectedValue = new BigInteger("23493");

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(customConfigFile), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        BigInteger actualConfigProperty = service.getBigIntegerFrom(customConfigFile, TEST_CONFIG_KEY);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    // BigInteger getBigInteger(String configKey, BigInteger defaultValue);

    @Test
    void shouldReturnSystemBigIntegerConfigProperty() {
        // Given
        BigInteger expectedValue = new BigInteger("1223245");

        doReturn(expectedValue)
                .when(service)
                .getBigIntegerSystemProperty(TEST_CONFIG_KEY);

        // When
        BigInteger actualConfigProperty = service.getBigInteger(TEST_CONFIG_KEY, new BigInteger("7777"));

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnBigIntegerConfigPropertyFromConfigurationAdminServiceWhenBigIntegerAlready() throws IOException {
        // Given
        BigInteger expectedValue = new BigInteger("111333");

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(TEST_CONFIG_KEY, expectedValue);
        mockConfigurationWithProperties(properties);

        // When
        BigInteger actualConfigProperty = service.getBigInteger(TEST_CONFIG_KEY, new BigInteger("888"));

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnBigIntegerConfigPropertyFromConfigurationAdminServiceWhenString() throws IOException {
        // Given
        BigInteger expectedValue = new BigInteger("111333");

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(TEST_CONFIG_KEY, expectedValue.toString());
        mockConfigurationWithProperties(properties);

        // When
        BigInteger actualConfigProperty = service.getBigInteger(TEST_CONFIG_KEY, new BigInteger("11109"));

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnDefaultBigIntegerConfigProperty() throws IOException {
        // Given
        BigInteger defaultValue = new BigInteger("653333");

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);
        mockConfigurationWithProperties(null);

        // When
        BigInteger actualConfigProperty = service.getBigInteger(TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo(defaultValue);
    }

    // BigInteger getBigInteger(String configKey) throws ConfigPropertyException;

    @Test
    void shouldThrowExceptionWhenPropertyNotPresentInDefaultConfigFile() {
        // Given
        doThrow(new ConfigurationPropertyException("Could not find property"))
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        ConfigurationPropertyException thrown =
                assertThrows(ConfigurationPropertyException.class, () -> service.getBigInteger(TEST_CONFIG_KEY));

        assertThat(thrown).hasMessage("Could not find property");
    }

    @Test
    void shouldReturnPropertyFromDefaultConfigFile() {
        // Given
        BigInteger expectedValue = new BigInteger("35000");

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        BigInteger actualConfigProperty = service.getBigInteger(TEST_CONFIG_KEY);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    // BigInteger get(String configKey, Class<T> type)

    @Test
    void shouldReturnBigIntegerFromGenericConfigProviderAndDefaultConfigFile() {
        // Given
        BigInteger expectedValue = new BigInteger("35000");

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        BigInteger actualConfigProperty = service.get(TEST_CONFIG_KEY, BigInteger.class);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    // BigInteger get(String configKey, T defaultValue, Class<T> type)

    @Test
    void shouldReturnBigIntegerDefaultValueFromGenericConfigProviderAndDefaultConfigFile() {
        // Given
        BigInteger defaultValue = new BigInteger("12300");

        doReturn(defaultValue)
                .when(service)
                .getConfigAdminProperty(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));


        // When
        BigInteger actualConfigProperty = service.get(TEST_CONFIG_KEY, defaultValue, BigInteger.class);

        // Then
        assertThat(actualConfigProperty).isEqualTo(defaultValue);
    }
}
