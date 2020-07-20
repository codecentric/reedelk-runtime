package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.openapi.OpenApiJsons;
import org.junit.jupiter.api.Test;

public class ContactObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeContactWithAllProperties() {
        // Given
        ContactObject contact = new ContactObject();
        contact.setName("API Support");
        contact.setUrl("http://www.example.com/support");
        contact.setEmail("support@example.com");

        // Expect
        assertSerializedCorrectly(contact, OpenApiJsons.ContactObject.WithAllProperties);
    }

    @Test
    void shouldCorrectlySerializeContactWithRequiredValues() {
        // Given
        ContactObject contact = new ContactObject();

        // Expect (expect empty, because there are no required properties for contact)
        assertSerializedCorrectly(contact, OpenApiJsons.ContactObject.WithDefaultProperties);
    }
}
