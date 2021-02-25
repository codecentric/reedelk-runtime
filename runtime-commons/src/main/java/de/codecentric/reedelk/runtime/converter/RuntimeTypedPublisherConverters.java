package de.codecentric.reedelk.runtime.converter;

import de.codecentric.reedelk.runtime.api.message.content.TypedPublisher;
import de.codecentric.reedelk.runtime.converter.types.ValueConverter;
import reactor.core.publisher.Flux;

import java.util.Map;
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
            Map<Class<?>, ValueConverter<?, ?>> fromInputConverters =
                    RuntimeConverters.converters().getOrDefault(input.getType(), RuntimeConverters.defaults());
            return Optional.of(fromInputConverters)
                    .flatMap(fromConverter -> Optional.ofNullable((ValueConverter<I, O>) fromConverter.get(outputClass)))
                    .map(toConverter -> TypedPublisher.from(Flux.from(input).map(toConverter::from), outputClass))
                    .orElseThrow(RuntimeConverters.converterNotFound(input.getType(), outputClass));
        }
    }
}
