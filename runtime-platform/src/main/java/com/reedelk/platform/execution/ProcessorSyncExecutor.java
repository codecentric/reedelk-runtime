package com.reedelk.platform.execution;

import com.reedelk.platform.graph.ExecutionGraph;
import com.reedelk.platform.graph.ExecutionNode;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.exception.PlatformException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.util.function.BiConsumer;

import static com.reedelk.platform.commons.NextNode.ofOrThrow;

public class ProcessorSyncExecutor implements FlowExecutor {

    @Override
    public Publisher<MessageAndContext> execute(Publisher<MessageAndContext> publisher, ExecutionNode currentNode, ExecutionGraph graph) {

        ProcessorSync processorSync = (ProcessorSync) currentNode.getComponent();

        Publisher<MessageAndContext> mono = Flux.from(publisher).handle(apply(processorSync));

        ExecutionNode next = ofOrThrow(currentNode, graph);

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
                Message outMessage = processor.apply(flowContext, input);

                // We replace in the context the new output message.
                messageAndContext.replaceWith(outMessage);

                // Notify next element ready
                sink.next(messageAndContext);

            } catch (Exception exception) {
                // Propagate the error occurred while applying the processor
                sink.error(exception);

            } catch (Throwable throwable) {
                // If Throwable is 'NoClassDefFoundError' it means that the processor uses a component
                // which uses a class not imported in the module bundle it belongs to.
                // The module bundle should correctly add the missing import in the pom.xml configuration:
                // maven-bundle-plugin > configuration > instructions > Import-Package xml node.
                // IMPORTANT: This exception must be wrapped because Throwable exceptions are NOT
                // caught in the com.reedelk.platform.execution.FlowExecutorEngine -> doOnError callback.
                // They are normally caught at subscribe time however it does not apply because
                // the FlowExecutorEngine the stream on publishOn(SchedulerProvider.flow())
                // - i.e on a different thread. Therefore we must wrap 'NoClassDefFoundError' exception.
                PlatformException wrapped = new PlatformException(throwable);
                sink.error(wrapped);
            }
        };
    }
}
