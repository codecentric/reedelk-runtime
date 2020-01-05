package com.reedelk.esb.execution;

import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.message.FlowContext;
import com.reedelk.runtime.api.message.Message;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.util.function.BiConsumer;

import static com.reedelk.esb.execution.ExecutionUtils.nextNodeOfOrThrow;

public class ProcessorSyncExecutor implements FlowExecutor {

    @Override
    public Publisher<MessageAndContext> execute(Publisher<MessageAndContext> publisher, ExecutionNode currentNode, ExecutionGraph graph) {

        ProcessorSync processorSync = (ProcessorSync) currentNode.getComponent();

        Publisher<MessageAndContext> mono = Flux.from(publisher).handle(apply(processorSync));

        ExecutionNode next = nextNodeOfOrThrow(currentNode, graph);

        // Move on building the flux for the following
        // processors in the execution graph definition.
        return FlowExecutorFactory.get().execute(mono, next, graph);
    }

    private BiConsumer<MessageAndContext, SynchronousSink<MessageAndContext>> apply(ProcessorSync processor) {
        return (messageAndContext, sink) -> {

            Message input = messageAndContext.getMessage();

            FlowContext flowContext = messageAndContext.getFlowContext();

            try {

                // Apply the input Message to the processor and we
                // let it process it (transform) to its new value.
                Message outMessage = processor.apply(input, flowContext);

                // We replace in the context the new output message.
                messageAndContext.replaceWith(outMessage);

                // Notify next element ready
                sink.next(messageAndContext);

            } catch (Exception e) {

                // An error has occurred
                sink.error(e);

            }
        };
    }
}
