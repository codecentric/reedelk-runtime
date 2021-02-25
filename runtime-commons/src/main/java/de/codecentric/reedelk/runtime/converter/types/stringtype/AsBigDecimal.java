package de.codecentric.reedelk.runtime.converter.types.stringtype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

import java.math.BigDecimal;

class AsBigDecimal implements ValueConverter<String, BigDecimal> {

    @Override
    public BigDecimal from(String value) {
        return new BigDecimal(value);
    }
}
