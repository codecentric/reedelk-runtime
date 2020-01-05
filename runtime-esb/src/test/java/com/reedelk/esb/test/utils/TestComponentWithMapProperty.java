package com.reedelk.esb.test.utils;

import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.message.FlowContext;
import com.reedelk.runtime.api.message.Message;

import java.util.Map;

public class TestComponentWithMapProperty implements ProcessorSync {

    @Override
    public Message apply(Message message, FlowContext flowContext) {
        throw new UnsupportedOperationException("Test Only ProcessorSync");
    }

    private Map<String, Object> myObjectProperty;

    public Map<String, Object> getMyObjectProperty() {
        return myObjectProperty;
    }

    public void setMyObjectProperty(Map<String, Object> myObjectProperty) {
        this.myObjectProperty = myObjectProperty;
    }
}
