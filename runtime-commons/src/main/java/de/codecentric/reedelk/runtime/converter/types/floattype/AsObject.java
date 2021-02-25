package de.codecentric.reedelk.runtime.converter.types.floattype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsObject implements ValueConverter<Float,Object> {

    @Override
    public Object from(Float value) {
        return value;
    }
}
