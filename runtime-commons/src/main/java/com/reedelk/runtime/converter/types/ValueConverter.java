package com.reedelk.runtime.converter.types;

public interface ValueConverter<I, O> {

    O from(I value);

}
