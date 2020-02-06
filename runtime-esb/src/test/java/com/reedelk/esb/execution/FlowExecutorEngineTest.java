package com.reedelk.esb.execution;

import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.component.OnResult;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import org.junit.jupiter.api.Test;
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
        Message message = MessageBuilder.get().withText("Sample text").build();

        ExecutionNode postFixProcessor = newExecutionNode(new PostFixProcessor());
        ExecutionGraph graph = ExecutionGraph.build();
        graph.putEdge(null, inbound);
        graph.putEdge(inbound, postFixProcessor);
        graph.putEdge(postFixProcessor, stop);

        // When
        FlowExecutorEngine executorEngine = spy(new FlowExecutorEngine(graph));
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
        Message message = MessageBuilder.get().withText("Sample text").build();

        ExecutionNode processorThrowingException = newExecutionNode(new ProcessorThrowingException());
        ExecutionGraph graph = ExecutionGraph.build();
        graph.putEdge(null, inbound);
        graph.putEdge(inbound, processorThrowingException);
        graph.putEdge(processorThrowingException, stop);

        // When
        FlowExecutorEngine executorEngine = spy(new FlowExecutorEngine(graph));
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
        Message message = MessageBuilder.get().withText("Sample text").build();

        ExecutionGraph graph = ExecutionGraph.build();

        // When
        FlowExecutorEngine executorEngine = spy(new FlowExecutorEngine(graph));
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
            return MessageBuilder.get().withText(data + "-postfix").build();
        }
    }

    static class ProcessorThrowingException implements ProcessorSync {
        @Override
        public Message apply(FlowContext flowContext, Message message) {
            throw new ESBException("Error processing flow");
        }
    }
}