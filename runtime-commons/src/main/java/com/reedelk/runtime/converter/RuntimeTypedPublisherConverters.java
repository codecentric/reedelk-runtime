package com.reedelk.runtime.converter;

import com.reedelk.runtime.api.message.content.utils.TypedPublisher;
import com.reedelk.runtime.converter.types.ValueConverter;
import reactor.core.publisher.Flux;

import java.util.Optional;

@SuppressWarnings("unchecked")
public class RuntimeTypedPublisherConverters {

    private static final RuntimeTypedPublisherConverters INSTANCE = new RuntimeTypedPublisherConverters();

    public static RuntimeTypedPublisherConverters getInstance() {
        return INSTANCE;
    }

    public <I, O> TypedPublisher<O> convert(TypedPublisher<I> input, Class<O> outputClass) {
        if (input == null) {
            return null;
        } else if (input.getType().equals(outputClass)) {
            return (TypedPublisher<O>) input;
        } else if  (Object.class.equals(outputClass)) {
            return (TypedPublisher<O>) input;
        } else {
            return Optional.ofNullable(RuntimeConverters.converters().get(input.getType()))
                    .flatMap(fromConverter -> Optional.ofNullable((ValueConverter<I, O>) fromConverter.get(outputClass)))
                    .map(toConverter -> TypedPublisher.from(Flux.from(input).map(toConverter::from), outputClass))
                    .orElseThrow(RuntimeConverters.converterNotFound(input.getType(), outputClass));
        }
    }
}
