package com.reedelk.runtime.api.commons;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

public class StackTraceUtils {

    public static Publisher<byte[]> asByteStream(Throwable exception) {
        String exceptionAsString = asString(exception);
        return exceptionAsString == null ?
                Mono.empty() :
                Mono.just(exceptionAsString.getBytes());
    }

    public static byte[] asByteArray(Throwable exception) {
        String exceptionAsString = asString(exception);
        return exceptionAsString == null ?
                new byte[0] :
                exceptionAsString.getBytes();
    }

    public static String asString(Throwable exception) {
        try (StringWriter stringWriter = new StringWriter()) {
            try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
                exception.printStackTrace(printWriter);
                return stringWriter.toString();
            }
        } catch (IOException e) {
            return String.format("Error: could not serialize exception as byte stream [%s]", e.getMessage());
        }
    }

    public static Throwable rootCauseOf(Throwable throwable) {
        Objects.requireNonNull(throwable);
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

    public static String rootCauseMessageOf(Throwable throwable) {
        return rootCauseOf(throwable).getMessage();
    }
}
