package com.reedelk.esb.execution.context;

import com.reedelk.esb.test.utils.TestComponent;
import com.reedelk.runtime.api.flow.Disposable;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;

import static com.reedelk.runtime.api.commons.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultFlowContextTest {

    @Mock
    private Disposable disposable1;
    @Mock
    private Disposable disposable2;

    @Test
    void shouldCreateContextWithNewCorrelationIdIfDoesNotExistInMessageAttributes() {
        // Given
        Message message = MessageBuilder.get().empty().build();

        // When
        DefaultFlowContext context = DefaultFlowContext.from(message);

        // Then
        assertThat((String) context.get("correlationId")).isNotBlank();
    }

    @Test
    void shouldCreateContextWithCorrelationIdFromGivenMessageAttributes() {
        // Given
        String expectedCorrelationId = "aabbcc";
        MessageAttributes attributes = new DefaultMessageAttributes(TestComponent.class,
                of(MessageAttributeKey.CORRELATION_ID, expectedCorrelationId));

        Message message = MessageBuilder.get().attributes(attributes).empty().build();

        // When
        DefaultFlowContext context = DefaultFlowContext.from(message);

        // Then
        assertThat((String) context.get("correlationId")).isEqualTo(expectedCorrelationId);
    }

    @Test
    void shouldCorrectlyDisposeAllRegisteredDisposableObjects() {
        // Given
        Message message = MessageBuilder.get().empty().build();
        DefaultFlowContext context = DefaultFlowContext.from(message);
        context.register(disposable1);
        context.register(disposable2);

        // When
        context.dispose();

        // Then
        verify(disposable1).dispose();
        verify(disposable2).dispose();
        verifyNoMoreInteractions(disposable1, disposable2);

        assertDisposablesAreEmpty(context);
    }

    @Test
    void shouldDisposeAllObjectsEvenWhenOneThrowsException() {
        // Given
        Message message = MessageBuilder.get().empty().build();
        DefaultFlowContext context = DefaultFlowContext.from(message);

        Disposable disposable3 = mock(Disposable.class);

        context.register(disposable1);
        context.register(disposable3);
        context.register(disposable2);

        doThrow(new IllegalStateException("Some illegal state"))
                .when(disposable3).dispose();

        // When
        context.dispose();

        // Then
        verify(disposable1).dispose();
        verify(disposable3).dispose();
        verify(disposable2).dispose();
        verifyNoMoreInteractions(disposable1, disposable2, disposable3);

        assertDisposablesAreEmpty(context);
    }

    @Test
    void shouldNotThrowExceptionWhenValueIsNull() {
        // Given
        Message message = MessageBuilder.get().empty().build();
        DefaultFlowContext context = DefaultFlowContext.from(message);

        // Expect
        try {
            context.put("myKey", null);
        } catch (Exception e) {
            Assertions.fail("Should have not thrown exception.", e);
        }
    }

    @Test
    void shouldAddKeyAndValueCorrectly() {
        // Given
        Message message = MessageBuilder.get().empty().build();
        DefaultFlowContext context = DefaultFlowContext.from(message);

        context.put("myKey", "This is a test");

        assertThat(context).containsEntry("myKey", "This is a test");
    }

    @Test
    void shouldThrowExceptionWhenKeyIsNull() {
        // Given
        Message message = MessageBuilder.get().empty().build();
        DefaultFlowContext context = DefaultFlowContext.from(message);

        IllegalArgumentException thrown =
                assertThrows(IllegalArgumentException.class, () -> context.put(null, "my value"));

        assertThat(thrown).hasMessage("flow context key must not be empty");
    }

    @Test
    void shouldThrowExceptionWhenKeyIsEmpty() {
        // Given
        Message message = MessageBuilder.get().empty().build();
        DefaultFlowContext context = DefaultFlowContext.from(message);

        IllegalArgumentException thrown =
                assertThrows(IllegalArgumentException.class, () -> context.put(" ", "my value"));

        assertThat(thrown).hasMessage("flow context key must not be empty");
    }

    @Test
    void shouldReturnTrueWhenContextContainsKey() {
        // Given
        Message message = MessageBuilder.get().empty().build();
        DefaultFlowContext context = DefaultFlowContext.from(message);

        context.put("myKey", "my value");

        // When
        boolean actual = context.contains("myKey");

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void shouldReturnFalseWhenContextDoesNotContainKey() {
        // Given
        Message message = MessageBuilder.get().empty().build();
        DefaultFlowContext context = DefaultFlowContext.from(message);

        // When
        boolean actual = context.contains("myKey");

        // Then
        assertThat(actual).isFalse();
    }

    @SuppressWarnings("unchecked")
    private void assertDisposablesAreEmpty(FlowContext context) {
        try {
            Field disposableList = context.getClass().getDeclaredField("disposableList");
            disposableList.setAccessible(true);
            List<Disposable> disposables = (List<Disposable>) disposableList.get(context);
            assertThat(disposables).isEmpty();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Could not verify disposables empty", e);
        }
    }
}
