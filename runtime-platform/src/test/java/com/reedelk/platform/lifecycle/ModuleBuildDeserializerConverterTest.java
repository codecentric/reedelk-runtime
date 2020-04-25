package com.reedelk.platform.lifecycle;

import com.reedelk.platform.module.DeSerializedModule;
import com.reedelk.platform.module.Module;
import com.reedelk.runtime.api.configuration.ConfigurationService;
import com.reedelk.runtime.converter.DeserializerConverter;
import com.reedelk.runtime.converter.DeserializerConverterContext;
import com.reedelk.runtime.system.api.SystemProperty;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyZeroInteractions;

@ExtendWith(MockitoExtension.class)
public class ModuleBuildDeserializerConverterTest {

    @Mock
    private Module module;
    @Mock
    private SystemProperty systemPropertyService;
    @Mock
    private ConfigurationService configurationService;
    @Mock
    private DeSerializedModule deSerializedModule;

    private final long testModuleId = 10L;
    private DeserializerConverter converter;
    private DeserializerConverterContext deserializerConverterContext = new DeserializerConverterContext(testModuleId);

    @BeforeEach
    void setUp() {
        ModuleBuild moduleBuild = new ModuleBuild();
        moduleBuild.systemPropertyService(systemPropertyService);
        moduleBuild.configurationService(configurationService);
        this.converter =
                moduleBuild.createDeserializerConverter(deSerializedModule, module, testModuleId);
    }

    // We want to make sure that the resolution of system config properties
    // happens before the resolution of user defined config properties and not vice-versa.
    @Test
    void shouldResolveSystemConfigPropertyBeforeConfigProperties() {
        // Given
        String propertyName = "myProperty";
        String expectedValue = "/var/runtime/config";

        doReturn(expectedValue)
                .when(systemPropertyService)
                .configDirectory();

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, "${RUNTIME_CONFIG}");

        // When
        Object typeInstance = converter.convert(String.class, componentDefinition, propertyName, deserializerConverterContext);

        // Then
        assertThat(typeInstance).isEqualTo(expectedValue);
        verifyZeroInteractions(configurationService);
    }

    @Test
    void shouldResolveConfigPropertyCorrectly() {
        // Given
        String propertyName = "myProperty";
        String propertyValue = "${my.host.name}";
        String expectedValue = "http://mydomain.com";

        doReturn(expectedValue)
                .when(configurationService)
                .get("my.host.name", String.class);

        JSONObject componentDefinition = new JSONObject();
        componentDefinition.put(propertyName, propertyValue);

        // When
        Object typeInstance = converter.convert(String.class, componentDefinition, propertyName, deserializerConverterContext);

        // Then
        assertThat(typeInstance).isEqualTo(expectedValue);
        verifyZeroInteractions(systemPropertyService);
    }
}
