package com.reedelk.platform.commons;

import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;

import java.util.UUID;

import static com.reedelk.runtime.api.message.MessageAttributeKey.CORRELATION_ID;

public class CorrelationID {

    private CorrelationID() {
    }

    public static String getOrCreate(Message message) {
        return message.attributes().contains(CORRELATION_ID) ?
                message.attributes().get(CORRELATION_ID) :
                UUID.randomUUID().toString();
    }

    public static String getOrNull(FlowContext context) {
        if (context.containsKey(CORRELATION_ID)) {
            Object maybeCorrelationId = context.get(CORRELATION_ID);
            // This cover the case where the FlowContext correlation id attribute
            // might be overridden by another value in the flow. If it is a string,
            // we return its value, if it is another serializable value, we return
            // the default value, since the original correlation id is (by definition)
            // a string value.
            if (maybeCorrelationId instanceof String) {
                return (String) maybeCorrelationId;
            }
        }
        return null;
    }
}
