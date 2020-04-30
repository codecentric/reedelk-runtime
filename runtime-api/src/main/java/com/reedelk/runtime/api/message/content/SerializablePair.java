package com.reedelk.runtime.api.message.content;

import java.io.Serializable;

public class SerializablePair<L extends Serializable,R extends Serializable> implements Pair<L, R> {

    private final L left;
    private final R right;

    public SerializablePair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public L getLeft() {
        return left;
    }

    @Override
    public R getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}
