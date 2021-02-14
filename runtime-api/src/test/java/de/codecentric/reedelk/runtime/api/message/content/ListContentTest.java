package de.codecentric.reedelk.runtime.api.message.content;

import de.codecentric.reedelk.runtime.api.message.content.ListContent;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ListContentTest {

    @Test
    void shouldCorrectlyCloneObject() {
        // Given
        List<MyObject> data = Arrays.asList(new MyObject(3), new MyObject(34));
        ListContent<MyObject> content = new ListContent<>(data, MyObject.class);

        // Then
        assertThat(content.streamType()).isEqualTo(MyObject.class);
        assertThat(content.data()).isEqualTo(data);
    }

    static class MyObject {
        int value;
        public MyObject(int value) {
            this.value = value;
        }
    }
}
