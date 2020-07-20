package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.openapi.OpenApiJsons;
import org.junit.jupiter.api.Test;

class LicenseObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeLicenseWithAllProperties() {
        // Given
        LicenseObject license = new LicenseObject();
        license.setName("Apache 2.0");
        license.setUrl("http://www.apache.org/licenses/LICENSE-2.0.html");

        // Expect
        assertSerializedCorrectly(license, OpenApiJsons.LicenseObject.WithAllProperties);
    }

    @Test
    void shouldCorrectlySerializeLicenseWithRequiredValues() {
        // Given
        LicenseObject license = new LicenseObject();

        // Expect
        assertSerializedCorrectly(license, OpenApiJsons.LicenseObject.WithDefaultProperties);
    }
}
