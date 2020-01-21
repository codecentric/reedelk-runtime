package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;

public class TestComponent implements ProcessorSync {
    @Override
    public Message apply(FlowContext flowContext, Message message) {
        throw new UnsupportedOperationException("test only");
    }
}
