package de.codecentric.reedelk.platform.execution;

import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import org.reactivestreams.Publisher;

public interface FlowExecutor {

    Publisher<MessageAndContext> execute(
            Publisher<MessageAndContext> publisher, ExecutionNode currentNode, ExecutionGraph graph);

}

