package com.reedelk.runtime.api.message;


import com.reedelk.runtime.api.message.content.TypedContent;

import java.io.Serializable;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class Message implements Serializable {

    private final MessageAttributes attributes;
    private final TypedContent<?,?> content;

    Message(TypedContent<?,?> content, MessageAttributes attributes) {
        this.content = content;
        this.attributes = attributes;
    }

    // This method is needed by Script engine to access this object's properties.
    public <ItemType,PayloadType,R extends TypedContent<ItemType,PayloadType>> R getContent() {
        return (R) content;
    }

    // This is a 'nice to have' method to make getting the content more readable
    // from the Script language e.g. message.content() instead of message.getContent().
    public <ItemType,PayloadType,R extends TypedContent<ItemType,PayloadType>> R content() {
        return (R) content;
    }

    public <PayloadType> PayloadType payload() {
        return (PayloadType) Optional.ofNullable(content)
                .map(TypedContent::data)
                .orElse(null);
    }

    // This method is needed by Script engine to access this object's properties.
    public MessageAttributes getAttributes() {
        return attributes;
    }

    // This is a 'nice to have' method to make getting the attributes more readable
    // from the Script language e.g. message.attributes() instead of message.getAttributes().
    public MessageAttributes attributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content=" + content +
                ", attributes=" + attributes +
                '}';
    }
}