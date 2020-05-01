package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.runtime.api.annotation.ScriptSignature;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.script.Script;

public class ScriptSignatureComponent implements ProcessorSync {

    @ScriptSignature(arguments = {"arg1","arg2","arg3"}, types = {String.class, Message.class, FlowContext.class})
    private Script propertyWithSignature;

    private Script propertyWithoutSignature;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        throw new UnsupportedOperationException();
    }
}
