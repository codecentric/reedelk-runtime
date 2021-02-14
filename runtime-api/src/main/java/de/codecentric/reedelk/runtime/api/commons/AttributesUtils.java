package de.codecentric.reedelk.runtime.api.commons;

import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageAttributes;

import java.util.List;

public class AttributesUtils {

    private AttributesUtils() {
    }

    /**
     * Merges the attributes from all the input messages into one single collection.
     */
    public static MessageAttributes merge(List<Message> messages) {
        MessageAttributes mergedAttributes = new MessageAttributes();
        for (Message currentMessage : messages) {
            MessageAttributes currentAttributes = currentMessage.attributes();
            mergedAttributes.putAll(currentAttributes);
        }
        return mergedAttributes;
    }
}
