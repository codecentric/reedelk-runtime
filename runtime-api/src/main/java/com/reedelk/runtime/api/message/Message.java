package com.reedelk.runtime.api.message;


import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.message.content.TypedContent;

import java.io.Serializable;

@AutocompleteType
public interface Message extends Serializable {

    /*
     * This method is needed by Script engine to access this object's properties.
     */
    @AutocompleteItem(returnType = TypedContent.class, replaceValue = "getContent()",
            description = "Returns the message content of this message. " +
                    "The message content contains information about the payload's mime type, data type and stream status.")
    <ItemType,PayloadType, R extends TypedContent<ItemType,PayloadType>> R getContent();

    /**
     *
     * This is a 'nice to have' method to make getting the content more readable
     * from the Script language e.g. message.content() instead of message.getContent().
     */
    @AutocompleteItem(returnType = TypedContent.class, replaceValue = "content()",
            description = "Returns the message content of this message. " +
                    "The message content contains information about the payload's mime type, data type and stream status.")
    <ItemType,PayloadType, R extends TypedContent<ItemType,PayloadType>> R content();

    /**
     * Returns the message payload. This method automatically resolves the payload.
     */
    @AutocompleteItem(returnType = Object.class, replaceValue = "payload()",
            description = "Returns the payload data of this message.")
    <PayloadType> PayloadType payload();

    /**
     * This method is needed by Script engine to access this object's properties.
     * @return the message attributes.
     */
    @AutocompleteItem(replaceValue = "getAttributes()",
            description = "Returns the attributes belonging to this message.")
    MessageAttributes getAttributes();

    /**
     * This is a 'nice to have' method to make getting the attributes more readable
     * from the Script language e.g. message.attributes() instead of message.getAttributes().
     * @return the message attributes.
     */
    @AutocompleteItem(replaceValue = "attributes()",
            description = "Returns the attributes belonging to this message.")
    MessageAttributes attributes();

}
