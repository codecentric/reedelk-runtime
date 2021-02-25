package de.codecentric.reedelk.runtime.component;

import de.codecentric.reedelk.runtime.api.annotation.Hidden;
import de.codecentric.reedelk.runtime.api.annotation.ModuleComponent;
import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;

@Hidden
@ModuleComponent("Placeholder")
public class Placeholder implements ProcessorSync {

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        return message;
    }
}
