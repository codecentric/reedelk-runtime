package com.reedelk.runtime.api.message;


import com.reedelk.runtime.api.message.content.TypedContent;

import java.io.Serializable;

public interface Message extends Serializable {

    String TYPE = "Message";
    String TYPE_ARRAY = "Message[]";


    /**
     * This method is needed by Script engine to access this object's properties.
     */
    <ItemType,PayloadType,R extends TypedContent<ItemType,PayloadType>> R getContent();

    /**
     *
     * This is a 'nice to have' method to make getting the content more readable
     * from the Script language e.g. message.content() instead of message.getContent().
     */
    <ItemType,PayloadType,R extends TypedContent<ItemType,PayloadType>> R content();

    /**
     * Returns the message payload. This method automatically resolves the payload.
     */
    <PayloadType> PayloadType payload();

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
    MessageAttributes attributes();

}
