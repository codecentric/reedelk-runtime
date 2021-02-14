package de.codecentric.reedelk.runtime.api.component;

import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;

public interface ProcessorSync extends Component {

    Message apply(FlowContext flowContext, Message message);

}