package com.reedelk.esb.commons;

import com.reedelk.esb.test.utils.TestMessage;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.reedelk.esb.commons.Messages.FlowErrorMessage;

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
                "com.reedelk.esb.MyException",
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
                "com.reedelk.esb.MyException",
                "An error has occurred");

        // Then
        JSONAssert.assertEquals(
                TestMessage.FLOW_ERROR_MESSAGE_DEFAULT_WITH_NULL_CORRELATION_ID.get(),
                actualMessageJson, true);
    }
}