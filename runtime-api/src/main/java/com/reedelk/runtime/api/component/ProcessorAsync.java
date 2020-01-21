package com.reedelk.runtime.api.component;

import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;

public interface ProcessorAsync extends Component {

    void apply(FlowContext flowContext, Message input, OnResult callback);

}