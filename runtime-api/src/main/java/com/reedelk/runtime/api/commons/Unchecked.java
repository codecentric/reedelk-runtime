package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.exception.ESBException;

import java.util.function.Consumer;
import java.util.function.Function;

public class Unchecked {

    private Unchecked() {
    }

    public static <T, R, E extends Exception> Function<T, R> function(FunctionWithException<T, R, E> functionThrowingCheckedException) {
        return arg -> {
            try {
                return functionThrowingCheckedException.apply(arg);
            } catch (Exception exception) {
                throw new ESBException(exception);
            }
        };
    }

    public static <T, E extends Exception> Consumer<T> consumer(ConsumerWithException<T, E> consumer) {
        return arg -> {
            try {
                consumer.accept(arg);
            } catch (Exception exception) {
                throw new ESBException(exception);
            }
        };
    }
}
