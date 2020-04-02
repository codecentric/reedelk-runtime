package com.reedelk.esb.services.scriptengine.evaluator;

import com.reedelk.esb.test.utils.TestComponent;
import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.commons.StackTraceUtils;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.DefaultMessageAttributes;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.script.dynamicvalue.*;
import com.reedelk.runtime.commons.ObjectToBytes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.Optional;

import static com.reedelk.runtime.api.commons.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DynamicValueEvaluatorTest {

    private final long moduleId = 10L;

    @Mock
    private FlowContext context;

    private ModuleContext moduleContext;
    private DynamicValueEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new DynamicValueEvaluator();
        moduleContext = new ModuleContext(moduleId);
    }

    @Nested
    @DisplayName("Evaluate dynamic string value with message and context")
    class EvaluateDynamicStringValueWithMessageAndContext {

        @Test
        void shouldCorrectlyEvaluateMessageAttributeProperty() {
            // Given
            MessageAttributes attributes = new DefaultMessageAttributes(TestComponent.class, of("property1", "test1"));
            Message message = MessageBuilder.get().withText("this is a test").attributes(attributes).build();
            DynamicString dynamicString = DynamicString.from("#[message.attributes.property1]", moduleContext);

            // When
            Optional<String> evaluated = evaluator.evaluate(dynamicString, context, message);

            // Then
            assertThat(evaluated).isPresent().contains("test1");
        }

        @Test
        void shouldCorrectlyEvaluateTextPayload() {
            // Given
            Message message = MessageBuilder.get().withText("this is a test").build();
            DynamicString dynamicString = DynamicString.from("#[message.payload()]", moduleContext);

            // When
            Optional<String> evaluated = evaluator.evaluate(dynamicString, context, message);

            // Then
            assertThat(evaluated).isPresent().contains("this is a test");
        }

        @Test
        void shouldCorrectlyEvaluateStreamPayload() {
            // Given
            Flux<String> textStream = Flux.just("one", "two");
            Message message = MessageBuilder.get().withText(textStream).build();

            DynamicString dynamicString = DynamicString.from("#[message.payload()]", moduleContext);

            // When
            Optional<String> evaluated = evaluator.evaluate(dynamicString, context, message);

            // Then
            assertThat(evaluated).isPresent().contains("onetwo");
        }

        @Test
        void shouldCorrectlyConcatenateStreamWithString() {
            // Given
            Flux<String> content = Flux.just("Hello", ", this", " is", " just", " a");
            Message message = MessageBuilder.get().withText(content).build();

            DynamicString dynamicString = DynamicString.from("#[message.content.data() + ' test.']", moduleContext);

            // When
            Optional<String> result = evaluator.evaluate(dynamicString, context, message);

            // Then
            assertThat(result).isPresent().contains("Hello, this is just a test.");
        }

        @Test
        void shouldCorrectlyConcatenateWithString() {
            // Given
            String payload = "Hello, this is just a";
            Message message = MessageBuilder.get().withText(payload).build();

            DynamicString dynamicString = DynamicString.from("#[message.content.data() + ' test.']", moduleContext);

            // When
            Optional<String> result = evaluator.evaluate(dynamicString, context, message);

            // Then
            assertThat(result).isPresent().contains("Hello, this is just a test.");
        }

        @Test
        void shouldCorrectlyEvaluateString() {
            // Given
            Message message = MessageBuilder.get().withText("test").build();

            DynamicString dynamicString = DynamicString.from("#['evaluation test']", moduleContext);

            // When
            Optional<String> result = evaluator.evaluate(dynamicString, context, message);

            // Then
            assertThat(result).isPresent().contains("evaluation test");
        }

        @Test
        void shouldReturnTextFromDynamicValue() {
            // Given
            Message message = MessageBuilder.get().withText("test").build();
            DynamicString dynamicString = DynamicString.from("Expected text", moduleContext);

            // When
            Optional<String> result = evaluator.evaluate(dynamicString, context, message);

            // Then
            assertThat(result).isPresent().contains("Expected text");
        }

        @Test
        void shouldReturnEmptyString() {
            // Given
            Message message = MessageBuilder.get().withText("test").build();
            DynamicString dynamicString = DynamicString.from("", moduleContext);

            // When
            Optional<String> result = evaluator.evaluate(dynamicString, context, message);

            // Then
            assertThat(result).isPresent().contains("");
        }

        @Test
        void shouldResultNotBePresentWhenDynamicValueIsNull() {
            // Given
            Message message = MessageBuilder.get().withText("test").build();
            DynamicString dynamicString = null;

            // When
            Optional<String> result = evaluator.evaluate(dynamicString, context, message);

            // Then
            assertThat(result).isNotPresent();
        }

        @Test
        void shouldResultNotBePresentWhenDynamicValueScriptIsEmpty() {
            // Given
            Message message = MessageBuilder.get().withText("test").build();
            DynamicString dynamicString = DynamicString.from("#[]", moduleContext);

            // When
            Optional<String> result = evaluator.evaluate(dynamicString, context, message);

            // Then
            assertThat(result).isNotPresent();
        }

        @Test
        void shouldResultNotBePresentWhenDynamicValueStringIsNull() {
            // Given
            Message message = MessageBuilder.get().withText("test").build();
            DynamicString dynamicString = DynamicString.from(null, moduleContext);

            // When
            Optional<String> result = evaluator.evaluate(dynamicString, context, message);

            // Then
            assertThat(result).isNotPresent();
        }

        @Test
        void shouldCorrectlyEvaluateInteger() {
            // Given
            Message message = MessageBuilder.get().withJavaObject(23432).build();
            DynamicString dynamicString = DynamicString.from("#[message.payload()]", moduleContext);

            // When
            Optional<String> result = evaluator.evaluate(dynamicString, context, message);

            // Then
            assertThat(result).contains("23432");
        }

        @Test
        void shouldEvaluateNullScriptResults() {
            // Given
            Message message = MessageBuilder.get().withText("A text").build();
            DynamicString dynamicString = DynamicString.from("#[null]", moduleContext);

            // When
            Optional<String> result = evaluator.evaluate(dynamicString, context, message);

            // Then
            assertThat(result).isNotPresent();
        }
    }

    @Nested
    @DisplayName("Evaluate dynamic integer value with message and context")
    class EvaluateDynamicIntegerValueWithMessageAndContext {

        @Test
        void shouldCorrectlyEvaluateInteger() {
            // Given
            Message message = MessageBuilder.get().withText("test").build();
            DynamicInteger dynamicInteger = DynamicInteger.from("#[506]", moduleContext);

            // When
            Optional<Integer> evaluated = evaluator.evaluate(dynamicInteger, context, message);

            // Then
            assertThat(evaluated).isPresent().contains(506);
        }

        // Testing optimistic typing (Nashorn uses optimistic typing (since JDK 8u40))
        // http://openjdk.java.net/jeps/196.
        @Test
        void shouldCorrectlySumNumber() {
            // Given
            Message message = MessageBuilder.get().withText("12").build();
            DynamicInteger dynamicInteger = DynamicInteger.from("#[parseInt(message.payload()) + 10]", moduleContext);

            // When
            Optional<Integer> evaluated = evaluator.evaluate(dynamicInteger, context, message);

            // Then
            assertThat(evaluated).isPresent().contains(22);
        }

        @Test
        void shouldCorrectlyEvaluateIntegerFromText() {
            // Given
            Message message = MessageBuilder.get().withText("test").build();
            DynamicInteger dynamicInteger = DynamicInteger.from(53, moduleContext);

            // When
            Optional<Integer> evaluated = evaluator.evaluate(dynamicInteger, context, message);

            // Then
            assertThat(evaluated).isPresent().contains(53);
        }

        @Test
        void shouldCorrectlyEvaluateIntegerFromMessagePayload() {
            // Given
            Message message = MessageBuilder.get().withJavaObject(120).build();
            DynamicInteger dynamicInteger = DynamicInteger.from("#[message.payload()]", moduleContext);

            // When
            Optional<Integer> evaluated = evaluator.evaluate(dynamicInteger, context, message);

            // Then
            assertThat(evaluated).isPresent().contains(120);
        }
    }

    @Nested
    @DisplayName("Evaluate dynamic boolean value with message and context")
    class EvaluateDynamicBooleanValueWithMessageAndContext {

        @Test
        void shouldCorrectlyEvaluateBoolean() {
            // Given
            Message message = MessageBuilder.get().withText("a test").build();
            DynamicBoolean dynamicBoolean = DynamicBoolean.from("#[1 == 1]", moduleContext);

            // When
            Optional<Boolean> evaluated = evaluator.evaluate(dynamicBoolean, context, message);

            // Then
            assertThat(evaluated).isPresent().contains(true);
        }

        @Test
        void shouldCorrectlyEvaluateBooleanFromPayload() {
            // Given
            Message message = MessageBuilder.get().withText("true").build();
            DynamicBoolean dynamicBoolean = DynamicBoolean.from("#[message.payload()]", moduleContext);

            // When
            Optional<Boolean> evaluated = evaluator.evaluate(dynamicBoolean, context, message);

            // Then
            assertThat(evaluated).isPresent().contains(true);
        }
    }

    @Nested
    @DisplayName("Evaluate dynamic byte array value with message and context")
    class EvaluateDynamicByteArrayWithMessageAndContext {

        @Test
        void shouldCorrectlyEvaluateByteArrayFromPayload() {
            // Given
            String payload = "My sample payload";
            Message message = MessageBuilder.get().withText(payload).build();
            DynamicByteArray dynamicByteArray = DynamicByteArray.from("#[message.payload()]", moduleContext);

            // When
            Optional<byte[]> evaluated = evaluator.evaluate(dynamicByteArray, context, message);

            // Then
            assertThat(evaluated).isPresent().contains(payload.getBytes());
        }
    }

    @Nested
    @DisplayName("Evaluate dynamic object value with message and context")
    class EvaluateDynamicObjectValueWithMessageAndContext {

        @Test
        void shouldCorrectlyEvaluateDynamicObject() {
            // Given
            Flux<String> content = Flux.just("Hello", ", this", " is", " just", " a");
            Message message = MessageBuilder.get().withText(content).build();

            DynamicObject dynamicObject = DynamicObject.from("#[message.content]", moduleContext);

            // When
            Optional<Object> result = evaluator.evaluate(dynamicObject, context, message);

            // Then
            assertThat(result).isPresent().containsSame(message.content());
        }

        @Test
        void shouldCorrectlyEvaluateMessage() {
            // Given
            Message message = MessageBuilder.get().withText("test").build();
            DynamicObject dynamicString = DynamicObject.from("#[message]", moduleContext);

            // When
            Optional<Object> evaluated = evaluator.evaluate(dynamicString, context, message);

            // Then
            assertThat(evaluated).isPresent().contains(message);
        }

        @Test
        void shouldCorrectlyEvaluateMessagePayload() {
            // Given
            MyObject given = new MyObject();
            Message message = MessageBuilder.get().withJavaObject(given).build();
            DynamicObject dynamicString = DynamicObject.from("#[message.payload()]", moduleContext);

            // When
            Optional<Object> evaluated = evaluator.evaluate(dynamicString, context, message);

            // Then
            assertThat(evaluated).isPresent().contains(given);
        }
    }

    @Nested
    @DisplayName("Evaluate dynamic object value with mime type, message and context")
    class EvaluateDynamicObjectValueWithMimeTypeAndMessageAndContext {

        @Test
        void shouldCorrectlyConvertObjectToTextMimeType() {
            // Given
            Flux<String> content = Flux.just("Hello", ", this", " is", " just", " a");
            Message message = MessageBuilder.get().withText(content).build();

            DynamicObject dynamicObject = DynamicObject.from("#[message.content]", moduleContext);

            // When
            Optional<Object> result = evaluator.evaluate(dynamicObject, MimeType.TEXT, context, message);

            // Then
            assertThat(result).isPresent().contains(message.content().toString());
        }

        @Test
        void shouldCorrectlyConvertObjectToBinaryMimeType() {
            // Given
            Message message = MessageBuilder.get().withText("my test").build();

            DynamicObject dynamicObject = DynamicObject.from("#[message.content.type()]", moduleContext);

            // When
            Optional<Object> result = evaluator.evaluate(dynamicObject, MimeType.APPLICATION_BINARY, context, message);

            // Then
            assertThat(result).isPresent().contains(message.content().type().toString().getBytes());
        }

        @Test
        void shouldReturnEmptyResultWhenDynamicObjectIsNull() {
            // Given
            Message message = MessageBuilder.get().empty().build();

            DynamicObject dynamicObject = null;

            // When
            Optional<Object> result = evaluator.evaluate(dynamicObject, MimeType.APPLICATION_BINARY, context, message);

            // Then
            assertThat(result).isNotPresent();
        }

        @Test
        void shouldReturnEmptyResultWhenDynamicObjectHasEmptyScript() {
            // Given
            Message message = MessageBuilder.get().empty().build();

            DynamicObject dynamicObject = DynamicObject.from("#[]", moduleContext);

            // When
            Optional<Object> result = evaluator.evaluate(dynamicObject, MimeType.APPLICATION_JSON, context, message);

            // Then
            assertThat(result).isNotPresent();
        }

        @Test
        void shouldReturnConvertedObjectWhenNotScript() {
            // Given
            Message message = MessageBuilder.get().empty().build();

            MyTestObject testObject = new MyTestObject(43, 234.23f, "test");

            DynamicObject dynamicObject = DynamicObject.from(testObject, moduleContext);

            // When
            Optional<Object> result = evaluator.evaluate(dynamicObject, MimeType.TEXT, context, message);

            // Then
            assertThat(result).isPresent().contains(testObject.toString());
        }

        @Test
        void shouldCorrectlyConvertMessagePayload() {
            // Given
            MyTestObject testObject = new MyTestObject(2345, 4.223f, "my object");
            Message message = MessageBuilder.get().withJavaObject(testObject, MimeType.ANY).build();

            DynamicObject dynamicObject = DynamicObject.from("#[message.payload()]", moduleContext);

            // When
            Optional<Object> result = evaluator.evaluate(dynamicObject, MimeType.TEXT, context, message);

            // Then
            assertThat(result).isPresent().contains(testObject.toString());
        }
    }

    @Nested
    @DisplayName("Evaluate dynamic string with throwable and context")
    class EvaluateDynamicStringWithThrowableAndContext {

        @Test
        void shouldCorrectlyEvaluateErrorPayload() {
            // Given
            Throwable myException = new ESBException("Test error");
            DynamicString dynamicString = DynamicString.from("#[error]", moduleContext);

            // When
            Optional<String> evaluated = evaluator.evaluate(dynamicString, context, myException);

            // Then
            assertThat(evaluated).isPresent().contains(StackTraceUtils.asString(myException));
        }

        @Test
        void shouldCorrectlyEvaluateExceptionMessage() {
            // Given
            Throwable myException = new ESBException("My exception message");
            DynamicString dynamicString = DynamicString.from("#[error.getMessage()]", moduleContext);

            // When
            Optional<String> evaluated = evaluator.evaluate(dynamicString, context, myException);

            // Then
            assertThat(evaluated).isPresent().contains("My exception message");
        }

        @Test
        void shouldReturnEmptyWhenScriptIsEmpty() {
            // Given
            Throwable myException = new ESBException("My exception message");
            DynamicString dynamicString = DynamicString.from("#[]", moduleContext);

            // When
            Optional<String> evaluated = evaluator.evaluate(dynamicString, context, myException);

            // Then
            assertThat(evaluated).isNotPresent();
        }

        @Test
        void shouldReturnEmptyWhenNullString() {
            // Given
            Throwable myException = new ESBException("My exception message");
            DynamicString dynamicString = DynamicString.from(null, moduleContext);

            // When
            Optional<String> evaluated = evaluator.evaluate(dynamicString, context, myException);

            // Then
            assertThat(evaluated).isNotPresent();
        }

        @Test
        void shouldReturnStringValue() {
            // Given
            Throwable myException = new ESBException("My exception message");
            DynamicString dynamicString = DynamicString.from("my text", moduleContext);

            // When
            Optional<String> evaluated = evaluator.evaluate(dynamicString, context, myException);

            // Then
            assertThat(evaluated).contains("my text");
        }

        @Test
        void shouldReturnEmptyWhenNullDynamicValue() {
            // Given
            Throwable myException = new ESBException("My exception message");
            DynamicString dynamicString = null;

            // When
            Optional<String> evaluated = evaluator.evaluate(dynamicString, context, myException);

            // Then
            assertThat(evaluated).isNotPresent();
        }
    }

    @Nested
    @DisplayName("Evaluate dynamic object value with throwable and context")
    class EvaluateDynamicObjectValueWithThrowableAndContext {

        @Test
        void shouldCorrectlyEvaluateDynamicObject() {
            // Given
            Throwable myException = new ESBException("My exception message");
            DynamicObject dynamicObject = DynamicObject.from("#[error]", moduleContext);

            // When
            Optional<Object> result = evaluator.evaluate(dynamicObject, context, myException);

            // Then
            assertThat(result).isPresent().containsSame(myException);
        }

        @Test
        void shouldReturnStringDynamicObject() {
            // Given
            Throwable myException = new ESBException("My exception message");
            DynamicObject dynamicObject = DynamicObject.from("my text", moduleContext);

            // When
            Optional<Object> result = evaluator.evaluate(dynamicObject, context, myException);

            // Then
            assertThat(result).isPresent().contains("my text");
        }
    }

    @Nested
    @DisplayName("Evaluate dynamic byte array with throwable and context")
    class EvaluateDynamicByteArrayWithThrowableAndContext {

        @Test
        void shouldCorrectlyEvaluateDynamicByteArrayFromException() {
            // Given
            Throwable myException = new ESBException("My exception message");
            DynamicByteArray dynamicByteArray = DynamicByteArray.from("#[error]", moduleContext);

            // When
            Optional<byte[]> result = evaluator.evaluate(dynamicByteArray, context, myException);

            // Then
            assertThat(result).isPresent().contains(StackTraceUtils.asByteArray(myException));
        }
    }

    static class NotSerializableContent {
        int value;
    }

    private static class MyObject {
    }
}
