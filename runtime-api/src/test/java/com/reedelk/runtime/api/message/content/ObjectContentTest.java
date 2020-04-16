package com.reedelk.runtime.api.message.content;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectContentTest {

    @Test
    void shouldReturnFalseWhenIsStreamAndObjectCreatedFromMono() {
        // Given
        MyItem item1 = new MyItem();
        Mono<MyItem> itemMono = Mono.just(item1);
        ObjectContent<MyItem> content = new ObjectContent<>(itemMono, MyItem.class);

        // When
        boolean stream = content.isStream();

        // Then
        assertThat(stream).isFalse();
    }

    static class MyItem {
    }
}
