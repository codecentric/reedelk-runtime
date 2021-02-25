package de.codecentric.reedelk.runtime.converter.types.stringtype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsFloat implements ValueConverter<String,Float> {

    @Override
    public Float from(String value) {
        return Float.parseFloat(value);
    }
}
