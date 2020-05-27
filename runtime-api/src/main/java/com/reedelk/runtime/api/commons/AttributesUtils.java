package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageAttributes;

import java.util.List;

public class AttributesUtils {

    private AttributesUtils() {
    }

    /**
     * Merges the attributes from all the input messages into one single collection.
     */
    public static MessageAttributes merge(List<Message> messages) {
        MessageAttributes mergedAttributes = new MessageAttributes();
        for (Message message : messages) {
            MessageAttributes messageAttributes = message.attributes();
            mergedAttributes.putAll(messageAttributes);
        }
        return mergedAttributes;
    }
}
