package com.reedelk.runtime.api.commons;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import static org.assertj.core.api.Assertions.assertThat;

class StreamUtilsTest {

    @Test
    void shouldReturnEmptyArray() {
        // Given
        Flux<byte[]> flux = Flux.empty();

        // When
        byte[] actual = StreamUtils.FromByteArray.consume(flux);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnSameArrayWhenFluxHasSize1() {
        // Given
        byte[] input = "Test".getBytes();

        // When
        byte[] actual = StreamUtils.FromByteArray.consume(Flux.just(input));

        // Then
        assertThat(actual).isEqualTo(input);
    }

    @Test
    void shouldReturnCorrectBytes() {
        // Given
        byte[] input1 = "Hello ".getBytes();
        byte[] input2 = "World".getBytes();

        // When
        byte[] actual = StreamUtils.FromByteArray.consume(Flux.just(input1, input2));

        // Then
        assertThat(new String(actual)).isEqualTo("Hello World");
    }
}
