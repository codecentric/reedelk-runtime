package com.reedelk.esb.services.configuration;

import com.reedelk.esb.services.configuration.configurer.ConfigFile;
import com.reedelk.esb.services.configuration.configurer.PropertiesConfigFile;
import com.reedelk.esb.services.configuration.configurer.XmlConfigFile;
import com.reedelk.runtime.api.commons.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.osgi.service.cm.Configuration;

import java.io.IOException;
import java.util.*;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
class DefaultConfigurationServiceTest extends BaseDefaultConfigurationServiceTest {

    @Nested
    @DisplayName("Get config Admin property")
    class ConfigAdminProperty {

        @Test
        void getConfigAdminPropertyShouldReturnDefaultValueWhenPropertyNotDefinedInDictionary() throws IOException {
            // Given
            Dictionary<String, Object> properties = new Hashtable<>();
            mockConfigurationWithProperties(properties);

            // When
            String actualProperty = service.getConfigAdminProperty("a.config.pid", TEST_CONFIG_KEY, "Default", input -> (String) input);

            // Then
            assertThat(actualProperty).isEqualTo("Default");
        }

        @Test
        void getConfigAdminPropertyShouldReturnEmptyWhenPropertyDefinedInDictionaryAndIsEmpty() throws IOException {
            // Given
            Dictionary<String, Object> properties = new Hashtable<>();
            properties.put(TEST_CONFIG_KEY, StringUtils.EMPTY);
            mockConfigurationWithProperties(properties);

            // When
            String actualProperty = service.getConfigAdminProperty("a.config.pid", TEST_CONFIG_KEY, "Default", o -> (String) o);

            // Then
            assertThat(actualProperty).isEmpty();
        }
    }

    @Nested
    @DisplayName("Initialize service tests")
    class InitializeService {

        @Test
        void shouldInitializeApplyLogbackConfigurerCorrectly() throws IOException {
            // Given
            String expectedValue = "my/logback/file/path/logback.xml";

            Configuration mockConfiguration = mock(Configuration.class);
            doReturn(mockConfiguration)
                    .when(mockConfigurationAdmin)
                    .getConfiguration("org.ops4j.pax.logging", "?");

            XmlConfigFile mockLogbackConfigFile = mock(XmlConfigFile.class);
            doReturn(expectedValue)
                    .when(mockLogbackConfigFile)
                    .getFilePath();
            doReturn("logback.xml")
                    .when(mockLogbackConfigFile)
                    .getFileName();

            Collection<ConfigFile> mockConfigFiles = Collections.singletonList(mockLogbackConfigFile);
            doReturn(mockConfigFiles)
                    .when(service)
                    .listConfigFilesFromConfigDirectory();

            // When
            service.initialize();

            // Then
            verify(mockConfiguration).update(dictionaryCaptor.capture());

            Dictionary<String, Object> updatedDictionary = dictionaryCaptor.getValue();
            String actualValue = (String) updatedDictionary.get("org.ops4j.pax.logging.logback.config.file");

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        void shouldInitializeApplyConfigPropertiesFileCorrectly() throws IOException {
            // Given
            String pid1 = "com.reedelk.esb.custom.components1";
            String pid2 = "com.reedelk.esb.custom.components2";

            PropertiesConfigFile propertiesConfigFile1 = mock(PropertiesConfigFile.class);
            Properties properties1 = singleKeyValueProperty("key1", "value1");
            doReturn(pid1 + ".properties").when(propertiesConfigFile1).getFileName();
            doReturn(properties1).when(propertiesConfigFile1).getContent();

            PropertiesConfigFile propertiesConfigFile2 = mock(PropertiesConfigFile.class);
            Properties properties2 = singleKeyValueProperty("key2", "value2");
            doReturn(pid2 + ".properties").when(propertiesConfigFile2).getFileName();
            doReturn(properties2).when(propertiesConfigFile2).getContent();

            Collection<ConfigFile> mockConfigFiles = asList(propertiesConfigFile1, propertiesConfigFile2);
            doReturn(mockConfigFiles)
                    .when(service)
                    .listConfigFilesFromConfigDirectory();

            Configuration mockConfiguration1 = mock(Configuration.class);
            doReturn(mockConfiguration1)
                    .when(mockConfigurationAdmin)
                    .getConfiguration(pid1, "?");

            Configuration mockConfiguration2 = mock(Configuration.class);
            doReturn(mockConfiguration2)
                    .when(mockConfigurationAdmin)
                    .getConfiguration(pid2, "?");

            // When
            service.initialize();

            // Then
            verify(mockConfiguration1).update(dictionaryCaptor.capture());
            Dictionary<String, Object> updatedDictionary1 = dictionaryCaptor.getValue();
            assertThat(updatedDictionary1).isEqualTo(properties1);

            verify(mockConfiguration2).update(dictionaryCaptor.capture());
            Dictionary<String, Object> updatedDictionary2 = dictionaryCaptor.getValue();
            assertThat(updatedDictionary2).isEqualTo(properties2);
        }
    }
}