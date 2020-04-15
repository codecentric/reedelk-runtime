package com.reedelk.runtime.api.message.content;

import java.io.Serializable;

public class SerializablePair<L extends Serializable,R extends Serializable> implements Pair<L, R> {

    private final L key;
    private final R value;

    SerializablePair(L key, R value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public L getKey() {
        return key;
    }

    @Override
    public R getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
