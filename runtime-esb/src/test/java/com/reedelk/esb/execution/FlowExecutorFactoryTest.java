package com.reedelk.esb.execution;

import com.reedelk.esb.component.ForkWrapper;
import com.reedelk.esb.component.RouterWrapper;
import com.reedelk.runtime.api.component.Component;
import com.reedelk.runtime.api.component.OnResult;
import com.reedelk.runtime.api.component.ProcessorAsync;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.message.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.component.Stop;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FlowExecutorFactoryTest {

    @Test
    void shouldGetComponentBuilderOrThrowReturnCorrectlyForProcessorSync() {
        // Given
        Component component = new TestProcessorSync();

        // Expect
        assertBuilderForTargetClassIs(component, ProcessorSyncExecutor.class);
    }

    @Test
    void shouldGetComponentBuilderOrThrowReturnCorrectlyForProcessorAsync() {
        // Given
        Component component = new TestProcessorAsync();

        // Expect
        assertBuilderForTargetClassIs(component, ProcessorAsyncExecutor.class);
    }

    @Test
    void shouldGetComponentBuilderOrThrowReturnCorrectlyForStop() {
        // Given
        Component component = new Stop();

        // Expect
        assertBuilderForTargetClassIs(component, StopExecutor.class);
    }

    @Test
    void shouldGetComponentBuilderOrThrowReturnCorrectlyForFork() {
        // Given
        Component component = new ForkWrapper();

        // Expect
        assertBuilderForTargetClassIs(component, ForkExecutor.class);
    }

    @Test
    void shouldGetComponentBuilderOrThrowReturnCorrectlyForRouter() {
        // Given
        Component component = new RouterWrapper();

        // Expect
        assertBuilderForTargetClassIs(component, RouterExecutor.class);
    }

    @Test
    void shouldGetComponentBuilderOrThrowThrowExceptionWhenComponentDoesNotImplementKnownInterface() {
        // Given
        Component component = new UndefinedComponentType();

        Assertions.assertThrows(IllegalStateException.class, () ->
                FlowExecutorFactory.get().executorOf(component));
    }

    private void assertBuilderForTargetClassIs(Component component, Class<? extends FlowExecutor> builderClass) {
        // When
        FlowExecutor builder = FlowExecutorFactory.get()
                .executorOf(component);

        // Expect
        assertThat(builder).isNotNull();
        assertThat(builder).isInstanceOf(builderClass);
    }

    interface NotRelatedInterface {
    }

    private class UndefinedComponentType implements Component {

    }

    class TestProcessorAsync implements ProcessorAsync, NotRelatedInterface {
        @Override
        public void apply(Message input, FlowContext flowContext, OnResult callback) {

        }
    }

    class TestProcessorSync implements ProcessorSync, NotRelatedInterface {
        @Override
        public Message apply(Message message, FlowContext flowContext) {
            return null;
        }
    }
}