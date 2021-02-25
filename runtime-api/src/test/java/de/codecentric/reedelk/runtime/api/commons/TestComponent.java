package de.codecentric.reedelk.runtime.api.commons;

import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;

public class TestComponent implements ProcessorSync {
    @Override
    public Message apply(FlowContext flowContext, Message message) {
        throw new UnsupportedOperationException("test only");
    }
}
