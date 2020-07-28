package com.reedelk.openapi.v3.model;

import com.reedelk.openapi.v3.Fixture;
import com.reedelk.openapi.v3.LicenseObject;
import org.junit.jupiter.api.Test;

class LicenseObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeLicenseWithAllProperties() {
        // Given
        LicenseObject license = new LicenseObject();
        license.setName("Apache 2.0");
        license.setUrl("http://www.apache.org/licenses/LICENSE-2.0.html");

        // Expect
        assertSerializeJSON(license, Fixture.LicenseObject.WithAllPropertiesJson);
        assertSerializeYAML(license, Fixture.LicenseObject.WithAllPropertiesYaml);
    }

    @Test
    void shouldCorrectlySerializeLicenseWithRequiredValues() {
        // Given
        LicenseObject license = new LicenseObject();

        // Expect
        assertSerializeJSON(license, Fixture.LicenseObject.WithDefaultPropertiesJson);
        assertSerializeYAML(license, Fixture.LicenseObject.WithDefaultPropertiesYaml);
    }
}
