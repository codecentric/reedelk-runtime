package com.reedelk.runtime.api.message.content;

import java.io.Serializable;

public class SerializablePair implements Pair<Serializable, Serializable> {

    private final Serializable key;
    private final Serializable value;

    SerializablePair(Serializable key, Serializable value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Serializable getKey() {
        return key;
    }

    @Override
    public Serializable getValue() {
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
