package de.codecentric.reedelk.platform.flow.deserializer.converter;

import de.codecentric.reedelk.runtime.api.resource.ResourceFile;
import de.codecentric.reedelk.runtime.api.resource.ResourceText;
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
