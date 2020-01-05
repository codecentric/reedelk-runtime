package com.reedelk.esb.commons;

import com.reedelk.runtime.api.exception.ESBException;

import java.util.function.Consumer;
import java.util.function.Function;

public class FunctionWrapper {

    private FunctionWrapper() {
    }

    public static <T, R, E extends Exception> Function<T, R> unchecked(FunctionWithException<T, R, E> functionThrowingCheckedException) {
        return arg -> {
            try {
                return functionThrowingCheckedException.apply(arg);
            } catch (Exception e) {
                throw new ESBException(e);
            }
        };
    }

    public static <T, E extends Exception> Consumer<T> uncheckedConsumer(ConsumerWithException<T, E> consumer) {
        return arg -> {
            try {
                consumer.accept(arg);
            } catch (Exception e) {
                throw new ESBException(e);
            }
        };
    }
}
