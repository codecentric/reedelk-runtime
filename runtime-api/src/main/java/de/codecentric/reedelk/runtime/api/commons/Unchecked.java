package de.codecentric.reedelk.runtime.api.commons;

import de.codecentric.reedelk.runtime.api.exception.PlatformException;

import java.util.function.BiFunction;
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
                throw new PlatformException(exception);
            }
        };
    }

    public static <T, R, E extends Exception> Function<T, R> function(FunctionWithException<T, R, E> functionThrowingCheckedException,
                                                                      BiFunction<T, Exception, ? extends RuntimeException> exceptionSupplier) {
        return arg -> {
            try {
                return functionThrowingCheckedException.apply(arg);
            } catch (Exception exception) {
                throw exceptionSupplier.apply(arg, exception);
            }
        };
    }

    public static <T, E extends Exception> Consumer<T> consumer(ConsumerWithException<T, E> consumerThrowingCheckedException) {
        return arg -> {
            try {
                consumerThrowingCheckedException.accept(arg);
            } catch (Exception exception) {
                throw new PlatformException(exception);
            }
        };
    }

    public static <T, E extends Exception> Consumer<T> consumer(ConsumerWithException<T, E> consumerThrowingCheckedException,
                                                                BiFunction<T, Exception, ? extends RuntimeException> exceptionSupplier) {
        return arg -> {
            try {
                consumerThrowingCheckedException.accept(arg);
            } catch (Exception exception) {
                throw exceptionSupplier.apply(arg, exception);
            }
        };
    }
}
