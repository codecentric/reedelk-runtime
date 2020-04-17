package com.reedelk.runtime.api.message.content;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class DataRowTest {

    @Test
    void shouldCorrectlyConvertToMap() {
        // Given
        DataRow<Serializable> dataRow =
                new DefaultDataRow(asList("column1", "column2"), asList("value1", 23));

        // When
        Map<String, Serializable> map = dataRow.asMap();

        // Then
        assertThat(map).containsEntry("column1", "value1");
        assertThat(map).containsEntry("column2", 23);
        assertThat(map).hasSize(2);
    }
}
