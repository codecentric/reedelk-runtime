package de.codecentric.reedelk.platform.execution;

import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.platform.test.utils.TestComponent;
import de.codecentric.reedelk.runtime.api.component.OnResult;
import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class FlowExecutorEngineTest extends AbstractExecutionTest {

    @Test
    void shouldCorrectlyExecuteFlowAndInvokeOnResultWhenDoneProcessing() throws InterruptedException {
        // Given
        Message message = MessageBuilder.get(TestComponent.class).withText("Sample text").build();

        ExecutionNode postFixProcessor = newExecutionNode(new PostFixProcessor());
        ExecutionGraph graph = ExecutionGraph.build();
        graph.putEdge(null, inbound);
        graph.putEdge(inbound, postFixProcessor);
        graph.putEdge(postFixProcessor, stop);

        // When
        FlowExecutorEngine executorEngine = Mockito.spy(new FlowExecutorEngine(graph));
        doReturn(Schedulers.elastic()).when(executorEngine).scheduler();

        CountDownLatch latch = new CountDownLatch(1);

        executorEngine.onEvent(message, new OnResult() {
            @Override
            public void onResult(FlowContext flowContext, Message message) {
                latch.countDown();
                String result = message.payload();
                assertThat(result).isEqualTo("Sample text-postfix");
            }

            @Override
            public void onError(FlowContext flowContext, Throwable throwable) {
                latch.countDown();
                fail("Not supposed to be called", throwable);
            }
        });

        latch.await();
    }

    @Test
    void shouldCorrectlyExecuteFlowAndInvokeOnErrorWhenProcessorThrowsException() throws InterruptedException {
        // Given
        Message message = MessageBuilder.get(TestComponent.class).withText("Sample text").build();

        ExecutionNode processorThrowingException = newExecutionNode(new ProcessorThrowingException());
        ExecutionGraph graph = ExecutionGraph.build();
        graph.putEdge(null, inbound);
        graph.putEdge(inbound, processorThrowingException);
        graph.putEdge(processorThrowingException, stop);

        // When
        FlowExecutorEngine executorEngine = Mockito.spy(new FlowExecutorEngine(graph));
        doReturn(Schedulers.elastic()).when(executorEngine).scheduler();

        CountDownLatch latch = new CountDownLatch(1);

        executorEngine.onEvent(message, new OnResult() {
            @Override
            public void onResult(FlowContext flowContext, Message message) {
                latch.countDown();
                fail("Not supposed to be called");
            }

            @Override
            public void onError(FlowContext flowContext, Throwable throwable) {
                latch.countDown();
                assertThat(throwable).hasMessage("Error processing flow");
            }
        });

        latch.await();
    }

    @Test
    void shouldCallOnErrorWhenExecutionGraphIsNotCorrect() throws InterruptedException {
        // Given
        Message message = MessageBuilder.get(TestComponent.class).withText("Sample text").build();

        ExecutionGraph graph = ExecutionGraph.build();

        // When
        FlowExecutorEngine executorEngine = Mockito.spy(new FlowExecutorEngine(graph));
        doReturn(Schedulers.elastic()).when(executorEngine).scheduler();

        CountDownLatch latch = new CountDownLatch(1);

        executorEngine.onEvent(message, new OnResult() {
            @Override
            public void onResult(FlowContext flowContext, Message message) {
                latch.countDown();
                fail("Not supposed to be called");
            }

            @Override
            public void onError(FlowContext flowContext, Throwable throwable) {
                latch.countDown();
                throwable.printStackTrace();
                assertThat(throwable).hasMessage("could not determine successors of null execution node");
            }
        });

        latch.await();
    }

    static class PostFixProcessor implements ProcessorSync {
        @Override
        public Message apply(FlowContext flowContext, Message message) {
            String data = message.payload();
            return MessageBuilder.get(TestComponent.class).withText(data + "-postfix").build();
        }
    }

    static class ProcessorThrowingException implements ProcessorSync {
        @Override
        public Message apply(FlowContext flowContext, Message message) {
            throw new PlatformException("Error processing flow");
        }
    }
}
