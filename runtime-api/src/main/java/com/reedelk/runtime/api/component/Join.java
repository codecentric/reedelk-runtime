package com.reedelk.runtime.api.component;

import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;

import java.util.List;

/**
 * Interface used to provide a custom (merge) Component able to join multiple input messages.
 */
public interface Join extends Component {

    Message apply(FlowContext flowContext, List<Message> messagesToJoin);

}
