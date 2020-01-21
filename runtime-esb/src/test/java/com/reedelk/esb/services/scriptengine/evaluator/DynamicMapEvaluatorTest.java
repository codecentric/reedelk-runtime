package com.reedelk.esb.services.scriptengine.evaluator;

import com.reedelk.esb.test.utils.TestComponent;
import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.*;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.reedelk.runtime.api.commons.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DynamicMapEvaluatorTest {

    private final long moduleId = 10L;
    private final ModuleContext moduleContext = new ModuleContext(moduleId);

    private FlowContext context;

    private DynamicMapEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new DynamicMapEvaluator();
        context = new TestableFlowContext();
    }

    @Test
    void shouldCorrectlyEvaluateMapWithScriptAndTextAndNumericValues() {
        // Given
        MessageAttributes attributes = new DefaultMessageAttributes(TestComponent.class, of("property1", "test1"));
        Message message = MessageBuilder.get().withText("test").attributes(attributes).build();

        DynamicStringMap dynamicMap = DynamicStringMap.from(of(
                "script", "#[message.attributes.propErty1]",
                "text", "This is a text",
                "numeric", "23532"), moduleContext);

        // When
        Map<String, String> evaluated = evaluator.evaluate(dynamicMap, context, message);

        // Then
        assertThat(evaluated.get("script")).isEqualTo("test1");
        assertThat(evaluated.get("text")).isEqualTo("This is a text");
        assertThat(evaluated.get("numeric")).isEqualTo("23532");
    }

    @Test
    void shouldCorrectlyEvaluateEmptyMap() {
        // Given
        Message message = MessageBuilder.get().empty().build();

        // When
        Map<String, String> evaluated = evaluator.evaluate(DynamicStringMap.empty(), context, message);

        // Then
        assertThat(evaluated).isEmpty();
    }

    @Test
    void shouldCorrectlyEvaluateNullMap() {
        // Given
        Message message = MessageBuilder.get().empty().build();
        DynamicStringMap dynamicStringMap = null;

        // When
        Map<String, String> evaluated = evaluator.evaluate(dynamicStringMap, context, message);

        // Then
        assertThat(evaluated).isEmpty();
    }

    @Test
    void shouldCorrectlyEvaluateMapWithValueContainingQuotes() {
        // Given
        Message message = MessageBuilder.get().withText("test").build();
        DynamicStringMap dynamicMap = DynamicStringMap.from(
                of("text", "a simple text 'with quotes'"), moduleContext);

        // When
        Map<String, String> evaluated = evaluator.evaluate(dynamicMap, context, message);

        // Then
        assertThat(evaluated.get("text")).isEqualTo("a simple text 'with quotes'");
    }

    @Test
    void shouldCorrectlyEvaluateMapWithThrowable() {
        // Given
        context.put("myVariable", "aabbcc");

        DynamicStringMap dynamicMap = DynamicStringMap.from(of(
                "script", "#[context.myVariable]",
                "text", "This is a text",
                "evaluateError", "#[error.getMessage()]"),
                moduleContext);

        ESBException myException = new ESBException("This is an error");

        // When
        Map<String, String> evaluated = evaluator.evaluate(dynamicMap, context, myException);

        // Then
        assertThat(evaluated.get("script")).isEqualTo("aabbcc");
        assertThat(evaluated.get("text")).isEqualTo("This is a text");
        assertThat(evaluated.get("evaluateError")).isEqualTo("This is an error");
    }

    @Test
    void shouldCorrectlyEvaluateMapWithDashInKey() {
        // Given
        DynamicStringMap dynamicMap = DynamicStringMap.from(
                of("X-Message-Text", "#[error.getMessage()]"),
                moduleContext);

        ESBException myException = new ESBException("This is an error");

        // When
        Map<String, String> evaluated = evaluator.evaluate(dynamicMap, context, myException);

        // Then
        assertThat(evaluated.get("X-Message-Text")).isEqualTo("This is an error");
    }

    @Test
    void shouldCorrectlyEvaluateMapWithEmptyDynamicScriptValue() {
        // Given
        MessageAttributes attributes = new DefaultMessageAttributes(TestComponent.class, of("property1", "test1"));
        Message message = MessageBuilder.get().withText("test").attributes(attributes).build();

        DynamicStringMap dynamicMap = DynamicStringMap.from(of("Key1", "#[]"), moduleContext);

        // When
        Map<String, String> evaluated = evaluator.evaluate(dynamicMap, context, message);

        // Then
        assertThat(evaluated.get("Key1")).isEqualTo("");
    }

    @Test
    void shouldCorrectlyEvaluateMapWithEmptyValue() {
        // Given
        MessageAttributes attributes = new DefaultMessageAttributes(TestComponent.class, of("property1", "test1"));
        Message message = MessageBuilder.get().withText("test").attributes(attributes).build();

        DynamicStringMap dynamicMap = DynamicStringMap.from(of("Key1", ""), moduleContext);

        // When
        Map<String, String> evaluated = evaluator.evaluate(dynamicMap, context, message);

        // Then
        assertThat(evaluated.get("Key1")).isEqualTo("");
    }

    @Test
    void shouldCorrectlyEvaluateMapWithEmptyKey() {
        // Given
        MessageAttributes attributes = new DefaultMessageAttributes(TestComponent.class, of("property1", "test1"));
        Message message = MessageBuilder.get().withText("test").attributes(attributes).build();

        DynamicStringMap dynamicMap = DynamicStringMap.from(of(
                "", "myValue", "key2", "value2"),
                moduleContext);

        // When
        Map<String, String> evaluated = evaluator.evaluate(dynamicMap, context, message);

        // Then
        assertThat(evaluated).hasSize(2);
        assertThat(evaluated).containsEntry("key2", "value2");
        assertThat(evaluated).containsEntry("", "myValue");
    }


    private class TestableFlowContext extends HashMap<String, Serializable> implements FlowContext {

    }
}