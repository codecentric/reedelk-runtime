package com.reedelk.runtime.api.resource;

import org.reactivestreams.Publisher;

public interface ResourceFile<T> {

    String path();

    Publisher<T> data();

}
