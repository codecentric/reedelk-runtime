package de.codecentric.reedelk.runtime.api.message;


import de.codecentric.reedelk.runtime.api.annotation.Type;
import de.codecentric.reedelk.runtime.api.annotation.TypeFunction;
import de.codecentric.reedelk.runtime.api.message.content.TypedContent;

import java.io.Serializable;

@Type(description = "The Message encapsulates data and " +
                "attributes passing through the components of a flow.")
public interface Message extends Serializable {

    /**
     * Returns the message payload. This method automatically consumes
     * the payload stream if it is a stream.
     */
    default <T> T getPayload() {
        return payload();
    }

    /**
     * Returns the message payload. This method automatically consumes
     * the payload stream if it is a stream.
     */
    @TypeFunction(
            returnType = MessagePayload.class,
            signature = "payload()",
            example = "message.payload()",
            description = "Returns the payload (data) of the message. " +
                    "The payload could be a text, a byte array, a collection " +
                    "and so on depending on the component which generated it.")
    <T> T payload();

    /*
     * This method is needed by Script engine to access this object's properties.
     */
    <T, StreamType, U extends TypedContent<T, StreamType>> U getContent();

    /**
     *
     * This is a 'nice to have' method to make getting the content more readable
     * from the Script language e.g. message.content() instead of message.getContent().
     */
    @TypeFunction(
            returnType = TypedContent.class,
            signature = "content()",
            example = "message.content()",
            description = "Returns the content descriptor of this message. " +
                    "The message content contains information about the payload's mime type, " +
                    "data type and stream status.")
    <T, StreamType, U extends TypedContent<T, StreamType>> U content();

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
    @TypeFunction(
            returnType = MessageAttributes.class,
            signature = "attributes()",
            example = "message.attributes()",
            description = "Returns the message attributes belonging to this message.")
    MessageAttributes attributes();

}
