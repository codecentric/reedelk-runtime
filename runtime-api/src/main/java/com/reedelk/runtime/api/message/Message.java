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
     * Returns the message payload. This method automatically resolves the payload.
     */
    @AutocompleteItem(
            returnType = Object.class,
            signature = "payload()",
            example = "message.payload()",
            description = "Returns the payload (data) of the message. " +
                    "The payload could be a text, a byte array, a collection " +
                    "and so on depending on the component which generated it.")
    <PayloadType> PayloadType payload();

    /*
     * This method is needed by Script engine to access this object's properties.
     */
    <ItemType,PayloadType, R extends TypedContent<ItemType,PayloadType>> R getContent();

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
    <ItemType,PayloadType, R extends TypedContent<ItemType,PayloadType>> R content();

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
