package de.codecentric.reedelk.runtime.converter.types.floattype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsString implements ValueConverter<Float,String> {

    @Override
    public String from(Float value) {
        return String.valueOf(value);
    }
}
