package de.codecentric.reedelk.runtime.api.commons;

import de.codecentric.reedelk.runtime.api.commons.DefaultValues;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultValuesTest {

    @Test
    void shouldReturnCorrectDefaultValueForBooleanObject() {
        // When
        Object actual = DefaultValues.defaultValue(Boolean.class);

        // Then
        assertThat(actual).isNull();
    }

    @Test
    void shouldReturnCorrectDefaultValueForBoolean() {
        // When
        boolean actual = (boolean) DefaultValues.defaultValue(boolean.class);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void shouldReturnCorrectDefaultValueForIntegerObject() {
        // When
        Object actual = DefaultValues.defaultValue(Integer.class);

        // Then
        assertThat(actual).isNull();
    }

    @Test
    void shouldReturnCorrectDefaultValueForInt() {
        // When
        int actual = (int) DefaultValues.defaultValue(int.class);

        // Then
        assertThat(actual).isEqualTo(0);
    }

    @Test
    void shouldReturnCorrectDefaultValueForLongObject() {
        // When
        Object actual = DefaultValues.defaultValue(Long.class);

        // Then
        assertThat(actual).isNull();
    }

    @Test
    void shouldReturnCorrectDefaultValueForLong() {
        // When
        long actual = (long) DefaultValues.defaultValue(long.class);

        // Then
        assertThat(actual).isEqualTo(0L);
    }

    @Test
    void shouldReturnCorrectDefaultValueForFloatObject() {
        // When
        Object actual = DefaultValues.defaultValue(Float.class);

        // Then
        assertThat(actual).isNull();
    }

    @Test
    void shouldReturnCorrectDefaultValueForFloat() {
        // When
        float actual = (float) DefaultValues.defaultValue(float.class);

        // Then
        assertThat(actual).isEqualTo(0f);
    }

    @Test
    void shouldReturnCorrectDefaultValueForDoubleObject() {
        // When
        Object actual = DefaultValues.defaultValue(Double.class);

        // Then
        assertThat(actual).isNull();
    }

    @Test
    void shouldReturnCorrectDefaultValueForDouble() {
        // When
        double actual = (double) DefaultValues.defaultValue(double.class);

        // Then
        assertThat(actual).isEqualTo(0d);
    }

    @Test
    void shouldReturnCorrectDefaultValueForShortObject() {
        // When
        Object actual = DefaultValues.defaultValue(Short.class);

        // Then
        assertThat(actual).isNull();
    }

    @Test
    void shouldReturnCorrectDefaultValueForShort() {
        // When
        short actual = (short) DefaultValues.defaultValue(short.class);

        // Then
        assertThat(actual).isEqualTo((short) 0);
    }

    @Test
    void shouldReturnCorrectDefaultValueForByteObject() {
        // When
        Object actual = DefaultValues.defaultValue(Byte.class);

        // Then
        assertThat(actual).isNull();
    }

    @Test
    void shouldReturnCorrectDefaultValueForByte() {
        // When
        byte actual = (byte) DefaultValues.defaultValue(byte.class);

        // Then
        assertThat(actual).isEqualTo((byte) 0);
    }

    @Test
    void shouldReturnCorrectDefaultValueForCharacterObject() {
        // When
        Object actual = DefaultValues.defaultValue(Character.class);

        // Then
        assertThat(actual).isNull();
    }

    @Test
    void shouldReturnCorrectDefaultValueForChar() {
        // When
        char actual = (char) DefaultValues.defaultValue(char.class);

        // Then
        assertThat(actual).isEqualTo((char) '\u0000');
    }

    @Test
    void shouldReturnCorrectDefaultValueForAnyOtherObject() {
        // When
        Object actual = DefaultValues.defaultValue(List.class);

        // Then
        assertThat(actual).isNull();
    }
}