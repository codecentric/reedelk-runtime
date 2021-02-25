package de.codecentric.reedelk.platform.services.scriptengine.evaluator;

import de.codecentric.reedelk.platform.execution.context.DefaultFlowContext;
import de.codecentric.reedelk.platform.flow.deserializer.converter.ProxyScript;
import de.codecentric.reedelk.platform.test.utils.TestComponent;
import de.codecentric.reedelk.runtime.api.commons.ModuleContext;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.runtime.api.message.content.TypedPublisher;
import de.codecentric.reedelk.runtime.api.script.Script;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ScriptEvaluatorTest {

    private final long moduleId = 10L;
    private final ModuleContext moduleContext = new ModuleContext(moduleId);

    private FlowContext context;
    private ScriptEvaluator evaluator;
    private Message emptyMessage = MessageBuilder.get(TestComponent.class).empty().build();

    @BeforeEach
    void setUp() {
        context = DefaultFlowContext.from(emptyMessage);
        evaluator = new ScriptEvaluator();
    }

    @Nested
    @DisplayName("Evaluate script with message and context")
    class ScriptWithMessageAndContext {

        @Test
        void shouldCorrectlyEvaluateScriptAndReturnOptional() {
            // Given
            Script stringConcatenation = scriptFromBody(wrapAsTestFunction("'one' + ' ' + 'two'"));

            // When
            Optional<String> actual = evaluator.evaluate(stringConcatenation, String.class, context, emptyMessage);

            // Then
            assertThat(actual).isPresent().contains("one two");
        }

        @Test
        void shouldCorrectlyReturnEmptyOptionalWhenScriptIsEmpty() {
            // Given
            Script emptyScript = scriptFromBody("");

            // When
            Optional<String> actual = evaluator.evaluate(emptyScript, String.class, context, emptyMessage);

            // Then
            assertThat(actual).isNotPresent();
        }

        @Test
        void shouldCorrectlyReturnEmptyOptionalWhenScriptIsNull() {
            // Given
            Script nullScript = null;

            // When
            Optional<String> actual = evaluator.evaluate(nullScript, String.class, context, emptyMessage);

            // Then
            assertThat(actual).isNotPresent();
        }

        @Test
        void shouldThrowExceptionWhenScriptIsNotValid() {
            // Given
            Script invalidScript = scriptFromBody(wrapAsTestFunction("'hello"));

            // When
            PlatformException exception = Assertions.assertThrows(PlatformException.class,
                    () -> evaluator.evaluate(invalidScript, String.class, context, emptyMessage));

            // Then
            assertThat(exception).isNotNull();
        }

        @Test
        void shouldCorrectlyConvertIntegerResultToString() {
            // Given
            Script intScript = scriptFromBody(wrapAsTestFunction("2351"));

            // When
            Optional<Integer> actual = evaluator.evaluate(intScript, Integer.class, context, emptyMessage);

            // Then
            assertThat(actual).isPresent().contains(2351);
        }

        @Test
        void shouldCorrectlyEvaluateMessagePayload() {
            // Given
            Script payloadScript = scriptFromBody(wrapAsTestFunction("message.payload()"));
            Message message = MessageBuilder.get(TestComponent.class).withText("my payload as text").build();

            // When
            Optional<String> actual = evaluator.evaluate(payloadScript, String.class, context, message);

            // Then
            assertThat(actual).isPresent().contains("my payload as text");
        }

        @Test
        void shouldCorrectlyEvaluateContextVariable() {
            // Given
            context.put("messageVar", "my sample");
            Script contextVariableScript = scriptFromBody(wrapAsTestFunction("context.messageVar"));

            // When
            Optional<String> actual = evaluator.evaluate(contextVariableScript, String.class, context, emptyMessage);

            // Then
            assertThat(actual).isPresent().contains("my sample");
        }
    }

    @Nested
    @DisplayName("Evaluate script with messages and context")
    class ScriptWithMessagesAndContext {

        private final List<Message> messages = asList(
                MessageBuilder.get(TestComponent.class).withText("one").build(),
                MessageBuilder.get(TestComponent.class).withText("two").build(),
                MessageBuilder.get(TestComponent.class).withText("three").build());

        @Test
        void shouldCorrectlyEvaluateScriptAndReturnOptional() {
            // Given
            String concatenateMessagesScript = "messages.collect{ it.payload() }.join(';')";
            Script stringConcatenation = scriptFromBody(wrapAsTestFunctionWithMessages(concatenateMessagesScript));

            // When
            Optional<String> actual = evaluator.evaluate(stringConcatenation, String.class, context, messages);

            // Then
            assertThat(actual).isPresent().contains("one;two;three");
        }

        @Test
        void shouldCorrectlyReturnEmptyOptionalWhenScriptIsEmpty() {
            // Given
            Script emptyScript = scriptFromBody("");

            // When
            Optional<String> actual = evaluator.evaluate(emptyScript, String.class, context, messages);

            // Then
            assertThat(actual).isNotPresent();
        }

        @Test
        void shouldCorrectlyReturnEmptyOptionalWhenScriptIsNull() {
            // Given
            Script nullScript = null;

            // When
            Optional<String> actual = evaluator.evaluate(nullScript, String.class, context, messages);

            // Then
            assertThat(actual).isNotPresent();
        }

        @Test
        void shouldThrowExceptionWhenScriptIsNotValid() {
            // Given
            Script notValidScript = scriptFromBody("return 'hello");

            // When
            PlatformException exception = Assertions.assertThrows(PlatformException.class,
                    () -> evaluator.evaluate(notValidScript, String.class, context, messages));

            // Then
            assertThat(exception).isNotNull();
        }
    }

    @Nested
    @DisplayName("Evaluate script stream with message and context")
    class EvaluateScriptStreamWithMessageAndContext {

        @Test
        void shouldReturnByteStreamFromString() {
            // Given
            Script textValuedScript = scriptFromBody(wrapAsTestFunction("'my test'"));

            // When
            TypedPublisher<byte[]> actual = evaluator.evaluateStream(textValuedScript, byte[].class, context, emptyMessage);

            // Then
            assertThat(actual.getType()).isEqualTo(byte[].class);
            StepVerifier.create(actual)
                    .expectNextMatches(bytes -> Arrays.equals(bytes, "my test".getBytes()))
                    .verifyComplete();
        }

        @Test
        void shouldReturnResolvedStreamWhenMessagePayloadExecuted() {
            // Given
            Flux<byte[]> stream = Flux.just("one".getBytes(), "two".getBytes());
            Message message = MessageBuilder.get(TestComponent.class).withBinary(stream, MimeType.TEXT_PLAIN).build();

            Script extractStreamScript = scriptFromBody(wrapAsTestFunction("message.payload()"));

            // When
            TypedPublisher<byte[]> actual = evaluator.evaluateStream(extractStreamScript, byte[].class, context, message);

            // Then
            assertThat(actual.getType()).isEqualTo(byte[].class);
            StepVerifier.create(actual)
                    .expectNextMatches(bytes -> Arrays.equals(bytes, "onetwo".getBytes()))
                    .verifyComplete();
        }

        @Test
        void shouldReturnNullStreamWhenScriptIsNull() {
            // Given
            Script nullScript = null;

            // When
            Publisher<byte[]> actual = evaluator.evaluateStream(nullScript, byte[].class, context, emptyMessage);

            // Then
            assertThat(actual).isNull();
        }

        @Test
        void shouldReturnEmptyStreamWhenScriptIsEmpty() {
            // Given
            Script emptyScript = scriptFromBody("");

            // When
            Publisher<byte[]> actual = evaluator.evaluateStream(emptyScript, byte[].class, context, emptyMessage);

            // Then
            StepVerifier.create(actual).verifyComplete();
        }

        @Test
        void shouldReturnEmptyStreamWhenScriptReturnsNull() {
            // Given
            Script scriptReturningNull = scriptFromBody(wrapAsTestFunction("null"));

            // When
            Publisher<byte[]> actual = evaluator.evaluateStream(scriptReturningNull, byte[].class, context, emptyMessage);

            // Then
            StepVerifier.create(actual).verifyComplete();
        }
    }

    private static String wrapAsTestFunction(String code) {
        return String.format("def myTestFunction(context, message) {" +
                "%s" +
                "}", code);
    }

    private static String wrapAsTestFunctionWithMessages(String code) {
        return String.format("def myTestFunction(context, messages) {" +
                "%s" +
                "}", code);
    }

    private Script scriptFromBody(String body) {
        Script originalScript = Script.from("/test/path", moduleContext);
        return new ProxyScript(originalScript, body);
    }
}
