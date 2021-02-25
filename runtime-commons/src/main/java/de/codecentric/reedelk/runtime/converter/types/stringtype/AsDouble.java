package de.codecentric.reedelk.runtime.converter.types.stringtype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsDouble implements ValueConverter<String,Double> {

    @Override
    public Double from(String value) {
        return Double.parseDouble(value);
    }
}
