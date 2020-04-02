package com.reedelk.esb.component.foreach;

import com.reedelk.runtime.api.message.content.Pair;

public class MapEntry implements Pair<Object,Object> {

    private final Object key;
    private final Object value;

    public MapEntry(Object key, Object value) {
        this.key = key;
        this.value = value;
    }


    @Override
    public Object key() {
        return key;
    }

    @Override
    public Object value() {
        return value;
    }

    @Override
    public Object getKey() {
        return key;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Object left() {
        return key;
    }

    @Override
    public Object right() {
        return value;
    }

    @Override
    public Object getLeft() {
        return key;
    }

    @Override
    public Object getRight() {
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
