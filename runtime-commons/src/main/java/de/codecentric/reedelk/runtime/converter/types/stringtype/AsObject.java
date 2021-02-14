package de.codecentric.reedelk.runtime.converter.types.stringtype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsObject implements ValueConverter<String,Object> {

    @Override
    public Object from(String value) {
        return value;
    }
}
