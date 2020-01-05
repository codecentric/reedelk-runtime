package com.reedelk.runtime.api.resource;

import org.reactivestreams.Publisher;

public class ResourceBinary implements ResourceFile<byte[]> {

    private final String resourcePath;

    public static ResourceBinary from(String resourcePath) {
        return new ResourceBinary(resourcePath);
    }

    protected ResourceBinary(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public Publisher<byte[]> data() {
        throw new UnsupportedOperationException("implemented in the proxy");
    }

    @Override
    public String path() {
        return resourcePath;
    }
}
