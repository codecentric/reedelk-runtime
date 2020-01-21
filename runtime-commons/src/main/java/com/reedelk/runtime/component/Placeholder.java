package com.reedelk.runtime.component;

import com.reedelk.runtime.api.annotation.ESBComponent;
import com.reedelk.runtime.api.annotation.Hidden;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;

@Hidden
@ESBComponent("Placeholder")
public class Placeholder implements ProcessorSync {

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        return message;
    }
}
