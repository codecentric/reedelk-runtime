package de.codecentric.reedelk.platform.services.scriptengine.evaluator;

import de.codecentric.reedelk.runtime.api.script.dynamicvalue.*;
import de.codecentric.reedelk.platform.test.utils.MyTestAttributes;
import de.codecentric.reedelk.platform.test.utils.TestComponent;
import de.codecentric.reedelk.runtime.api.commons.ModuleContext;
import de.codecentric.reedelk.runtime.api.commons.StackTraceUtils;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageAttributes;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.runtime.api.message.content.TypedPublisher;
import de.codecentric.reedelk.runtime.api.commons.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Map;

import static de.codecentric.reedelk.runtime.api.commons.ImmutableMap.of;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DynamicValueStreamEvaluatorTest {

    private final long moduleId = 10L;
    private final ModuleContext moduleContext = new ModuleContext(moduleId);

    @Mock
    private FlowContext context;

    private DynamicValueStreamEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new DynamicValueStreamEvaluator();
    }

    @Nested
    @DisplayName("Evaluate dynamic string value with message and context")
    class EvaluateDynamicStringValueWithMessageAndContext {

        @Test
        void shouldCorrectlyEvaluateMessageAttributeProperty() {
            // Given
            class MyAttributes extends MessageAttributes {
                MyAttributes() {
                    put("property1", "test1");
                }
            }

            Message message = MessageBuilder.get(TestComponent.class).withText("this is a test").attributes(new MyAttributes()).build();
            DynamicString dynamicString = DynamicString.from("#[message.attributes.pRoperty1]", moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext("test1")
                    .verifyComplete();
        }

        @Test
        void shouldCorrectlyEvaluateTextPayload() {
            // Given
            Message message = MessageBuilder.get(TestComponent.class).withText("this is a test").build();
            DynamicString dynamicString = DynamicString.from("#[message.payload()]", moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext("this is a test")
                    .verifyComplete();
        }

        @Test
        void shouldCorrectlyEvaluateStreamPayload() {
            // Given
            Flux<String> textStream = Flux.just("one", "two");
            Message message = MessageBuilder.get(TestComponent.class).withText(textStream).build();

            DynamicString dynamicString = DynamicString.from("#[message.payload()]", moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext("one")
                    .expectNext("two")
                    .verifyComplete();
        }

        // Here the original stream MUST be consumed
        // in order to evaluate the script.
        @Test
        void shouldCorrectlyConcatenateStreamWithString() {
            // Given
            Flux<String> content = Flux.just("Hello", ", this", " is", " just", " a");
            Message message = MessageBuilder.get(TestComponent.class).withText(content).build();

            DynamicString dynamicString = DynamicString.from("#[message.content.data() + ' test.']", moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext("Hello, this is just a test.")
                    .verifyComplete();
        }

        @Test
        void shouldCorrectlyConcatenateWithString() {
            // Given
            String payload = "Hello, this is just a";
            Message message = MessageBuilder.get(TestComponent.class).withText(payload).build();

            DynamicString dynamicString = DynamicString.from("#[message.content.data() + ' test.']", moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext("Hello, this is just a test.")
                    .verifyComplete();
        }

        @Test
        void shouldCorrectlyEvaluateString() {
            // Given
            Message message = MessageBuilder.get(TestComponent.class).withText("test").build();

            DynamicString dynamicString = DynamicString.from("#['evaluation test']", moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext("evaluation test")
                    .verifyComplete();
        }

        @Test
        void shouldReturnTextFromDynamicValue() {
            // Given
            Message message = MessageBuilder.get(TestComponent.class).withText("test").build();
            DynamicString dynamicString = DynamicString.from("Expected text", moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext("Expected text")
                    .verifyComplete();
        }

        @Test
        void shouldReturnEmptyString() {
            // Given
            Message message = MessageBuilder.get(TestComponent.class).withText("test").build();
            DynamicString dynamicString = DynamicString.from("", moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext("")
                    .verifyComplete();
        }

        @Test
        void shouldResultBeNullWhenDynamicValueIsNull() {
            // Given
            Message message = MessageBuilder.get(TestComponent.class).withText("test").build();
            DynamicString dynamicString = null;

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, message);

            // Then
            assertThat(publisher).isNull();
        }

        @Test
        void shouldResultNotBePresentWhenDynamicValueScriptIsEmpty() {
            // Given
            Message message = MessageBuilder.get(TestComponent.class).withText("test").build();
            DynamicString dynamicString = DynamicString.from("#[]", moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, message);

            // Then
            StepVerifier.create(publisher)
                    .verifyComplete();
        }

        @Test
        void shouldResultNotBePresentWhenDynamicValueStringIsNull() {
            // Given
            Message message = MessageBuilder.get(TestComponent.class).withText("test").build();
            DynamicString dynamicString = DynamicString.from(null, moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, message);

            // Then
            StepVerifier.create(publisher)
                    .verifyComplete();
        }

        @Test
        void shouldCorrectlyEvaluateInteger() {
            // Given
            Message message = MessageBuilder.get(TestComponent.class).withJavaObject(23432).build();
            DynamicString dynamicString = DynamicString.from("#[message.payload()]", moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext("23432")
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Evaluate dynamic integer value with message and context")
    class EvaluateDynamicIntegerValueWithMessageAndContext {

        @Test
        void shouldCorrectlyEvaluateInteger() {
            // Given
            Message message = MessageBuilder.get(TestComponent.class).withText("test").build();
            DynamicInteger dynamicInteger = DynamicInteger.from("#[506]", moduleContext);

            // When
            TypedPublisher<Integer> publisher = evaluator.evaluateStream(dynamicInteger, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext(506)
                    .verifyComplete();
        }

        @Test
        void shouldCorrectlySumNumber() {
            // Given
            Message message = MessageBuilder.get(TestComponent.class).withText("12").build();
            DynamicInteger dynamicInteger = DynamicInteger.from("#[message.payload().toInteger() + 10]", moduleContext);

            // When
            TypedPublisher<Integer> publisher = evaluator.evaluateStream(dynamicInteger, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext(22)
                    .verifyComplete();
        }

        @Test
        void shouldCorrectlyEvaluateIntegerFromText() {
            // Given
            Message message = MessageBuilder.get(TestComponent.class).withText("test").build();
            DynamicInteger dynamicInteger = DynamicInteger.from(53, moduleContext);

            // When
            TypedPublisher<Integer> publisher = evaluator.evaluateStream(dynamicInteger, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext(53)
                    .verifyComplete();
        }

        @Test
        void shouldCorrectlyEvaluateIntegerFromMessagePayload() {
            // Given
            Message message = MessageBuilder.get(TestComponent.class).withJavaObject(120).build();
            DynamicInteger dynamicInteger = DynamicInteger.from("#[message.payload()]", moduleContext);

            // When
            TypedPublisher<Integer> publisher = evaluator.evaluateStream(dynamicInteger, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext(120)
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Evaluate dynamic boolean value with message and context")
    class EvaluateDynamicBooleanValueWithMessageAndContext {

        @Test
        void shouldCorrectlyEvaluateBoolean() {
            // Given
            Message message = MessageBuilder.get(TestComponent.class).withText("a test").build();
            DynamicBoolean dynamicBoolean = DynamicBoolean.from("#[1 == 1]", moduleContext);

            // When
            TypedPublisher<Boolean> publisher = evaluator.evaluateStream(dynamicBoolean, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext(true)
                    .verifyComplete();
        }

        @Test
        void shouldCorrectlyEvaluateBooleanFromPayload() {
            // Given
            Message message = MessageBuilder.get(TestComponent.class).withText("true").build();
            DynamicBoolean dynamicBoolean = DynamicBoolean.from("#[message.payload()]", moduleContext);

            // When
            TypedPublisher<Boolean> publisher = evaluator.evaluateStream(dynamicBoolean, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext(true)
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Evaluate dynamic byte array value with message and context")
    class EvaluateDynamicByteArrayWithMessageAndContext {

        @Test
        void shouldCorrectlyEvaluateByteArrayFromPayload() {
            // Given
            String payload = "My sample payload";
            Message message = MessageBuilder.get(TestComponent.class).withText(payload).build();
            DynamicByteArray dynamicByteArray = DynamicByteArray.from("#[message.payload()]", moduleContext);

            // When
            TypedPublisher<byte[]> publisher = evaluator.evaluateStream(dynamicByteArray, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNextMatches(bytes -> Arrays.equals(bytes, payload.getBytes()))
                    .verifyComplete();
        }

        @Test
        void shouldCorrectlyEvaluateByteArrayFromPayloadByteArrayStream() {
            // Given
            Flux<byte[]> stream = Flux.just("one".getBytes(), "two".getBytes());
            Message message = MessageBuilder.get(TestComponent.class).withBinary(stream, MimeType.TEXT_PLAIN).build();
            DynamicByteArray dynamicByteArray = DynamicByteArray.from("#[message.payload()]", moduleContext);

            // When
            TypedPublisher<byte[]> publisher = evaluator.evaluateStream(dynamicByteArray, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNextMatches(bytes -> Arrays.equals(bytes, "one".getBytes()))
                    .expectNextMatches(bytes -> Arrays.equals(bytes, "two".getBytes()))
                    .verifyComplete();
        }

        @Test
        void shouldCorrectlyEvaluateByteArrayFromPayloadStringStream() {
            // Given
            Flux<String> stream =  Flux.just("one","two");
            Message message = MessageBuilder.get(TestComponent.class).withText(stream).build();
            DynamicByteArray dynamicByteArray = DynamicByteArray.from("#[message.payload()]", moduleContext);

            // When
            TypedPublisher<byte[]> publisher = evaluator.evaluateStream(dynamicByteArray, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNextMatches(bytes -> Arrays.equals(bytes, "one".getBytes()))
                    .expectNextMatches(bytes -> Arrays.equals(bytes, "two".getBytes()))
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Evaluate dynamic object value with message and context")
    class EvaluateDynamicObjectValueWithMessageAndContext {

        @Test
        void shouldCorrectlyEvaluateDynamicObject() {
            // Given
            Flux<String> content = Flux.just("Hello", ", this", " is", " just", " a");
            Message message = MessageBuilder.get(TestComponent.class).withText(content).build();

            DynamicObject dynamicObject = DynamicObject.from("#[message.content]", moduleContext);

            // When
            TypedPublisher<Object> publisher = evaluator.evaluateStream(dynamicObject, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext(message.content())
                    .verifyComplete();
        }

        @Test
        void shouldCorrectlyEvaluateMessage() {
            // Given
            Message message = MessageBuilder.get(TestComponent.class).withText("test").build();
            DynamicObject dynamicString = DynamicObject.from("#[message]", moduleContext);

            // When
            TypedPublisher<Object> publisher = evaluator.evaluateStream(dynamicString, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext(message)
                    .verifyComplete();
        }

        @Test
        void shouldCorrectlyEvaluateMessagePayload() {
            // Given
            MyObject given = new MyObject();
            Message message = MessageBuilder.get(TestComponent.class).withJavaObject(given).build();
            DynamicObject dynamicString = DynamicObject.from("#[message.payload()]", moduleContext);

            // When
            TypedPublisher<Object> publisher = evaluator.evaluateStream(dynamicString, context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext(given)
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Evaluate dynamic string with throwable and context")
    class EvaluateDynamicStringWithThrowableAndContext {

        @Test
        void shouldCorrectlyEvaluateErrorPayload() {
            // Given
            Throwable myException = new PlatformException("Test error");
            DynamicString dynamicString = DynamicString.from("#[error]", moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, myException);

            // Then
            StepVerifier.create(publisher)
                    .expectNext(StackTraceUtils.asString(myException))
                    .verifyComplete();
        }

        @Test
        void shouldCorrectlyEvaluateExceptionMessage() {
            // Given
            Throwable myException = new PlatformException("My exception message");
            DynamicString dynamicString = DynamicString.from("#[error.getMessage()]", moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, myException);

            // Then
            StepVerifier.create(publisher)
                    .expectNext("My exception message")
                    .verifyComplete();
        }

        @Test
        void shouldReturnEmptyWhenScriptIsEmpty() {
            // Given
            Throwable myException = new PlatformException("My exception message");
            DynamicString dynamicString = DynamicString.from("#[]", moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, myException);

            // Then
            StepVerifier.create(publisher)
                    .verifyComplete();
        }

        @Test
        void shouldReturnEmptyWhenNullString() {
            // Given
            Throwable myException = new PlatformException("My exception message");
            DynamicString dynamicString = DynamicString.from(null, moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, myException);

            // Then
            StepVerifier.create(publisher)
                    .verifyComplete();
        }

        @Test
        void shouldReturnStringValue() {
            // Given
            Throwable myException = new PlatformException("My exception message");
            DynamicString dynamicString = DynamicString.from("my text", moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, myException);

            // Then
            StepVerifier.create(publisher)
                    .expectNext("my text")
                    .verifyComplete();
        }

        @Test
        void shouldReturnNullWhenNullDynamicValue() {
            // Given
            Throwable myException = new PlatformException("My exception message");
            DynamicString dynamicString = null;

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, context, myException);

            // Then
            assertThat(publisher).isNull();
        }
    }

    @Nested
    @DisplayName("Evaluate dynamic object value with throwable and context")
    class EvaluateDynamicObjectValueWithThrowableAndContext {

        @Test
        void shouldCorrectlyEvaluateDynamicObject() {
            // Given
            Throwable myException = new PlatformException("My exception message");
            DynamicObject dynamicObject = DynamicObject.from("#[error]", moduleContext);

            // When
            TypedPublisher<Object> publisher = evaluator.evaluateStream(dynamicObject, context, myException);

            // Then
            StepVerifier.create(publisher)
                    .expectNext(myException)
                    .verifyComplete();
        }

        @Test
        void shouldReturnStringDynamicObject() {
            // Given
            Throwable myException = new PlatformException("My exception message");
            DynamicObject dynamicObject = DynamicObject.from("my text", moduleContext);

            // When
            TypedPublisher<Object> publisher = evaluator.evaluateStream(dynamicObject, context, myException);

            // Then
            StepVerifier.create(publisher)
                    .expectNext("my text")
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Evaluate dynamic byte array with throwable and context")
    class EvaluateDynamicByteArrayWithThrowableAndContext {

        @Test
        void shouldCorrectlyEvaluateDynamicByteArrayFromException() {
            // Given
            Throwable myException = new PlatformException("My exception message");
            DynamicByteArray dynamicByteArray = DynamicByteArray.from("#[error]", moduleContext);

            // When
            TypedPublisher<byte[]> publisher = evaluator.evaluateStream(dynamicByteArray, context, myException);

            // Then
            StepVerifier.create(publisher)
                    .expectNextMatches(bytes -> Arrays.equals(bytes, StackTraceUtils.asByteArray(myException)))
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Evaluate dynamic string with custom arguments")
    class EvaluateDynamicStringWithCustomArguments {

        @Test
        void shouldCorrectlyEvaluateDynamicStringWithCustomArguments() {
            // Given
            Map<String, String> attributes = ImmutableMap.of("property1", "test1");
            Message message = MessageBuilder.get(TestComponent.class).withText("this is a test").attributes(new MyTestAttributes(attributes)).build();
            DynamicString dynamicString = DynamicString.from("#[message.attributes.pRoperty1]", moduleContext);

            // When
            TypedPublisher<String> publisher = evaluator.evaluateStream(dynamicString, asList("context", "message"), context, message);

            // Then
            StepVerifier.create(publisher)
                    .expectNext("test1")
                    .verifyComplete();
        }
    }

    private static class MyObject {
    }
}
