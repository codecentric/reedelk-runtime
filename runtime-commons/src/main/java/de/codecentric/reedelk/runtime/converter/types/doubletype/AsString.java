package de.codecentric.reedelk.runtime.converter.types.doubletype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsString implements ValueConverter<Double,String> {

    @Override
    public String from(Double value) {
        return Double.toString(value);
    }
}
