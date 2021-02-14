package de.codecentric.reedelk.platform.services.converter;

import de.codecentric.reedelk.runtime.api.converter.ConverterService;
import de.codecentric.reedelk.runtime.api.message.content.TypedPublisher;
import de.codecentric.reedelk.runtime.converter.RuntimeConverters;
import de.codecentric.reedelk.runtime.converter.RuntimeTypedPublisherConverters;

public class DefaultConverterService implements ConverterService {

    private static final DefaultConverterService INSTANCE = new DefaultConverterService();

    private DefaultConverterService() {
    }

    public static ConverterService getInstance() {
        return INSTANCE;
    }

    @Override
    public <O> O convert(Object input, Class<O> outputClass) {
        return RuntimeConverters.getInstance().convert(input, outputClass);
    }

    @Override
    public <I, O> TypedPublisher<O> convert(TypedPublisher<I> input, Class<O> outputClass) {
        return RuntimeTypedPublisherConverters.getInstance().convert(input, outputClass);
    }
}
