package de.codecentric.reedelk.runtime.api.message.content;

import java.io.Serializable;

/**
 * getX, setX are in the interface so that from script language I can use item.key.
 * @param <L> the type of the left (or key) value.
 * @param <R> the type of the right (or value) value.
 */
public interface Pair<L extends Serializable, R extends Serializable> extends Serializable {

    L getLeft();

    R getRight();

    default L left() {
        return getLeft();
    }

    default R right() {
        return getRight();
    }

    static <L extends Serializable, R extends Serializable> Pair<L,R> create(L left, R right) {
        return new SerializablePair<>(left, right);
    }
}
