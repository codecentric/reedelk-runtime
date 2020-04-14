package com.reedelk.platform.test.utils;

import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.script.dynamicvalue.*;

public class TestComponentWithDynamicValueProperty implements ProcessorSync {

    private DynamicLong dynamicLongProperty;
    private DynamicFloat dynamicFloatProperty;
    private DynamicDouble dynamicDoubleProperty;
    private DynamicString dynamicStringProperty;
    private DynamicObject dynamicObjectProperty;
    private DynamicBoolean dynamicBooleanProperty;
    private DynamicInteger dynamicIntegerProperty;
    private DynamicByteArray dynamicByteArrayProperty;
    private DynamicBigDecimal dynamicBigDecimalProperty;
    private DynamicBigInteger dynamicBigIntegerProperty;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        throw new UnsupportedOperationException("Test Only ProcessorSync");
    }

    public DynamicLong getDynamicLongProperty() {
        return dynamicLongProperty;
    }

    public void setDynamicLongProperty(DynamicLong dynamicLongProperty) {
        this.dynamicLongProperty = dynamicLongProperty;
    }

    public DynamicFloat getDynamicFloatProperty() {
        return dynamicFloatProperty;
    }

    public void setDynamicFloatProperty(DynamicFloat dynamicFloatProperty) {
        this.dynamicFloatProperty = dynamicFloatProperty;
    }

    public DynamicDouble getDynamicDoubleProperty() {
        return dynamicDoubleProperty;
    }

    public void setDynamicDoubleProperty(DynamicDouble dynamicDoubleProperty) {
        this.dynamicDoubleProperty = dynamicDoubleProperty;
    }

    public DynamicString getDynamicStringProperty() {
        return dynamicStringProperty;
    }

    public void setDynamicStringProperty(DynamicString dynamicStringProperty) {
        this.dynamicStringProperty = dynamicStringProperty;
    }

    public DynamicObject getDynamicObjectProperty() {
        return dynamicObjectProperty;
    }

    public void setDynamicObjectProperty(DynamicObject dynamicObjectProperty) {
        this.dynamicObjectProperty = dynamicObjectProperty;
    }

    public DynamicBoolean getDynamicBooleanProperty() {
        return dynamicBooleanProperty;
    }

    public void setDynamicBooleanProperty(DynamicBoolean dynamicBooleanProperty) {
        this.dynamicBooleanProperty = dynamicBooleanProperty;
    }

    public DynamicInteger getDynamicIntegerProperty() {
        return dynamicIntegerProperty;
    }

    public void setDynamicIntegerProperty(DynamicInteger dynamicIntegerProperty) {
        this.dynamicIntegerProperty = dynamicIntegerProperty;
    }

    public DynamicByteArray getDynamicByteArrayProperty() {
        return dynamicByteArrayProperty;
    }

    public void setDynamicByteArrayProperty(DynamicByteArray dynamicByteArrayProperty) {
        this.dynamicByteArrayProperty = dynamicByteArrayProperty;
    }

    public DynamicBigDecimal getDynamicBigDecimalProperty() {
        return dynamicBigDecimalProperty;
    }

    public void setDynamicBigDecimalProperty(DynamicBigDecimal dynamicBigDecimalProperty) {
        this.dynamicBigDecimalProperty = dynamicBigDecimalProperty;
    }

    public DynamicBigInteger getDynamicBigIntegerProperty() {
        return dynamicBigIntegerProperty;
    }

    public void setDynamicBigIntegerProperty(DynamicBigInteger dynamicBigIntegerProperty) {
        this.dynamicBigIntegerProperty = dynamicBigIntegerProperty;
    }
}
