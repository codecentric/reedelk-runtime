package com.reedelk.esb.commons;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class SerializationUtilsTest {

    @Test
    void shouldCloneCorrectly() {
        // Given
        MessageEntry entry1 = new MessageEntry(23f);
        MessageEntry entry2 = new MessageEntry(112.2f);

        String aString = "a description";
        int anInt = 2343;
        Collection<String> aListOfStrings = asList("one", "two", "three");
        Collection<MessageEntry> aListOfObjects = asList(entry1, entry2);
        MessageObjectSample message = new MessageObjectSample(anInt, aString, aListOfStrings, aListOfObjects);

        // When
        MessageObjectSample clone = SerializationUtils.clone(message);

        // Then
        assertThat(clone).isNotEqualTo(message);
        assertThat(clone.getaString()).isEqualTo(aString);
        assertThat(clone.getAnInt()).isEqualTo(anInt);
        assertThat(clone.getaSimpleCollection()).containsExactlyInAnyOrder("one", "two", "three");

        Collection<MessageEntry> messageEntries = clone.getaComplexCollection();
        assertThatContainsEntryWith(messageEntries, 23f);
        assertThatContainsEntryWith(messageEntries, 112.2f);
        assertThat(messageEntries).doesNotContain(entry1, entry2);
    }

    private void assertThatContainsEntryWith(Collection<MessageEntry> messageEntries, float targetValue) {
        boolean found = messageEntries
                .stream()
                .anyMatch(messageEntry -> messageEntry.getValue() == targetValue);
        assertThat(found).isTrue();
    }
}