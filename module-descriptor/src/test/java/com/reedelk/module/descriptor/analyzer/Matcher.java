package com.reedelk.module.descriptor.analyzer;

public interface Matcher<T> {

    boolean matches(T input);
}
