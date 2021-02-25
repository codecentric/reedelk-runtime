package de.codecentric.reedelk.runtime.converter.types.booleantype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsString implements ValueConverter<Boolean,String> {

    @Override
    public String from(Boolean value) {
        return String.valueOf(value);
    }
}
