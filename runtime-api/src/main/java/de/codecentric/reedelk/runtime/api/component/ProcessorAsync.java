package de.codecentric.reedelk.runtime.api.component;

import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;

public interface ProcessorAsync extends Component {

    void apply(FlowContext flowContext, Message input, OnResult callback);

}