package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.message.FlowContext;
import com.reedelk.runtime.api.message.Message;

public class TestComponent implements ProcessorSync {
    @Override
    public Message apply(Message message, FlowContext flowContext) {
        throw new UnsupportedOperationException("test only");
    }
}
