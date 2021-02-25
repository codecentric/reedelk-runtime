package de.codecentric.reedelk.runtime.api.message.content;

import reactor.core.publisher.Mono;

public class TypedMono {

    // string

    public static TypedPublisher<String> just(String data) {
        return TypedPublisher.fromString(Mono.justOrEmpty(data));
    }

    public static TypedPublisher<String> emptyString() {
        return TypedPublisher.fromString(Mono.empty());
    }

    // byte array

    public static TypedPublisher<byte[]> just(byte[] data) {
        return TypedPublisher.fromByteArray(Mono.justOrEmpty(data));
    }

    public static TypedPublisher<byte[]> emptyByteArray() {
        return TypedPublisher.fromByteArray(Mono.empty());
    }

    // float

    public static TypedPublisher<Float> just(Float data) {
        return TypedPublisher.fromFloat(Mono.justOrEmpty(data));
    }

    public static TypedPublisher<Float> emptyFloat() {
        return TypedPublisher.fromFloat(Mono.empty());
    }

    // integer

    public static TypedPublisher<Integer> just(Integer data) {
        return TypedPublisher.fromInteger(Mono.justOrEmpty(data));
    }

    public static TypedPublisher<Integer> emptyInteger() {
        return TypedPublisher.fromInteger(Mono.empty());
    }

    // double

    public static TypedPublisher<Double> just(Double data) {
        return TypedPublisher.fromDouble(Mono.justOrEmpty(data));
    }

    public static TypedPublisher<Double> emptyDouble() {
        return TypedPublisher.fromDouble(Mono.empty());
    }

    // boolean

    public static TypedPublisher<Boolean> just(Boolean data) {
        return TypedPublisher.fromBoolean(Mono.justOrEmpty(data));
    }

    public static TypedPublisher<Boolean> emptyBoolean() {
        return TypedPublisher.fromBoolean(Mono.empty());
    }

    // void

    public static TypedPublisher<Void> just(Void data) {
        return TypedPublisher.fromVoid(Mono.justOrEmpty(data));
    }

    public static TypedPublisher<Void> emptyVoid() {
        return TypedPublisher.fromVoid(Mono.empty());
    }

    // object

    public static <T> TypedPublisher<T> just(T data, Class<T> clazz) {
        Mono<T> mono = Mono.justOrEmpty(data);
        return TypedPublisher.fromObject(mono, clazz);
    }

    public static <T> TypedPublisher<T> from(Mono<T> mono, Class<T> clazz) {
        return TypedPublisher.fromObject(mono, clazz);
    }
}
