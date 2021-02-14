package de.codecentric.reedelk.runtime.converter.types.defaulttype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsString implements ValueConverter<Object,String> {

    @Override
    public String from(Object value) {
        return value == null ? null : value.toString();
    }
}
