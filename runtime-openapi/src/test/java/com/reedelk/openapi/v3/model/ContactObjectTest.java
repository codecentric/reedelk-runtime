package com.reedelk.openapi.v3.model;

import com.reedelk.openapi.v3.ContactObject;
import com.reedelk.openapi.v3.Fixture;
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
        assertSerializeJSON(contact, Fixture.ContactObject.WithAllProperties);
    }

    @Test
    void shouldCorrectlySerializeContactWithRequiredValues() {
        // Given
        ContactObject contact = new ContactObject();

        // Expect (expect empty, because there are no required properties for contact)
        assertSerializeJSON(contact, Fixture.ContactObject.WithDefaultProperties);
    }
}
