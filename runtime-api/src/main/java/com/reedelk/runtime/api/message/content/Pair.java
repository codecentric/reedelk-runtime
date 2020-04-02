package com.reedelk.runtime.api.message.content;

import java.io.Serializable;

/**
 * Get are in the interface so that from script language I can use item.key.
 * @param <L> the type of the left (or key) value.
 * @param <R> the type of the right (or value) value.
 */
public interface Pair<L,R> extends Serializable {

    L key();

    R value();

    L getKey();

    R getValue();

    L left();

    R right();

    L getLeft();

    R getRight();

}
