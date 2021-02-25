package de.codecentric.reedelk.runtime.converter.types.stringtype;

import de.codecentric.reedelk.runtime.converter.types.ValueConverter;

import java.math.BigInteger;

class AsBigInteger implements ValueConverter<String, BigInteger> {

    @Override
    public BigInteger from(String value) {
        return new BigInteger(value);
    }
}
