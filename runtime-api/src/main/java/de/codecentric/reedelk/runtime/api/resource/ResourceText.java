package de.codecentric.reedelk.runtime.api.resource;

import org.reactivestreams.Publisher;

public class ResourceText implements ResourceFile<String> {

    private final String resourcePath;

    public static ResourceText from(String resourcePath) {
        return new ResourceText(resourcePath);
    }

    protected ResourceText(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public String path() {
        return resourcePath;
    }

    @Override
    public Publisher<String> data() {
        throw new UnsupportedOperationException("implemented in the proxy");
    }
}
