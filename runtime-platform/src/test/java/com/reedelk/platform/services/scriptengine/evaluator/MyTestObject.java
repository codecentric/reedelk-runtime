package com.reedelk.platform.services.scriptengine.evaluator;

public class MyTestObject {

    private int intValue;
    private float floatValue;
    private String stringValue;

    public MyTestObject(int intValue, float floatValue, String stringValue) {
        this.intValue = intValue;
        this.floatValue = floatValue;
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
