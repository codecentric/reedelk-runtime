package de.codecentric.reedelk.platform.services.configuration;

import de.codecentric.reedelk.runtime.api.script.dynamicvalue.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

class DefaultConfigurationServiceDynamicValueTest extends BaseDefaultConfigurationServiceTest {

    // DynamicBigDecimal get(String configKey, Class<T> type)

    @Test
    void shouldReturnDynamicBigDecimalFromGenericConfigProviderAndDefaultConfigFile() {
        // Given
        BigDecimal expectedValue = new BigDecimal(35000.1d);

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(DefaultConfigurationService.InputMapper.class));

        // When
        DynamicBigDecimal actualConfigProperty = service.get(TEST_CONFIG_KEY, DynamicBigDecimal.class);

        // Then
        assertThat(actualConfigProperty.value()).isEqualTo(expectedValue);
    }

    // DynamicBigInteger get(String configKey, Class<T> type)

    @Test
    void shouldReturnDynamicBigIntegerFromGenericConfigProviderAndDefaultConfigFile() {
        // Given
        BigInteger expectedValue = new BigInteger("35000");

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(DefaultConfigurationService.InputMapper.class));

        // When
        DynamicBigInteger actualConfigProperty = service.get(TEST_CONFIG_KEY, DynamicBigInteger.class);

        // Then
        assertThat(actualConfigProperty.value()).isEqualTo(expectedValue);
    }

    // DynamicBoolean get(String configKey, Class<T> type)

    @Test
    void shouldReturnDynamicBooleanFromGenericConfigProviderAndDefaultConfigFile() {
        // Given
        boolean expectedValue = true;

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(DefaultConfigurationService.InputMapper.class));

        // When
        DynamicBoolean actualConfigProperty = service.get(TEST_CONFIG_KEY, DynamicBoolean.class);

        // Then
        assertThat(actualConfigProperty.value()).isEqualTo(expectedValue);
    }

    // DynamicDouble get(String configKey, Class<T> type)

    @Test
    void shouldReturnDynamicDoubleFromGenericConfigProviderAndDefaultConfigFile() {
        // Given
        double expectedValue = 234.234324d;

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(DefaultConfigurationService.InputMapper.class));

        // When
        DynamicDouble actualConfigProperty = service.get(TEST_CONFIG_KEY, DynamicDouble.class);

        // Then
        assertThat(actualConfigProperty.value()).isEqualTo(expectedValue);
    }

    // DynamicFloat get(String configKey, Class<T> type)

    @Test
    void shouldReturnDynamicFloatFromGenericConfigProviderAndDefaultConfigFile() {
        // Given
        float expectedValue = 987.24f;

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(DefaultConfigurationService.InputMapper.class));

        // When
        DynamicFloat actualConfigProperty = service.get(TEST_CONFIG_KEY, DynamicFloat.class);

        // Then
        assertThat(actualConfigProperty.value()).isEqualTo(expectedValue);
    }

    // DynamicInteger get(String configKey, Class<T> type)

    @Test
    void shouldReturnDynamicIntegerFromGenericConfigProviderAndDefaultConfigFile() {
        // Given
        int expectedValue = 753;

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(DefaultConfigurationService.InputMapper.class));

        // When
        DynamicInteger actualConfigProperty = service.get(TEST_CONFIG_KEY, DynamicInteger.class);

        // Then
        assertThat(actualConfigProperty.value()).isEqualTo(expectedValue);
    }

    // DynamicLong get(String configKey, Class<T> type)

    @Test
    void shouldReturnDynamicLongFromGenericConfigProviderAndDefaultConfigFile() {
        // Given
        long expectedValue = 753L;

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(DefaultConfigurationService.InputMapper.class));

        // When
        DynamicLong actualConfigProperty = service.get(TEST_CONFIG_KEY, DynamicLong.class);

        // Then
        assertThat(actualConfigProperty.value()).isEqualTo(expectedValue);
    }

    // DynamicString get(String configKey, Class<T> type)

    @Test
    void shouldReturnDynamicStringFromGenericConfigProviderAndDefaultConfigFile() {
        // Given
        String expectedValue = "my config value";

        doReturn(expectedValue)
                .when(service)
                .getConfigAdminPropertyOrThrow(eq(DEFAULT_CONFIG_FILE), eq(TEST_CONFIG_KEY), any(DefaultConfigurationService.InputMapper.class));

        // When
        DynamicString actualConfigProperty = service.get(TEST_CONFIG_KEY, DynamicString.class);

        // Then
        assertThat(actualConfigProperty.value()).isEqualTo(expectedValue);
    }
}
