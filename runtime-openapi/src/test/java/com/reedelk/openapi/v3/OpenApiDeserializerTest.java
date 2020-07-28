package com.reedelk.openapi.v3;

import com.reedelk.openapi.OpenApiDeserializer1;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiDeserializerTest {

    @Test
    void shouldDeserializeFromJSON() {
        // Given
        String input = Fixture.EndToEnd.SAMPLE_JSON.string();

        // When
        OpenApiObjectAbstract actual = OpenApiDeserializer1.from(input);

        // Then
        assertThat(actual).isNotNull();
    }

}
