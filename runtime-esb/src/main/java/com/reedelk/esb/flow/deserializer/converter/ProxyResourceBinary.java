package com.reedelk.esb.flow.deserializer.converter;

import com.reedelk.runtime.api.resource.ResourceBinary;
import com.reedelk.runtime.api.resource.ResourceFile;
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