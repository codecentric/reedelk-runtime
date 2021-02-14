package de.codecentric.reedelk.platform.test.utils;

import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;

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
