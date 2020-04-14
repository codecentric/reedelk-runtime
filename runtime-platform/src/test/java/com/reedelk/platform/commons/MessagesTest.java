package com.reedelk.platform.commons;

import com.reedelk.platform.test.utils.TestMessage;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.reedelk.platform.commons.Messages.FlowErrorMessage;

class MessagesTest {

    @Test
    void shouldCorrectlySerializeFlowErrorMessage() {
        // When
        String actualMessageJson = FlowErrorMessage.DEFAULT.format(
                10L,
                "my-module-name",
                "aabbcc",
                "My flow",
                "aabb-cc1122-3344-ff",
                "com.reedelk.platform.MyException",
                "An error has occurred");

        // Then
        JSONAssert.assertEquals(TestMessage.FLOW_ERROR_MESSAGE_DEFAULT.get(), actualMessageJson, true);
    }

    @Test
    void shouldCorrectlySerializeFlowErrorMessageWhenCorrelationIdIsNull() {
        // When
        String actualMessageJson = FlowErrorMessage.DEFAULT.format(
                10L,
                "my-module-name",
                "aabbcc",
                "My flow",
                null,
                "com.reedelk.platform.MyException",
                "An error has occurred");

        // Then
        JSONAssert.assertEquals(
                TestMessage.FLOW_ERROR_MESSAGE_DEFAULT_WITH_NULL_CORRELATION_ID.get(),
                actualMessageJson, true);
    }
}
