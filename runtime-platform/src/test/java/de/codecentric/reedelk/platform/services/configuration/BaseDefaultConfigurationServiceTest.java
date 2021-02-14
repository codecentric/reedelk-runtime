package de.codecentric.reedelk.platform.services.configuration;

import de.codecentric.reedelk.runtime.system.api.SystemProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
abstract class BaseDefaultConfigurationServiceTest {

    @Mock
    protected SystemProperty systemProperty;
    @Mock
    protected ConfigurationAdmin mockConfigurationAdmin;
    @Captor
    protected ArgumentCaptor<Dictionary<String, Object>> dictionaryCaptor;

    protected DefaultConfigurationService service;

    final String DEFAULT_CONFIG_FILE = "configuration";
    final String TEST_CONFIG_KEY = "name.endpoint";

    @BeforeEach
    void setUp() {
        service = spy(new DefaultConfigurationService(mockConfigurationAdmin, systemProperty));
    }

    Properties singleKeyValueProperty(String key, String value) {
        Properties properties = new Properties();
        properties.put(key, value);
        return properties;
    }

    void mockConfigurationWithTestPropertyValue(Object propertyValue) throws IOException {
        Dictionary<String,Object> properties = new Hashtable<>();
        properties.put(TEST_CONFIG_KEY, propertyValue);
        mockConfigurationWithProperties(properties);
    }

    void mockConfigurationWithProperties(Dictionary<String, Object> properties) throws IOException {
        Configuration mockConfiguration = mock(Configuration.class);
        doReturn(properties)
                .when(mockConfiguration)
                .getProperties();
        doReturn(mockConfiguration)
                .when(mockConfigurationAdmin)
                .getConfiguration(any());
    }
}
