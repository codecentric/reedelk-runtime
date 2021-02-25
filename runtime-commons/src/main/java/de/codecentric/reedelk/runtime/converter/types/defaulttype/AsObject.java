package de.codecentric.reedelk.runtime.converter.types.defaulttype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsObject implements ValueConverter<Object,Object> {

    @Override
    public Object from(Object value) {
        return value;
    }
}
