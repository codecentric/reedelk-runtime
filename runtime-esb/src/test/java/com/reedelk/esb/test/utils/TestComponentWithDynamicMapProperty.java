package com.reedelk.esb.test.utils;

import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;

public class TestComponentWithDynamicMapProperty implements ProcessorSync {

    private DynamicStringMap dynamicStringMapProperty;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        throw new UnsupportedOperationException("Test Only ProcessorSync");
    }

    public DynamicStringMap getDynamicStringMapProperty() {
        return dynamicStringMapProperty;
    }

    public void setDynamicStringMapProperty(DynamicStringMap dynamicStringMapProperty) {
        this.dynamicStringMapProperty = dynamicStringMapProperty;
    }
}
