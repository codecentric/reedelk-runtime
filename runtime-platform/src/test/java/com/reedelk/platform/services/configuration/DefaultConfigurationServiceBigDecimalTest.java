package com.reedelk.platform.services.configuration;

import com.reedelk.runtime.api.exception.ConfigPropertyException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.Hashtable;

import static com.reedelk.platform.services.configuration.DefaultConfigurationService.InputMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

class DefaultConfigurationServiceBigDecimalTest extends BaseDefaultConfigurationServiceTest {

    // BigDecimal getBigDecimalFrom(String configPID, String configKey, BigDecimal defaultValue);

    @Test
    void shouldGetBigDecimalFromWithDefaultReturnDefault() {
        // Given
        String customConfigFile = "test.configuration";
        BigDecimal defaultValue = new BigDecimal(294032.23454);

        doReturn(defaultValue)
                .when(service)
                .getConfigAdminProperty(eq(customConfigFile), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));

        // When
        BigDecimal actualConfigProperty = service.getBigDecimalFrom(customConfigFile, TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo(defaultValue);
    }

    @Test
    void shouldGetBigDecimalFromWithDefaultReturnPropertyValue() {
        // Given
        String customConfigFile = "test.configuration";
        BigDecimal defaultValue = new BigDecimal(5822.234);
        BigDecimal expectedValue = new BigDecimal(23.3);

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminProperty(eq(customConfigFile), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));

        // When
        BigDecimal actualConfigProperty = service.getBigDecimalFrom(customConfigFile, TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    // BigDecimal getBigDecimalFrom(String configPID, String configKey) throws ConfigPropertyException;

    @Test
    void shouldThrowExceptionWhenPropertyNotPresentInConfigFile() {
        // Given
        String customConfigFile = "test.configuration";

        doThrow(new ConfigPropertyException("Could not find property"))
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(customConfigFile), eq(TEST_CONFIG_KEY), any(InputMapper.class));
        // When
        ConfigPropertyException thrown =
                assertThrows(ConfigPropertyException.class, () -> service.getBigDecimalFrom(customConfigFile, TEST_CONFIG_KEY));

        assertThat(thrown).hasMessage("Could not find property");
    }

    @Test
    void shouldReturnPropertyFromConfigFile() {
        // Given
        String customConfigFile = "test.configuration";
        BigDecimal expectedValue = new BigDecimal(234.93d);

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(customConfigFile), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        BigDecimal actualConfigProperty = service.getBigDecimalFrom(customConfigFile, TEST_CONFIG_KEY);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    // BigDecimal getBigDecimal(String configKey, BigDecimal defaultValue);

    @Test
    void shouldReturnSystemBigDecimalConfigProperty() {
        // Given
        BigDecimal expectedValue = new BigDecimal(1223.245d);

        doReturn(expectedValue)
                .when(service)
                .getBigDecimalSystemProperty(TEST_CONFIG_KEY);

        // When
        BigDecimal actualConfigProperty = service.getBigDecimal(TEST_CONFIG_KEY, new BigDecimal(7777.23d));

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnBigDecimalConfigPropertyFromConfigurationAdminServiceWhenBigDecimalAlready() throws IOException {
        // Given
        BigDecimal expectedValue = new BigDecimal(111333.1d);

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(TEST_CONFIG_KEY, expectedValue);
        mockConfigurationWithProperties(properties);

        // When
        BigDecimal actualConfigProperty = service.getBigDecimal(TEST_CONFIG_KEY, new BigDecimal(888.2d));

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnBigDecimalConfigPropertyFromConfigurationAdminServiceWhenString() throws IOException {
        // Given
        BigDecimal expectedValue = new BigDecimal(111333.43d);

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(TEST_CONFIG_KEY, expectedValue.toPlainString());
        mockConfigurationWithProperties(properties);

        // When
        BigDecimal actualConfigProperty = service.getBigDecimal(TEST_CONFIG_KEY, new BigDecimal(11109.1d));

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnDefaultBigDecimalConfigProperty() throws IOException {
        // Given
        BigDecimal defaultValue = new BigDecimal(653333.3d);

        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);
        mockConfigurationWithProperties(null);

        // When
        BigDecimal actualConfigProperty = service.getBigDecimal(TEST_CONFIG_KEY, defaultValue);

        // Then
        assertThat(actualConfigProperty).isEqualTo(defaultValue);
    }

    // BigDecimal getBigDecimal(String configKey) throws ConfigPropertyException;

    @Test
    void shouldThrowExceptionWhenPropertyNotPresentInDefaultConfigFile() {
        // Given
        doThrow(new ConfigPropertyException("Could not find property"))
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        ConfigPropertyException thrown =
                assertThrows(ConfigPropertyException.class, () -> service.getBigDecimal(TEST_CONFIG_KEY));

        assertThat(thrown).hasMessage("Could not find property");
    }

    @Test
    void shouldReturnPropertyFromDefaultConfigFile() {
        // Given
        BigDecimal expectedValue = new BigDecimal(35000.1d);

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        BigDecimal actualConfigProperty = service.getBigDecimal(TEST_CONFIG_KEY);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    // BigDecimal get(String configKey, Class<T> type)

    @Test
    void shouldReturnBigDecimalFromGenericConfigProviderAndDefaultConfigFile() {
        // Given
        BigDecimal expectedValue = new BigDecimal(35000.1d);

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(InputMapper.class));

        // When
        BigDecimal actualConfigProperty = service.get(TEST_CONFIG_KEY, BigDecimal.class);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    // BigDecimal get(String configKey, T defaultValue, Class<T> type)

    @Test
    void shouldReturnBigDecimalDefaultValueFromGenericConfigProviderAndDefaultConfigFile() {
        // Given
        BigDecimal defaultValue = new BigDecimal(294032.23454);

        doReturn(defaultValue)
                .when(service)
                .getConfigAdminProperty(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), eq(defaultValue),  any(InputMapper.class));


        // When
        BigDecimal actualConfigProperty = service.get(TEST_CONFIG_KEY, defaultValue, BigDecimal.class);

        // Then
        assertThat(actualConfigProperty).isEqualTo(defaultValue);
    }
}
