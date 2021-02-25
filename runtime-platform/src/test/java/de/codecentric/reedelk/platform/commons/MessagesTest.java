package de.codecentric.reedelk.platform.commons;

import de.codecentric.reedelk.platform.test.utils.TestMessage;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

class MessagesTest {

    @Test
    void shouldCorrectlySerializeFlowErrorMessage() {
        // When
        String actualMessageJson = Messages.FlowErrorMessage.DEFAULT.format(
                10L,
                "my-module-name",
                "aabbcc",
                "My flow",
                "aabb-cc1122-3344-ff",
                "de.codecentric.reedelk.platform.MyException",
                "An error has occurred");

        // Then
        JSONAssert.assertEquals(TestMessage.FLOW_ERROR_MESSAGE_DEFAULT.get(), actualMessageJson, true);
    }

    @Test
    void shouldCorrectlySerializeFlowErrorMessageWhenCorrelationIdIsNull() {
        // When
        String actualMessageJson = Messages.FlowErrorMessage.DEFAULT.format(
                10L,
                "my-module-name",
                "aabbcc",
                "My flow",
                null,
                "de.codecentric.reedelk.platform.MyException",
                "An error has occurred");

        // Then
        JSONAssert.assertEquals(
                TestMessage.FLOW_ERROR_MESSAGE_DEFAULT_WITH_NULL_CORRELATION_ID.get(),
                actualMessageJson, true);
    }
}
