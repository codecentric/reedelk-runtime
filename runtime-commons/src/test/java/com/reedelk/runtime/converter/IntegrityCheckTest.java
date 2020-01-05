package com.reedelk.runtime.converter;

import com.reedelk.runtime.api.commons.PlatformTypes;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IntegrityCheckTest {

    @Test
    void shouldHaveADeserializerConverterForEachPlatformType() {
        // Given
        int platformTypesSize = PlatformTypes.size();
        int deserializerConvertersSize = DeserializerConverterDefault.size();

        // We exclude Enum (handled in the convert), Combo and Password (which are String typed)
        assertThat(platformTypesSize - 3)
                .withFailMessage("The number of deserializer converters do not match the supported platform types!")
                .isEqualTo(deserializerConvertersSize );

        // Expect
        DeserializerConverterDefault.supportedConverters().forEach(supportedConverterClazz -> {
                    String typeFullyQualifiedName = supportedConverterClazz.getName();
                    boolean supported = PlatformTypes.isSupported(typeFullyQualifiedName);
                    assertThat(supported)
                            .withFailMessage("The default converter for type=[" + typeFullyQualifiedName + "] is " +
                                    "not a supported platform type! Add the type to the supported platform types or " +
                                    "remove the converter.")
                            .isTrue();
                });
    }
}