package com.reedelk.openapi.v3;

import com.reedelk.openapi.OpenApiDeserializer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiDeserializerTest {

    @Test
    void shouldDeserializeFromJSON() {
        // Given
        String input = Fixture.EndToEnd.SAMPLE_JSON.string();

        // When
        OpenApiObject actual = OpenApiDeserializer.from(input);

        // Then

        assertThat(actual).isNotNull();
    }

}
