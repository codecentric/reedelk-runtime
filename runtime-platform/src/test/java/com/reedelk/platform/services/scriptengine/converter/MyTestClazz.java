package com.reedelk.platform.services.scriptengine.converter;

import java.io.Serializable;

public class MyTestClazz implements Serializable {

    private int intVal;
    private String stringVal;

    public MyTestClazz(int intVal, String stringVal) {
        this.intVal = intVal;
        this.stringVal = stringVal;
    }

    public int getIntVal() {
        return intVal;
    }

    public void setIntVal(int intVal) {
        this.intVal = intVal;
    }

    public String getStringVal() {
        return stringVal;
    }

    public void setStringVal(String stringVal) {
        this.stringVal = stringVal;
    }

    @Override
    public String toString() {
        return "MyTestClazz{" +
                "intVal=" + intVal +
                ", stringVal='" + stringVal + '\'' +
                '}';
    }
}
