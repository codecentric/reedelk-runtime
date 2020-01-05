package com.reedelk.esb.services.configuration;

import com.reedelk.runtime.api.exception.ConfigPropertyException;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@MockitoSettings(strictness = Strictness.LENIENT)
class DefaultConfigurationServiceByClassTypeTest extends BaseDefaultConfigurationServiceTest {

    @Test
    void shouldReturnStringProperty() throws IOException {
        // Given
        String expectedValue = "Test property";

        mockConfigurationWithTestPropertyValue(expectedValue);

        // When
        String actualConfigProperty = service.get(TEST_CONFIG_KEY, String.class);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnIntProperty() throws IOException {
        // Given
        int expectedValue = 34;

        mockConfigurationWithTestPropertyValue(expectedValue);

        // When
        int actualConfigProperty = service.get(TEST_CONFIG_KEY, int.class);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnIntObjectProperty() throws IOException {
        // Given
        Integer expectedValue = 653;

        mockConfigurationWithTestPropertyValue(expectedValue);

        // When
        Integer actualConfigProperty = service.get(TEST_CONFIG_KEY, Integer.class);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnLongProperty() throws IOException {
        // Given
        long expectedValue = 543L;

        mockConfigurationWithTestPropertyValue(expectedValue);

        // When
        long actualConfigProperty = service.get(TEST_CONFIG_KEY, long.class);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnLongObjectProperty() throws IOException {
        // Given
        Long expectedValue = 234L;

        mockConfigurationWithTestPropertyValue(expectedValue);

        // When
        Long actualConfigProperty = service.get(TEST_CONFIG_KEY, Long.class);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnBooleanProperty() throws IOException {
        // Given
        boolean expectedValue = true;

        mockConfigurationWithTestPropertyValue(expectedValue);

        // When
        boolean actualConfigProperty = service.get(TEST_CONFIG_KEY, boolean.class);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldReturnBooleanObjectProperty() throws IOException {
        // Given
        Boolean expectedValue = true;

        mockConfigurationWithTestPropertyValue(expectedValue);

        // When
        Boolean actualConfigProperty = service.get(TEST_CONFIG_KEY, Boolean.class);

        // Then
        assertThat(actualConfigProperty).isEqualTo(expectedValue);
    }

    @Test
    void shouldThrowExceptionWhenPropertyWithGivenKeyDoesNotExist() throws IOException {
        // Given
        doReturn(null)
                .when(service)
                .getStringSystemProperty(TEST_CONFIG_KEY);

        Dictionary<String, Object> properties = new Hashtable<>();
        mockConfigurationWithProperties(properties);

        // When
        ConfigPropertyException thrown =
                assertThrows(ConfigPropertyException.class,
                        () -> service.get(TEST_CONFIG_KEY, String.class));

        // Then
        assertThat(thrown).hasMessage("Could not find config property with key=[name.endpoint].");
    }

    @Test
    void shouldThrowExceptionWhenPropertyWithTargetClazzUnsupportedIsRetrieved() {
        // Expect
        ConfigPropertyException thrown =
                assertThrows(ConfigPropertyException.class,
                        () -> service.get(TEST_CONFIG_KEY, Number.class));

        // Then
        assertThat(thrown).hasMessage("Unsupported conversion. Could not convert config property with key=[name.endpoint] for config pid=[configuration] to type=[java.lang.Number].");
    }
}
