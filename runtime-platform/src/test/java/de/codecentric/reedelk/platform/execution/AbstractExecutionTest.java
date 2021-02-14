package de.codecentric.reedelk.platform.execution;

import de.codecentric.reedelk.platform.execution.context.DefaultFlowContext;
import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.platform.test.utils.TestComponent;
import de.codecentric.reedelk.platform.test.utils.TestInboundComponent;
import de.codecentric.reedelk.runtime.api.component.Component;
import de.codecentric.reedelk.runtime.api.component.Join;
import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.runtime.api.message.content.TypedPublisher;
import de.codecentric.reedelk.runtime.component.Stop;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractExecutionTest {

    protected ExecutionNode inbound = newExecutionNode(new TestInboundComponent());
    protected ExecutionNode stop = newExecutionNode(new Stop());

    public static ExecutionNode newExecutionNode(Component component) {
        return new ExecutionNode(new ExecutionNode.ReferencePair<>(component));
    }

    protected MessageAndContext newEventWithContent(String content) {
        Message message = MessageBuilder.get(TestComponent.class).withText(content).build();
        return new NoActionResultMessageAndContext(message);
    }

    protected MessageAndContext newEventWithContent(TypedPublisher<String> stream) {
        Message message = MessageBuilder.get(TestComponent.class).withTypedPublisher(stream, MimeType.TEXT_PLAIN).build();
        return new NoActionResultMessageAndContext(message);
    }

    protected MessageAndContext newEventWithContent(Object content) {
        Message message = MessageBuilder.get(TestComponent.class).withJavaObject(content).build();
        return new NoActionResultMessageAndContext(message);
    }

    protected <T> MessageAndContext newEventWithContent(List<T> list, Class<T> listItemType) {
        Message message = MessageBuilder.get(TestComponent.class).withList(list, listItemType).build();
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

    protected Consumer<MessageAndContext> assertMessageContainsItems(Object ...expected) {
        return event -> {
            Collection<Object> out = event.getMessage().payload();
            assertThat(out).containsExactly(expected);
        };
    }

    protected Consumer<MessageAndContext> assertMessageContainsInAnyOrder(Object ...expected) {
        return event -> {
            Collection<Object> out = event.getMessage().payload();
            assertThat(out).containsExactlyInAnyOrder(expected);
        };
    }

    protected Consumer<MessageAndContext> assertMessageContains(String expected) {
        return event -> {
            String out = event.getMessage().payload();
            assertThat(out).isEqualTo(expected);
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

    public static class AddPostfixSyncProcessor implements ProcessorSync {

        private final String postfix;

        public AddPostfixSyncProcessor(String postfix) {
            this.postfix = postfix;
        }

        @Override
        public Message apply(FlowContext flowContext, Message message) {
            String inputString = (String) message.content().data();
            String outputString = inputString + postfix;
            return MessageBuilder.get(TestComponent.class).withText(outputString).build();
        }
    }

    public static class JoinStringWithDelimiter implements Join {

        private final String delimiter;

        public JoinStringWithDelimiter(String delimiter) {
            this.delimiter = delimiter;
        }

        @Override
        public Message apply(FlowContext flowContext, List<Message> messages) {
            String joined = messages.stream()
                    .map(message -> (String) message.content().data())
                    .collect(joining(delimiter));
            return MessageBuilder.get(TestComponent.class).withText(joined).build();
        }
    }

    public static class ToStringProcessor implements ProcessorSync {
        @Override
        public Message apply(FlowContext flowContext, Message message) {
            String outputString = message.payload().toString();
            return MessageBuilder.get(TestComponent.class).withText(outputString).build();
        }
    }
}
