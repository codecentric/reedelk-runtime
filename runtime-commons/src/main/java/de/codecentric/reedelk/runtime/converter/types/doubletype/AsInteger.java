package de.codecentric.reedelk.runtime.converter.types.doubletype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

class AsInteger implements ValueConverter<Double,Integer> {

    @Override
    public Integer from(Double value) {
        return  value.intValue();
    }
}
