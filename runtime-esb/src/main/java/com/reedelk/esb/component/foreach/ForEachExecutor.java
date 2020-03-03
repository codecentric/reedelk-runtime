package com.reedelk.esb.component.foreach;

import com.reedelk.esb.execution.FlowExecutor;
import com.reedelk.esb.execution.MessageAndContext;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import org.reactivestreams.Publisher;

public class ForEachExecutor implements FlowExecutor {

    @Override
    public Publisher<MessageAndContext> execute(Publisher<MessageAndContext> publisher, ExecutionNode currentNode, ExecutionGraph graph) {
        return null;
    }
}
