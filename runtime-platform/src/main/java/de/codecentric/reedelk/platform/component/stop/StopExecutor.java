package de.codecentric.reedelk.platform.component.stop;

import de.codecentric.reedelk.platform.execution.FlowExecutor;
import de.codecentric.reedelk.platform.execution.MessageAndContext;
import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import org.reactivestreams.Publisher;

public class StopExecutor implements FlowExecutor {
    /**
     * When we find a stop node, we just return the parent flux.
     * Nothing needs to be added to the flux anymore.
     */
    @Override
    public Publisher<MessageAndContext> execute(Publisher<MessageAndContext> publisher, ExecutionNode currentNode, ExecutionGraph graph) {
        return publisher;
    }
}
