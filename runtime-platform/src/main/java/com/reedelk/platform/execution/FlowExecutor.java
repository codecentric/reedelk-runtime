package com.reedelk.platform.execution;

import com.reedelk.platform.graph.ExecutionGraph;
import com.reedelk.platform.graph.ExecutionNode;
import org.reactivestreams.Publisher;

public interface FlowExecutor {

    Publisher<MessageAndContext> execute(
            Publisher<MessageAndContext> publisher, ExecutionNode currentNode, ExecutionGraph graph);

}

