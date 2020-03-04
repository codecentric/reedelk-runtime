package com.reedelk.esb.execution;

import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.esb.test.utils.TestInboundComponent;
import com.reedelk.runtime.api.component.Component;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.EmptyContent;
import com.reedelk.runtime.api.message.content.TypedContent;
import com.reedelk.runtime.component.Stop;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractExecutionTest {

    protected ExecutionNode inbound = newExecutionNode(new TestInboundComponent());
    protected ExecutionNode stop = newExecutionNode(new Stop());

    public static ExecutionNode newExecutionNode(Component component) {
        return new ExecutionNode(new ExecutionNode.ReferencePair<>(component));
    }

    protected MessageAndContext newEventWithContent(String content) {
        Message message = MessageBuilder.get().withText(content).build();
        return new NoActionResultMessageAndContext(message);
    }

    protected MessageAndContext newEventWithContent(Object content) {
        Message message = MessageBuilder.get().withJavaObject(content).build();
        return new NoActionResultMessageAndContext(message);
    }

    protected ExecutionGraph newGraphSequence(ExecutionNode... executionNodes) {
        ExecutionGraph graph = ExecutionGraph.build();
        ExecutionNode previous = null;
        for (ExecutionNode executionNode : executionNodes) {
            if (previous == null) {
                graph.putEdge(null, executionNode);
            } else {
                graph.putEdge(previous, executionNode);
            }
            previous = executionNode;
        }
        return graph;
    }

    protected Consumer<MessageAndContext> assertMessageContains(Collection<Object> expected) {
        return event -> {
            Collection<Object> out = event.getMessage().payload();
            assertThat(out).isEqualTo(expected);
        };
    }

    protected Consumer<MessageAndContext> assertMessageContains(String expected) {
        return event -> {
            String out = event.getMessage().payload();
            assertThat(out).isEqualTo(expected);
        };
    }

    protected Consumer<MessageAndContext> assertMessageIsEmptyContent() {
        return event -> {
            Message message = event.getMessage();
            TypedContent<?,?> content = message.content();
            assertThat(content).isInstanceOf(EmptyContent.class);
            assertThat(content.data()).isNull();
        };
    }

    protected Consumer<MessageAndContext> assertMessageContainsOneOf(String... expected) {
        return event -> {
            String out = (String) event.getMessage().content().data();
            assertThat(expected).contains(out);
        };
    }

    protected static class NoActionResultMessageAndContext extends MessageAndContext {
        NoActionResultMessageAndContext(Message message) {
            super(message, DefaultFlowContext.from(message));
        }
    }

    protected static class ProcessorThrowingIllegalStateExceptionSync implements ProcessorSync {

        private final String errorMessage;

        public ProcessorThrowingIllegalStateExceptionSync(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        @Override
        public Message apply(FlowContext flowContext, Message message) {
            throw new IllegalStateException(errorMessage + " (" + message.payload() + ")");
        }
    }

    protected static class ProcessorThrowingNoClassDefFoundErrorSync implements ProcessorSync {

        private final String missingClazz;

        public ProcessorThrowingNoClassDefFoundErrorSync(String missingClazz) {
            this.missingClazz = missingClazz;
        }

        @Override
        public Message apply(FlowContext flowContext, Message message) {
            throw new NoClassDefFoundError(missingClazz);
        }
    }

    protected static class AddPostfixSyncProcessor implements ProcessorSync {

        private final String postfix;

        public AddPostfixSyncProcessor(String postfix) {
            this.postfix = postfix;
        }

        @Override
        public Message apply(FlowContext flowContext, Message message) {
            String inputString = (String) message.content().data();
            String outputString = inputString + postfix;
            return MessageBuilder.get().withText(outputString).build();
        }
    }
}
