package de.codecentric.reedelk.module.descriptor.analyzer.property;

import de.codecentric.reedelk.runtime.api.annotation.ScriptSignature;
import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.script.Script;

public class ScriptSignatureComponent implements ProcessorSync {

    @ScriptSignature(arguments = {"arg1","arg2","arg3"}, types = {String.class, Message.class, FlowContext.class})
    private Script propertyWithSignature;

    private Script propertyWithoutSignature;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        throw new UnsupportedOperationException();
    }
}
