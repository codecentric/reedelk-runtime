package de.codecentric.reedelk.platform.flow.deserializer.converter;

import de.codecentric.reedelk.runtime.api.resource.ResourceBinary;
import de.codecentric.reedelk.runtime.api.resource.ResourceFile;
import org.reactivestreams.Publisher;

public class ProxyResourceBinary extends ResourceBinary {

    private final Publisher<byte[]> data;

    public ProxyResourceBinary(ResourceFile<?> original, Publisher<byte[]> data) {
        super(original.path());
        this.data = data;
    }

    @Override
    public Publisher<byte[]> data() {
        return data;
    }
}
