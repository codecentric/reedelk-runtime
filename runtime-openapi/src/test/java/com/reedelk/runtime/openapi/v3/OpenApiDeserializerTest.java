package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.OpenApiDeserializer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiDeserializerTest {

    @Test
    void shouldDeserializeFromJSON() {
        // Given
        String input = Fixture.EndToEnd.SAMPLE_JSON.string();

        // When
        OpenApiObjectAbstract actual = OpenApiDeserializer.from(input);

        // Then
        assertThat(actual).isNotNull();
    }

}
