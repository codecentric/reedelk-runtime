package de.codecentric.reedelk.platform.flow;

import de.codecentric.reedelk.platform.test.utils.TestComponent;
import de.codecentric.reedelk.platform.execution.FlowExecutorEngine;
import de.codecentric.reedelk.platform.graph.ExecutionGraph;
import de.codecentric.reedelk.platform.graph.ExecutionNode;
import de.codecentric.reedelk.runtime.api.component.Component;
import de.codecentric.reedelk.runtime.api.component.Inbound;
import de.codecentric.reedelk.runtime.api.component.OnResult;
import de.codecentric.reedelk.runtime.api.exception.FlowExecutionException;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.api.message.content.TypedContent;
import de.codecentric.reedelk.runtime.api.message.content.TypedPublisher;
import de.codecentric.reedelk.runtime.component.Stop;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FlowTest {

    private final String flowId = UUID.randomUUID().toString();
    private final String flowTitle = "Test flow";
    private final long moduleId = 10L;
    private final String moduleName = "Test module";

    @Mock
    private Bundle bundle;
    @Mock
    private Inbound mockInbound;
    @Mock
    private BundleContext mockContext;
    @Mock
    private ExecutionNode mockExecutionNode;
    @Mock
    private ExecutionGraph mockExecutionGraph;
    @Mock
    private FlowExecutorEngine executionEngine;

    @BeforeEach
    void setUp() {
        doReturn(mockContext).when(bundle).getBundleContext();
        doReturn(Bundle.ACTIVE).when(bundle).getState();
    }

    @Test
    void shouldReturnCorrectFlowId() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);

        // When
        String actualFlowId = flow.getFlowId();

        // Then
        assertThat(actualFlowId).isEqualTo(flowId);
    }

    @Test
    void shouldReturnCorrectFlowTitle() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);

        // When
        Optional<String> actualFlowTitle = flow.getFlowTitle();

        // Then
        assertThat(actualFlowTitle).contains(flowTitle);
    }

    @Test
    void shouldReturnAbsentFlowTitle() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, null, mockExecutionGraph, executionEngine);

        // When
        Optional<String> actualFlowTitle = flow.getFlowTitle();

        // Then
        assertThat(actualFlowTitle).isNotPresent();
    }

    @Test
    void shouldIsUsingComponentThrowExceptionWhenTargetComponentIsNull() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);

        // Expect
        IllegalArgumentException illegalArgumentException = Assertions.assertThrows(IllegalArgumentException.class,
                () -> flow.isUsingComponent(null),
                "Expected isUsingComponent to throw, but it didn't");

        assertThat(illegalArgumentException).isNotNull();
        assertThat(illegalArgumentException.getMessage()).contains("Component Name");
    }

    @Test
    void shouldIsUsingComponentReturnFalse() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
        doReturn(Optional.empty()).when(mockExecutionGraph).findOne(ArgumentMatchers.any());

        // When
        boolean actualIsUsingComponent = flow.isUsingComponent("de.codecentric.reedelk.platform.my.target.Component");

        // Then
        assertThat(actualIsUsingComponent).isFalse();
    }

    @Test
    void shouldIsUsingComponentReturnTrue() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);

        TestComponent testComponent = new TestComponent();
        ServiceReference<Component> serviceReference = mock(ServiceReference.class);
        ExecutionNode EN = new ExecutionNode(new ExecutionNode.ReferencePair<>(testComponent, serviceReference));

        doReturn(Optional.of(EN)).when(mockExecutionGraph).findOne(ArgumentMatchers.any());

        // When
        boolean actualIsUsingComponent = flow.isUsingComponent("de.codecentric.reedelk.platform.my.target.Component");

        // Then
        assertThat(actualIsUsingComponent).isTrue();
    }

    @Test
    void shouldReleaseReferencesDelegateToGraphTheCorrectConsumer() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);

        // When
        flow.releaseReferences(bundle);

        // Then
        verify(mockExecutionGraph).applyOnNodes(any(ReleaseReferenceConsumer.class));
    }

    @Test
    void shouldFlowStartCallComponentOnStartAndAddInboundEventListener() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
        doReturn(mockExecutionNode).when(mockExecutionGraph).getRoot();
        doReturn(mockInbound).when(mockExecutionNode).getComponent();

        // When
        flow.start();

        // Then
        verify(mockInbound).onStart();
        verify(mockInbound).addEventListener(flow);
        assertThat(flow.isStarted()).isTrue();
    }

    @Test
    void shouldReturnIsStartedFalseWhenNotStarted() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);

        // When
        boolean actualIsStarted = flow.isStarted();

        // Then
        assertThat(actualIsStarted).isFalse();
    }

    @Test
    void shouldReturnIsStartedTrueWhenStarted() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
        doReturn(mockExecutionNode).when(mockExecutionGraph).getRoot();
        doReturn(mockInbound).when(mockExecutionNode).getComponent();
        flow.start();

        // When
        boolean actualIsStarted = flow.isStarted();

        // Then
        assertThat(actualIsStarted).isTrue();
    }

    @Test
    void shouldStopFlowCallComponentOnShutdownAndRemoveListener() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
        doReturn(mockExecutionNode).when(mockExecutionGraph).getRoot();
        doReturn(mockInbound).when(mockExecutionNode).getComponent();
        InOrder inOrderMockInbound = inOrder(mockInbound);
        flow.start();

        // When
        flow.stopIfStarted();

        // Then
        inOrderMockInbound.verify(mockInbound).onShutdown();
        inOrderMockInbound.verify(mockInbound).removeEventListener();
    }

    @Test
    void shouldStopFlowNotCallComponentOnShutdownIfAlreadyStarted() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
        doReturn(mockExecutionNode).when(mockExecutionGraph).getRoot();
        doReturn(mockInbound).when(mockExecutionNode).getComponent();

        // When
        flow.stopIfStarted();

        // Then
        verify(mockInbound, never()).onShutdown();
        verify(mockInbound, never()).removeEventListener();
    }

    @Test
    void shouldForceStopCallComponentOnShutdownAndRemoveListenerIfNotStarted() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
        doReturn(mockExecutionNode).when(mockExecutionGraph).getRoot();
        doReturn(mockInbound).when(mockExecutionNode).getComponent();

        // When
        flow.forceStop();

        // Then
        verify(mockInbound).onShutdown();
        verify(mockInbound).removeEventListener();
    }

    @Test
    void shouldForceStopCallComponentOnShutdownAndRemoveListenerIfStarted() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
        doReturn(mockExecutionNode).when(mockExecutionGraph).getRoot();
        doReturn(mockInbound).when(mockExecutionNode).getComponent();
        flow.start();

        // When
        flow.forceStop();

        // Then
        verify(mockInbound).onShutdown();
        verify(mockInbound).removeEventListener();
    }

    @Test
    void shouldFlowBeStartedEvenWhenEmpty() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
        doReturn(true).when(mockExecutionGraph).isEmpty();

        // When
        flow.start();

        // Then
        assertThat(flow.isStarted()).isTrue();
    }

    @Test
    void shouldForceStopDoNothingWhenFlowIsEmpty() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
        doReturn(true).when(mockExecutionGraph).isEmpty();

        // When
        flow.forceStop();

        // Then
        assertThat(flow.isStarted()).isFalse();
    }

    @Test
    void shouldStopIfStartedDoNothingWhenFlowIsEmpty() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
        doReturn(true).when(mockExecutionGraph).isEmpty();

        // When
        flow.stopIfStarted();

        // Then
        assertThat(flow.isStarted()).isFalse();
    }

    @Nested
    @DisplayName("OnEvent flow tests")
    class OnEventTests {

        @Test
        void shouldOnEventDelegateExecutionToExecutor() {
            // Given
            Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
            doReturn(mockExecutionNode).when(mockExecutionGraph).getRoot();
            doReturn(mockInbound).when(mockExecutionNode).getComponent();

            ExecutionNode stopEN = mock(ExecutionNode.class);
            Mockito.doReturn(new Stop()).when(stopEN).getComponent();

            Set<ExecutionNode> executionNodes = new HashSet<>();
            executionNodes.add(stopEN);
            doReturn(executionNodes).when(mockExecutionGraph).successors(mockExecutionNode);
            Message inMessage = MessageBuilder.get(TestComponent.class).withText("test").build();


            // When
            flow.onEvent(inMessage, new OnResult() {
                @Override
                public void onResult(FlowContext flowContext, Message actualMessage) {
                    // Then
                    assertThat(actualMessage).isEqualTo(inMessage);
                    verify(mockExecutionGraph).getRoot();
                    verify(mockExecutionGraph).successors(mockExecutionNode);
                    verify(executionEngine).onEvent(inMessage, this);
                }

                @Override
                public void onError(FlowContext flowContext, Throwable throwable) {
                    fail("Unexpected");
                }
            });
        }

        @Test
        void shouldOnEventWrapErrorResultWithExceptionWrapper() {
            // Given
            final String correlationId = UUID.randomUUID().toString();
            final PlatformException thrownException = new PlatformException("Error could not find file x.y.z");
            final Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
            final FlowContext mockFlowContext = mock(FlowContext.class);

            doReturn(correlationId).when(mockFlowContext).get("correlationId");
            doReturn(true).when(mockFlowContext).containsKey("correlationId");


            Message inMessage = MessageBuilder.get(TestComponent.class).withText("test").build();

            doAnswer(invocation -> {
                OnResult callback = invocation.getArgument(1);
                callback.onError(mockFlowContext, thrownException);
                return null;
            }).when(executionEngine).onEvent(eq(inMessage), any(OnResult.class));

            // Expect
            flow.onEvent(inMessage, new OnResult() {
                @Override
                public void onResult(FlowContext flowContext, Message message) {
                    fail("Expected on error callback to be called instead.");
                }

                @Override
                public void onError(FlowContext flowContext, Throwable throwable) {
                    assertThat(throwable).isInstanceOf(FlowExecutionException.class);
                    FlowExecutionException flowExecutionException = (FlowExecutionException) throwable;
                    assertThat(flowExecutionException.getModuleId()).isEqualTo(moduleId);
                    assertThat(flowExecutionException.getModuleName()).isEqualTo(moduleName);
                    assertThat(flowExecutionException.getFlowId()).isEqualTo(flowId);
                    assertThat(flowExecutionException.getFlowTitle()).isEqualTo(flowTitle);
                    assertThat(flowExecutionException.getCorrelationId()).isEqualTo(correlationId);
                    assertThat(flowExecutionException.getCause()).isEqualTo(thrownException);
                    assertThat(flowExecutionException.getMessage()).isEqualTo("{\n" +
                            "  \"flowTitle\": \"Test flow\",\n" +
                            "  \"errorType\": \"de.codecentric.reedelk.runtime.api.exception.PlatformException\",\n" +
                            "  \"moduleName\": \"Test module\",\n" +
                            "  \"errorMessage\": \"Error could not find file x.y.z\",\n" +
                            "  \"correlationId\": \"" + correlationId + "\",\n" +
                            "  \"moduleId\": 10,\n" +
                            "  \"flowId\": \"" + flowId + "\"\n" +
                            "}");
                }
            });
        }

        @Test
        void shouldOnEventDelegateResultOnSuccess() {
            // Given
            final Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
            final FlowContext mockFlowContext = mock(FlowContext.class);

            Message inMessage = MessageBuilder.get(TestComponent.class).withText("test").build();
            Message outMessage = MessageBuilder.get(TestComponent.class).withText("out").build();

            doAnswer(invocation -> {
                OnResult callback = invocation.getArgument(1);
                callback.onResult(mockFlowContext, outMessage);
                return null;
            }).when(executionEngine).onEvent(eq(inMessage), any(OnResult.class));

            // Expect
            flow.onEvent(inMessage, new OnResult() {
                @Override
                public void onResult(FlowContext flowContext, Message message) {
                    String payload = message.payload();
                    assertThat(payload).isEqualTo("out");
                }

                @Override
                public void onError(FlowContext flowContext, Throwable throwable) {
                    fail("Expected on result callback to be called instead.");
                }
            });
        }

        @Test
        void shouldOnEventDisposeContextAfterMessageIsConsumed() throws InterruptedException {
            // Given
            final Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
            final FlowContext mockFlowContext = mock(FlowContext.class);

            Message inMessage = MessageBuilder.get(TestComponent.class).withText("test").build();
            Message outMessage = MessageBuilder.get(TestComponent.class).withText("out").build();

            doAnswer(invocation -> {
                OnResult callback = invocation.getArgument(1);
                callback.onResult(mockFlowContext, outMessage);
                return null;
            }).when(executionEngine).onEvent(eq(inMessage), any(OnResult.class));

            CountDownLatch latch = new CountDownLatch(1);

            // Expect
            flow.onEvent(inMessage, new OnResult() {
                @Override
                public void onResult(FlowContext flowContext, Message message) {
                    verify(mockFlowContext, never()).dispose();
                    String payload = message.payload();
                    assertThat(payload).isEqualTo("out");
                    latch.countDown();
                }

                @Override
                public void onError(FlowContext flowContext, Throwable throwable) {
                    latch.countDown();
                    fail("Expected on result callback to be called instead.");
                }
            });

            latch.await();
            verify(mockFlowContext).dispose();
        }

        @Test
        void shouldOnEventDisposeContextAfterMessageStreamIsConsumed() throws InterruptedException {
            // Given
            final Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
            final FlowContext mockFlowContext = mock(FlowContext.class);

            Message inMessage = MessageBuilder.get(TestComponent.class).withText("test").build();
            Message outMessage = MessageBuilder.get(TestComponent.class).withText("out").build();

            doAnswer(invocation -> {
                OnResult callback = invocation.getArgument(1);
                callback.onResult(mockFlowContext, outMessage);
                return null;
            }).when(executionEngine).onEvent(eq(inMessage), any(OnResult.class));

            CountDownLatch latch = new CountDownLatch(1);

            // Expect
            flow.onEvent(inMessage, new OnResult() {
                @Override
                public void onResult(FlowContext flowContext, Message message) {
                    verify(mockFlowContext, never()).dispose();
                    TypedContent<String, String> content = message.content();
                    TypedPublisher<String> stream = content.stream();
                    List<String> result = Flux.from(stream).collectList().block();
                    assertThat(result).containsOnly("out");
                    latch.countDown();
                }

                @Override
                public void onError(FlowContext flowContext, Throwable throwable) {
                    latch.countDown();
                    fail("Expected on result callback to be called instead.");
                }
            });

            latch.await();
            verify(mockFlowContext).dispose();
        }

        @Test
        void shouldOnEventDisposeContextWhenStreamThrowsError() throws InterruptedException {
            // Given
            final Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
            final FlowContext mockFlowContext = mock(FlowContext.class);

            Message inMessage = MessageBuilder.get(TestComponent.class).withText("test").build();

            Flux<String> stringFlux = Flux.create(new Consumer<FluxSink<String>>() {
                int count = 0;
                @Override
                public void accept(FluxSink<String> sink) {
                    while (count < 2) {
                        sink.next(String.valueOf(count));
                        count++;
                    }
                    sink.error(new PlatformException("Error completing the stream!"));
                }
            });

            Message outMessage = MessageBuilder.get(TestComponent.class)
                    .withTypedPublisher(TypedPublisher.fromString(stringFlux)).build();

            doAnswer(invocation -> {
                OnResult callback = invocation.getArgument(1);
                callback.onResult(mockFlowContext, outMessage);
                return null;
            }).when(executionEngine).onEvent(eq(inMessage), any(OnResult.class));

            // Expect
            flow.onEvent(inMessage, new OnResult() {
                @Override
                public void onResult(FlowContext flowContext, Message message) {
                    verify(mockFlowContext, never()).dispose();
                    TypedContent<String, String> content = message.content();
                    TypedPublisher<String> stream = content.stream();
                    try {
                        Flux.from(stream).collectList().block();
                    } catch (Exception e) {
                        // The stream throws an exception. We want to make sure that when the stream
                        // terminated with an error, the context was correctly disposed.
                        verify(mockFlowContext).dispose();
                    }
                }

                @Override
                public void onError(FlowContext flowContext, Throwable throwable) {
                    fail("Expected on result callback to be called instead.");
                }
            });
        }

        @Test
        void shouldOnEventDisposeContextWhenMessagePayloadIsNotConsumed() throws InterruptedException {
            // Given
            final Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
            final FlowContext mockFlowContext = mock(FlowContext.class);

            Message inMessage = MessageBuilder.get(TestComponent.class).withText("test").build();
            Message outMessage = MessageBuilder.get(TestComponent.class).withText("out").build();

            doAnswer(invocation -> {
                OnResult callback = invocation.getArgument(1);
                callback.onResult(mockFlowContext, outMessage);
                return null;
            }).when(executionEngine).onEvent(eq(inMessage), any(OnResult.class));

            CountDownLatch latch = new CountDownLatch(1);

            // Expect
            flow.onEvent(inMessage, new OnResult() {
                @Override
                public void onResult(FlowContext flowContext, Message message) {
                    // We don't consume the message.
                    verify(mockFlowContext, never()).dispose();
                    latch.countDown();
                }

                @Override
                public void onError(FlowContext flowContext, Throwable throwable) {
                    latch.countDown();
                    fail("Expected on result callback to be called instead.");
                }
            });

            latch.await();
            verify(mockFlowContext).dispose();
        }

        @Test
        void shouldOnEventDisposeContextWhenOnError() throws InterruptedException {
            // Given
            final Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
            final FlowContext mockFlowContext = mock(FlowContext.class);

            Message inMessage = MessageBuilder.get(TestComponent.class).withText("test").build();

            doAnswer(invocation -> {
                OnResult callback = invocation.getArgument(1);
                callback.onError(mockFlowContext, new PlatformException("An exception!"));
                return null;
            }).when(executionEngine).onEvent(eq(inMessage), any(OnResult.class));

            CountDownLatch latch = new CountDownLatch(1);

            // Expect
            flow.onEvent(inMessage, new OnResult() {
                @Override
                public void onResult(FlowContext flowContext, Message message) {
                    // We don't consume the message.
                    latch.countDown();
                    fail("Expected on result callback to be called instead.");
                }

                @Override
                public void onError(FlowContext flowContext, Throwable throwable) {
                    verify(mockFlowContext, never()).dispose();
                    latch.countDown();
                }
            });

            latch.await();
            verify(mockFlowContext).dispose();
        }

        @Test
        void shouldOnEventDisposeContextWhenCalledWithoutMessage() throws InterruptedException {
            // Given
            final Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
            final FlowContext mockFlowContext = mock(FlowContext.class);

            Message inMessage = MessageBuilder.get(TestComponent.class).withText("test").build();
            Message outMessage = MessageBuilder.get(TestComponent.class).withText("out").build();

            CountDownLatch latch = new CountDownLatch(1);

            doAnswer(invocation -> {
                OnResult callback = invocation.getArgument(1);
                callback.onResult(mockFlowContext, outMessage);
                latch.countDown();
                return null;
            }).when(executionEngine).onEvent(eq(inMessage), any(OnResult.class));

            // Expect
            flow.onEvent(inMessage);

            latch.await();
            verify(mockFlowContext).dispose();
        }
    }
}
