package com.reedelk.runtime.api.message.content;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

/**
 * A publisher wrapper carrying information about the class
 * type of the stream content. This is needed to properly
 * convert results from the evaluation of a script to the
 * correct desired type.
 * @param <T> the type of the data inside the wrapped stream.
 */
@SuppressWarnings("PublisherImplementation")
public class TypedPublisher<T> implements Publisher<T> {

    private final Class<T> clazz;
    private final Publisher<T> delegate;

    public static <T> TypedPublisher<T> from(Publisher<T> delegate, Class<T> clazz) {
        return new TypedPublisher<>(delegate, clazz);
    }

    // string

    public static TypedPublisher<String> fromString(Publisher<String> delegate) {
        return new TypedPublisher<>(delegate, String.class);
    }

    // byte array

    public static TypedPublisher<byte[]> fromByteArray(Publisher<byte[]> delegate) {
        return new TypedPublisher<>(delegate, byte[].class);
    }

    // float

    public static TypedPublisher<Float> fromFloat(Publisher<Float> delegate) {
        return new TypedPublisher<>(delegate, Float.class);
    }

    // integer

    public static TypedPublisher<Integer> fromInteger(Publisher<Integer> delegate) {
        return new TypedPublisher<>(delegate, Integer.class);
    }

    // double

    public static TypedPublisher<Double> fromDouble(Publisher<Double> delegate) {
        return new TypedPublisher<>(delegate, Double.class);
    }

    // boolean

    public static TypedPublisher<Boolean> fromBoolean(Publisher<Boolean> delegate) {
        return new TypedPublisher<>(delegate, Boolean.class);
    }

    // void

    public static TypedPublisher<Void> fromVoid(Publisher<Void> delegate) {
        return new TypedPublisher<>(delegate, Void.class);
    }

    // object

    public static <T> TypedPublisher<T> fromObject(Publisher<T> delegate, Class<T> clazz) {
        return new TypedPublisher<>(delegate, clazz);
    }

    private TypedPublisher(Publisher<T> delegate, Class<T> clazz) {
        this.clazz = clazz;
        this.delegate = delegate;
    }

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        this.delegate.subscribe(subscriber);
    }

    public Class<T> getType() {
        return clazz;
    }
}
