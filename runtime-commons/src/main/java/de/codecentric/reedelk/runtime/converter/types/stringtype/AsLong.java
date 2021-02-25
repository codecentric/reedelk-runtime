package de.codecentric.reedelk.runtime.converter.types.stringtype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsLong implements ValueConverter<String,Long> {

    @Override
    public Long from(String value) {
        return Long.parseLong(value);
    }
}
