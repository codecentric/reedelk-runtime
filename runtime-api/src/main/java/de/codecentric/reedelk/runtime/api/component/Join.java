package de.codecentric.reedelk.runtime.api.component;

import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;

import java.util.List;

/**
 * Interface used to provide a custom (merge) Component able to join multiple input messages.
 */
public interface Join extends Component {

    Message apply(FlowContext flowContext, List<Message> messagesToJoin);

}
