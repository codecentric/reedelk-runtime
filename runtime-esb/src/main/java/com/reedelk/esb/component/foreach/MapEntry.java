package com.reedelk.esb.component.foreach;

import com.reedelk.runtime.api.message.content.Pair;

import java.io.Serializable;

public class MapEntry implements Pair<Serializable,Serializable> {

    private final Serializable key;
    private final Serializable value;

    public MapEntry(Serializable key, Serializable value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Serializable key() {
        return key;
    }

    @Override
    public Serializable value() {
        return value;
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
    public Serializable left() {
        return key;
    }

    @Override
    public Serializable right() {
        return value;
    }

    @Override
    public Serializable getLeft() {
        return key;
    }

    @Override
    public Serializable getRight() {
        return value;
    }

    @Override
    public String toString() {
        return "MapEntry{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
