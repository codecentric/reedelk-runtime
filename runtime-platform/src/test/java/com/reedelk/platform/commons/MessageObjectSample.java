package com.reedelk.platform.commons;

import java.io.Serializable;
import java.util.Collection;

public class MessageObjectSample implements Serializable {

    private int anInt;
    private String aString;
    private Collection<String> aSimpleCollection;
    private Collection<MessageEntry> aComplexCollection;

    public MessageObjectSample(int anInt, String aString, Collection<String> aSimpleCollection, Collection<MessageEntry> aComplexCollection) {
        this.anInt = anInt;
        this.aString = aString;
        this.aSimpleCollection = aSimpleCollection;
        this.aComplexCollection = aComplexCollection;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }

    public Collection<String> getaSimpleCollection() {
        return aSimpleCollection;
    }

    public void setaSimpleCollection(Collection<String> aSimpleCollection) {
        this.aSimpleCollection = aSimpleCollection;
    }

    public Collection<MessageEntry> getaComplexCollection() {
        return aComplexCollection;
    }

    public void setaComplexCollection(Collection<MessageEntry> aComplexCollection) {
        this.aComplexCollection = aComplexCollection;
    }
}
