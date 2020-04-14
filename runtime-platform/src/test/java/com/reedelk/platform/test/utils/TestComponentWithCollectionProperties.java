package com.reedelk.platform.test.utils;

import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class TestComponentWithCollectionProperties implements ProcessorSync {

    private Collection<Long> myLongCollection;
    private Collection<Integer> myIntCollection;
    private Collection<Float> myFloatCollection;
    private Collection<Double> myDoubleCollection;
    private Collection<String> myStringCollection;
    private Collection<Boolean> myBooleanCollection;
    private Collection<BigInteger> myBigIntegerCollection;
    private Collection<BigDecimal> myBigDecimalCollection;

    private List<Long> myLongList;
    private List<Integer> myIntList;
    private List<Float> myFloatList;
    private List<Double> myDoubleList;
    private List<String> myStringList;
    private List<Boolean> myBooleanList;
    private List<BigInteger> myBigIntegerList;
    private List<BigDecimal> myBigDecimalList;

    private Set<Long> myLongSet;
    private Set<Integer> myIntSet;
    private Set<Float> myFloatSet;
    private Set<Double> myDoubleSet;
    private Set<String> myStringSet;
    private Set<Boolean> myBooleanSet;
    private Set<BigInteger> myBigIntegerSet;
    private Set<BigDecimal> myBigDecimalSet;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        throw new UnsupportedOperationException("Test Only ProcessorSync");
    }

    public Collection<Long> getMyLongCollection() {
        return myLongCollection;
    }

    public void setMyLongCollection(Collection<Long> myLongCollection) {
        this.myLongCollection = myLongCollection;
    }

    public Collection<Integer> getMyIntCollection() {
        return myIntCollection;
    }

    public void setMyIntCollection(Collection<Integer> myIntCollection) {
        this.myIntCollection = myIntCollection;
    }

    public Collection<Float> getMyFloatCollection() {
        return myFloatCollection;
    }

    public void setMyFloatCollection(Collection<Float> myFLoatCollection) {
        this.myFloatCollection = myFLoatCollection;
    }

    public Collection<Double> getMyDoubleCollection() {
        return myDoubleCollection;
    }

    public void setMyDoubleCollection(Collection<Double> myDoubleCollection) {
        this.myDoubleCollection = myDoubleCollection;
    }

    public Collection<String> getMyStringCollection() {
        return myStringCollection;
    }

    public void setMyStringCollection(Collection<String> myStringCollection) {
        this.myStringCollection = myStringCollection;
    }

    public Collection<Boolean> getMyBooleanCollection() {
        return myBooleanCollection;
    }

    public void setMyBooleanCollection(Collection<Boolean> myBooleanCollection) {
        this.myBooleanCollection = myBooleanCollection;
    }

    public Collection<BigInteger> getMyBigIntegerCollection() {
        return myBigIntegerCollection;
    }

    public void setMyBigIntegerCollection(Collection<BigInteger> myBigIntegerCollection) {
        this.myBigIntegerCollection = myBigIntegerCollection;
    }

    public Collection<BigDecimal> getMyBigDecimalCollection() {
        return myBigDecimalCollection;
    }

    public void setMyBigDecimalCollection(Collection<BigDecimal> myBigDecimalCollection) {
        this.myBigDecimalCollection = myBigDecimalCollection;
    }

    public List<Long> getMyLongList() {
        return myLongList;
    }

    public void setMyLongList(List<Long> myLongList) {
        this.myLongList = myLongList;
    }

    public List<Integer> getMyIntList() {
        return myIntList;
    }

    public void setMyIntList(List<Integer> myIntList) {
        this.myIntList = myIntList;
    }

    public List<Float> getMyFloatList() {
        return myFloatList;
    }

    public void setMyFloatList(List<Float> myFloatList) {
        this.myFloatList = myFloatList;
    }

    public List<Double> getMyDoubleList() {
        return myDoubleList;
    }

    public void setMyDoubleList(List<Double> myDoubleList) {
        this.myDoubleList = myDoubleList;
    }

    public List<String> getMyStringList() {
        return myStringList;
    }

    public void setMyStringList(List<String> myStringList) {
        this.myStringList = myStringList;
    }

    public List<Boolean> getMyBooleanList() {
        return myBooleanList;
    }

    public void setMyBooleanList(List<Boolean> myBooleanList) {
        this.myBooleanList = myBooleanList;
    }

    public List<BigInteger> getMyBigIntegerList() {
        return myBigIntegerList;
    }

    public void setMyBigIntegerList(List<BigInteger> myBigIntegerList) {
        this.myBigIntegerList = myBigIntegerList;
    }

    public List<BigDecimal> getMyBigDecimalList() {
        return myBigDecimalList;
    }

    public void setMyBigDecimalList(List<BigDecimal> myBigDecimalList) {
        this.myBigDecimalList = myBigDecimalList;
    }

    public Set<Long> getMyLongSet() {
        return myLongSet;
    }

    public void setMyLongSet(Set<Long> myLongSet) {
        this.myLongSet = myLongSet;
    }

    public Set<Integer> getMyIntSet() {
        return myIntSet;
    }

    public void setMyIntSet(Set<Integer> myIntSet) {
        this.myIntSet = myIntSet;
    }

    public Set<Float> getMyFloatSet() {
        return myFloatSet;
    }

    public void setMyFloatSet(Set<Float> myFloatSet) {
        this.myFloatSet = myFloatSet;
    }

    public Set<Double> getMyDoubleSet() {
        return myDoubleSet;
    }

    public void setMyDoubleSet(Set<Double> myDoubleSet) {
        this.myDoubleSet = myDoubleSet;
    }

    public Set<String> getMyStringSet() {
        return myStringSet;
    }

    public void setMyStringSet(Set<String> myStringSet) {
        this.myStringSet = myStringSet;
    }

    public Set<Boolean> getMyBooleanSet() {
        return myBooleanSet;
    }

    public void setMyBooleanSet(Set<Boolean> myBooleanSet) {
        this.myBooleanSet = myBooleanSet;
    }

    public Set<BigInteger> getMyBigIntegerSet() {
        return myBigIntegerSet;
    }

    public void setMyBigIntegerSet(Set<BigInteger> myBigIntegerSet) {
        this.myBigIntegerSet = myBigIntegerSet;
    }

    public Set<BigDecimal> getMyBigDecimalSet() {
        return myBigDecimalSet;
    }

    public void setMyBigDecimalSet(Set<BigDecimal> myBigDecimalSet) {
        this.myBigDecimalSet = myBigDecimalSet;
    }
}
