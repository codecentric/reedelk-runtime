package com.reedelk.runtime.api.commons;

@FunctionalInterface
public interface ConsumerWithException<T, E extends Exception> {

    void accept(T t) throws E;

}
