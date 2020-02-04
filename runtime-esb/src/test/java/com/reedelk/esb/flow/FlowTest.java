package com.reedelk.esb.flow;

import com.reedelk.esb.execution.FlowExecutorEngine;
import com.reedelk.esb.graph.ExecutionGraph;
import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.esb.test.utils.TestComponent;
import com.reedelk.runtime.api.component.Component;
import com.reedelk.runtime.api.component.Inbound;
import com.reedelk.runtime.api.component.OnResult;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.exception.FlowExecutionException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.component.Stop;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
        boolean actualIsUsingComponent = flow.isUsingComponent("com.reedelk.esb.my.target.Component");

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
        boolean actualIsUsingComponent = flow.isUsingComponent("com.reedelk.esb.my.target.Component");

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
        InOrder inOrderMockInbound = Mockito.inOrder(mockInbound);
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
    void shouldOnEventDelegateExecutionToExecutor() {
        // Given
        Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
        doReturn(mockExecutionNode).when(mockExecutionGraph).getRoot();
        doReturn(mockInbound).when(mockExecutionNode).getComponent();

        ExecutionNode stopEN = mock(ExecutionNode.class);
        doReturn(new Stop()).when(stopEN).getComponent();

        Set<ExecutionNode> executionNodes = new HashSet<>();
        executionNodes.add(stopEN);
        doReturn(executionNodes).when(mockExecutionGraph).successors(mockExecutionNode);
        Message inMessage = MessageBuilder.get().withText("test").build();


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
        final ESBException thrownException = new ESBException("Error could not find file x.y.z");
        final Flow flow = new Flow(moduleId, moduleName, flowId, flowTitle, mockExecutionGraph, executionEngine);
        final FlowContext mockFlowContext = mock(FlowContext.class);

        doReturn(correlationId).when(mockFlowContext).get("correlationId");
        doReturn(true).when(mockFlowContext).containsKey("correlationId");


        Message inMessage = MessageBuilder.get().withText("test").build();

        Mockito.doAnswer(invocation -> {
            OnResult callback = invocation.getArgument(1);
            callback.onError(mockFlowContext, thrownException);
            return null;
        }).when(executionEngine).onEvent(Mockito.eq(inMessage), Mockito.any(OnResult.class));

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
                        "  \"errorType\": \"com.reedelk.runtime.api.exception.ESBException\",\n" +
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

        Message inMessage = MessageBuilder.get().withText("test").build();
        Message outMessage = MessageBuilder.get().withText("out").build();

        Mockito.doAnswer(invocation -> {
            OnResult callback = invocation.getArgument(1);
            callback.onResult(mockFlowContext, outMessage);
            return null;
        }).when(executionEngine).onEvent(Mockito.eq(inMessage), Mockito.any(OnResult.class));

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
}