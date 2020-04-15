package com.reedelk.runtime.api.message.content;

import java.io.Serializable;

/**
 * getX, setX are in the interface so that from script language I can use item.key.
 * @param <L> the type of the left (or key) value.
 * @param <R> the type of the right (or value) value.
 */
public interface Pair<L extends Serializable, R extends Serializable> extends Serializable {

    L getKey();

    R getValue();

    default L key() {
        return getKey();
    }

    default R value() {
        return getValue();
    }

    default L left() {
        return getKey();
    }

    default R right() {
        return getValue();
    }

    default L getLeft() {
        return getKey();
    }

    default R getRight() {
        return getValue();
    }

    static <L extends Serializable, R extends Serializable> Pair<L,R> create(L key, R value) {
        return new SerializablePair<>(key, value);
    }
}
