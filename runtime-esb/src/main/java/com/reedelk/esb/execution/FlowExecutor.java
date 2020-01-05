package com.reedelk.esb.execution;

import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import org.reactivestreams.Publisher;

public interface FlowExecutor {

    Publisher<MessageAndContext> execute(
            Publisher<MessageAndContext> publisher, ExecutionNode currentNode, ExecutionGraph graph);

}

