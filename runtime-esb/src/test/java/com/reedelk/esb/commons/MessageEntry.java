package com.reedelk.esb.commons;

import java.io.Serializable;

public class MessageEntry implements Serializable {

    private float value;

    public MessageEntry(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
