package de.codecentric.reedelk.platform.services.scriptengine.evaluator;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Optional;

@SuppressWarnings("unchecked")
class ValueProviders {

    static final ValueProvider OPTIONAL_PROVIDER = new OptionalValueProvider();

    static final ValueProvider STREAM_PROVIDER = new StreamValueProvider();

    private ValueProviders(){
    }

    private static class OptionalValueProvider implements ValueProvider {
        @Override
        public Optional<?> empty() {
            return Optional.empty();
        }

        @Override
        public Optional<?> from(Object value) {
            return Optional.ofNullable(value);
        }
    }

    private static class StreamValueProvider implements ValueProvider {
        @Override
        public Publisher<?> empty() {
            return Mono.empty();
        }

        @Override
        public Publisher<?> from(Object value) {
            if (value instanceof Publisher<?>) {
                return (Publisher<?>) value;
            } else {
                return Mono.justOrEmpty(value);
            }
        }
    }
}
