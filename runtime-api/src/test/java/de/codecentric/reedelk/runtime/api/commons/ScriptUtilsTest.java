package de.codecentric.reedelk.runtime.api.commons;

import de.codecentric.reedelk.runtime.api.commons.ModuleContext;
import de.codecentric.reedelk.runtime.api.commons.ScriptUtils;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicByteArray;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicLong;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScriptUtilsTest {

    private final long testModuleId = 10L;
    private final ModuleContext moduleContext = new ModuleContext(testModuleId);

    @Nested
    @DisplayName("is script tests")
    class IsScript {

        @Test
        void shouldReturnFalseWhenValueIsNull() {
            // Given
            String given = null;

            // When
            boolean actual = ScriptUtils.isScript(given);

            // Then
            assertThat(actual).isFalse();
        }

        @Test
        void shouldReturnFalseWhenValueIsEmptyString() {
            // Given
            String given = "";

            // When
            boolean actual = ScriptUtils.isScript(given);

            // Then
            assertThat(actual).isFalse();
        }

        @Test
        void shouldReturnFalseWhenValueContainsOnlyOneChar() {
            // Given
            String given = "#";

            // When
            boolean actual = ScriptUtils.isScript(given);

            // Then
            assertThat(actual).isFalse();
        }

        @Test
        void shouldReturnFalseWhenValueContainsOnlyLeadingMarker() {
            // Given
            String given = "#[";

            // When
            boolean actual = ScriptUtils.isScript(given);

            // Then
            assertThat(actual).isFalse();
        }

        @Test
        void shouldReturnTrue() {
            // Given
            String given = "#[myVariable]";

            // When
            boolean actual = ScriptUtils.isScript(given);

            // Then
            assertThat(actual).isTrue();
        }
    }

    @Nested
    @DisplayName("unwrap tests")
    class Unwrap {

        @Test
        void shouldReturnNullWhenInputIsNull() {
            // Given
            String given = null;

            // When
            String actual = ScriptUtils.unwrap(given);

            // Then
            assertThat(actual).isNull();
        }

        @Test
        void shouldReturnEmptyWhenInputIsEmpty() {
            // Given
            String given = "";

            // When
            String actual = ScriptUtils.unwrap(given);

            // Then
            assertThat(actual).isEmpty();
        }

        @Test
        void shouldReturnUnWrappedScript() {
            // Given
            String given = "#[myVariable + 'my text']";

            // When
            String actual = ScriptUtils.unwrap(given);

            // Then
            assertThat(actual).isEqualTo("myVariable + 'my text'");
        }

        @Test
        void shouldReturnOriginalNotWellFormedScript() {
            // Given
            String given = "#[myVariable + 'my text'";

            // When
            String actual = ScriptUtils.unwrap(given);

            // Then
            assertThat(actual).isEqualTo("#[myVariable + 'my text'");
        }
    }

    @Nested
    @DisplayName("as script tests")
    class AsScript {

        @Test
        void shouldReturnEmptyScriptWhenValueIsNull() {
            // Given
            String given = null;

            // When
            String actual = ScriptUtils.asScript(given);

            // Then
            assertThat(actual).isEqualTo("#[]");
        }

        @Test
        void shouldReturnEmptyScriptWhenValueIsEmptyString() {
            // Given
            String given = "";

            // When
            String actual = ScriptUtils.asScript(given);

            // Then
            assertThat(actual).isEqualTo("#[]");
        }

        @Test
        void shouldReturnValueWithLeadingAndTrailingMarkers() {
            // Given
            String given = "payload + 'suffix'";

            // When
            String actual = ScriptUtils.asScript(given);

            // Then
            assertThat(actual).isEqualTo("#[payload + 'suffix']");
        }
    }

    @Nested
    @DisplayName("is message payload test")
    class IsMessagePayload {

        // 'payload' is the shortcut of 'message.content.data'
        @Test
        void shouldReturnTrueWhenTextIsMessagePayload() {
            // Given
            DynamicValue<?> given = DynamicString.from("#[message.payload()]", moduleContext);

            // When
            boolean actual = ScriptUtils.isEvaluateMessagePayload(given);

            // Then
            assertThat(actual).isTrue();
        }

        // 'message.content().data()' is the extended version of 'message.payload()'
        @Test
        void shouldReturnTrueWhenTextIsMessageContentData() {
            // Given
            DynamicValue<?> given = DynamicString.from("#[message.content().data()]", moduleContext);

            // When
            boolean actual = ScriptUtils.isEvaluateMessagePayload(given);

            // Then
            assertThat(actual).isTrue();
        }

        @Test
        void shouldReturnFalseWhenTextIsNotRelatedToMessagePayload() {
            // Given
            DynamicValue<?> given = DynamicString.from("#[something]", moduleContext);

            // When
            boolean actual = ScriptUtils.isEvaluateMessagePayload(given);

            // Then
            assertThat(actual).isFalse();
        }

        @Test
        void shouldReturnFalseWhenTextIsNotAScript() {
            // Given
            DynamicValue<?> given = DynamicString.from("testing something");

            // When
            boolean actual = ScriptUtils.isEvaluateMessagePayload(given);

            // Then
            assertThat(actual).isFalse();
        }

        @Test
        void shouldReturnFalseWhenTextIsNotAScriptAndTextIsEvaluateMessagePayload() {
            // Given
            DynamicValue<?> given = DynamicString.from("message.payload()");

            // When
            boolean actual = ScriptUtils.isEvaluateMessagePayload(given);

            // Then
            assertThat(actual).isFalse();
        }

        @Test
        void shouldReturnFalseWhenGivenIsNull() {
            // Given
            DynamicValue<?> given = null;

            // When
            boolean actual = ScriptUtils.isEvaluateMessagePayload(given);

            // Then
            assertThat(actual).isFalse();
        }
    }

    @Nested
    @DisplayName("is message payload from dynamic value test")
    class IsMessagePayloadFromDynamicValue {

        @Test
        void shouldReturnFalseWhenValueIsNull() {
            // Given
            DynamicByteArray nullByteArray = null;

            // When
            boolean actual = ScriptUtils.isEvaluateMessagePayload(nullByteArray);

            // Then
            assertThat(actual).isFalse();
        }

        @Test
        void shouldReturnFalseWhenDynamicValueDoesNotContainScript() {
            // Given
            DynamicByteArray value = DynamicByteArray.from("Hello");

            // When
            boolean actual = ScriptUtils.isEvaluateMessagePayload(value);

            // Then
            assertThat(actual).isFalse();
        }

        @Test
        void shouldReturnFalseWhenDynamicValueContainsScriptButNotEvaluateMessagePayload() {
            // Given
            DynamicString value = DynamicString.from("#[context.correlationId]", new ModuleContext(testModuleId));

            // When
            boolean actual = ScriptUtils.isEvaluateMessagePayload(value);

            // Then
            assertThat(actual).isFalse();
        }

        @Test
        void shouldReturnTrueWhenDynamicValueContainsEvaluateMessagePayload() {
            // Given
            DynamicString value = DynamicString.from("#[message.payload()]", new ModuleContext(testModuleId));

            // When
            boolean actual = ScriptUtils.isEvaluateMessagePayload(value);

            // Then
            assertThat(actual).isTrue();
        }

        @Test
        void shouldReturnTrueWhenDynamicValueContainsEvaluateMessagePayloadFromContent() {
            // Given
            DynamicLong value = DynamicLong.from("#[message.content().data()]", new ModuleContext(testModuleId));

            // When
            boolean actual = ScriptUtils.isEvaluateMessagePayload(value);

            // Then
            assertThat(actual).isTrue();
        }

        @Test
        void shouldReturnFalseWhenDynamicValueContainsEmptyScript() {
            // Given
            DynamicString value = DynamicString.from("#[]", new ModuleContext(testModuleId));

            // When
            boolean actual = ScriptUtils.isEvaluateMessagePayload(value);

            // Then
            assertThat(actual).isFalse();
        }

        @Test
        void shouldReturnFalseWhenDynamicValueContainsNullScript() {
            // Given
            DynamicString value = DynamicString.from(null, new ModuleContext(testModuleId));

            // When
            boolean actual = ScriptUtils.isEvaluateMessagePayload(value);

            // Then
            assertThat(actual).isFalse();
        }
    }
}