package com.reedelk.esb.flow.deserializer.converter;

import com.reedelk.runtime.api.resource.ResourceFile;
import com.reedelk.runtime.api.resource.ResourceText;
import org.reactivestreams.Publisher;

public class ProxyResourceText extends ResourceText {

    private final Publisher<String> data;

    public ProxyResourceText(ResourceFile<?> original, Publisher<String> data) {
        super(original.path());
        this.data = data;
    }

    @Override
    public Publisher<String> data() {
        return data;
    }
}