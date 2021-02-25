package de.codecentric.reedelk.runtime.converter.types.doubletype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsByteArray implements ValueConverter<Double,byte[]> {

    @Override
    public byte[] from(Double value) {
        return new byte[] {value.byteValue()};
    }
}
