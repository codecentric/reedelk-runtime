package com.reedelk.platform.test.utils;

import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;

public class TestComponentWithObjectProperty implements ProcessorSync {

    private TestImplementor config;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        throw new UnsupportedOperationException("Test Only ProcessorSync");
    }

    public TestImplementor getConfig() {
        return config;
    }

    public void setConfig(TestImplementor config) {
        this.config = config;
    }
}
