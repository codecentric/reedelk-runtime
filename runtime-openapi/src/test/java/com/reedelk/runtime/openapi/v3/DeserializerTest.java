package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.v3.model.OpenApiObject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeserializerTest {

    @Test
    void shouldDeserializeFromJSON() {
        // Given
        String input = Fixture.EndToEnd.SAMPLE_JSON.string();

        // When
        OpenApiObject actual = Deserializer.from(input);

        // Then
        assertThat(actual).isNotNull();
    }

}
