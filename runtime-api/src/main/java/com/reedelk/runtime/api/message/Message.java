package com.reedelk.runtime.api.message;


import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.message.content.TypedContent;

import java.io.Serializable;

@AutocompleteType(
        description = "The Message encapsulates data and " +
                "attributes passing through the components of a flow.")
public interface Message extends Serializable {

    /**
     * Returns the message payload. This method automatically consumes
     * the payload stream if it is a stream.
     */
    @AutocompleteItem(
            returnType = Object.class,
            signature = "getPayload()",
            example = "message.getPayload()",
            description = "Returns the payload (data) of the message. " +
                    "The payload could be a text, a byte array, a collection " +
                    "and so on depending on the component which generated it.")
    default <Type> Type getPayload() {
        return payload();
    }

    /**
     * Returns the message payload. This method automatically consumes
     * the payload stream if it is a stream.
     */
    @AutocompleteItem(
            returnType = Object.class,
            signature = "payload()",
            example = "message.payload()",
            description = "Returns the payload (data) of the message. " +
                    "The payload could be a text, a byte array, a collection " +
                    "and so on depending on the component which generated it.")
    <Type> Type payload();

    /*
     * This method is needed by Script engine to access this object's properties.
     */
    <Type, StreamType, R extends TypedContent<Type, StreamType>> R getContent();

    /**
     *
     * This is a 'nice to have' method to make getting the content more readable
     * from the Script language e.g. message.content() instead of message.getContent().
     */
    @AutocompleteItem(
            returnType = TypedContent.class,
            signature = "content()",
            example = "message.content()",
            description = "Returns the content descriptor of this message. " +
                    "The message content contains information about the payload's mime type, " +
                    "data type and stream status.")
    <Type, StreamType, R extends TypedContent<Type, StreamType>> R content();

    /**
     * This method is needed by Script engine to access this object's properties.
     * @return the message attributes.
     */
    MessageAttributes getAttributes();

    /**
     * This is a 'nice to have' method to make getting the attributes more readable
     * from the Script language e.g. message.attributes() instead of message.getAttributes().
     * @return the message attributes.
     */
    @AutocompleteItem(
            returnType = MessageAttributes.class,
            signature = "attributes()",
            example = "message.attributes()",
            description = "Returns the message attributes belonging to this message.")
    MessageAttributes attributes();

}
