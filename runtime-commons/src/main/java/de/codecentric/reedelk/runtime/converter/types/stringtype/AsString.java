package de.codecentric.reedelk.runtime.converter.types.stringtype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsString implements ValueConverter<String,String> {

    @Override
    public String from(String value) {
        return value;
    }
}
