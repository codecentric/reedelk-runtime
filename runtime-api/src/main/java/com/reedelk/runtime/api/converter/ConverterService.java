package com.reedelk.runtime.api.converter;

import com.reedelk.runtime.api.message.content.TypedPublisher;

public interface ConverterService {

    <O> O convert(Object input, Class<O> outputClass);

    <I, O> TypedPublisher<O> convert(TypedPublisher<I> input, Class<O> outputClass);

}
