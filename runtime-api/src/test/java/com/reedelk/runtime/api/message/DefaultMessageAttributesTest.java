package com.reedelk.runtime.api.message;

import com.reedelk.runtime.api.commons.TestComponent;
import org.junit.jupiter.api.Test;

import static com.reedelk.runtime.api.commons.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;

class DefaultMessageAttributesTest {

    @Test
    void hasAttributeShouldBeCaseInsensitive() {
        // Given
        MessageAttributes attributes = DefaultMessageAttributes.from(TestComponent.class, of("prop1", "value1"));

        // When
        boolean hasAttribute = attributes.contains("PROP1");

        // Then
        assertThat(hasAttribute).isTrue();
    }

    @Test
    void getAttributeShouldBeCaseInsensitive() {
        // Given
        MessageAttributes attributes = DefaultMessageAttributes.from(TestComponent.class, of("proP1", "value1"));

        // When
        Object attributeValue = attributes.get("PRop1");

        // Then
        assertThat(attributeValue).isEqualTo("value1");
    }

    @Test
    void shouldUseExistingCorrelationIDAttributeIfPresentAlready() {
        // Given
        String expectedCorrelationID = "aabbccdd";
        MessageAttributes attributes = DefaultMessageAttributes.from(TestComponent.class, of("X-CorRelaTION-ID", expectedCorrelationID));

        // When
        String attributeValue = attributes.get("X-CorrelatIOn-ID");

        // Then
        assertThat(attributeValue).isEqualTo(expectedCorrelationID);
    }

    @Test
    void shouldSetComponentName() {
        // Given
        String expectedComponentName = TestComponent.class.getName();
        MessageAttributes attributes = DefaultMessageAttributes.from(TestComponent.class, of("property1", "value1"));

        // When
        String attributeValue = attributes.get("compOnenT");

        // Then
        assertThat(attributeValue).isEqualTo(expectedComponentName);
    }
}
