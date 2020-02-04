package com.reedelk.runtime.api.message;


import com.reedelk.runtime.api.message.content.TypedContent;

import java.io.Serializable;
import java.util.Optional;

public class Message implements Serializable {

    private final TypedContent<?,?> content;
    private final MessageAttributes attributes;

    Message(TypedContent<?,?> content, MessageAttributes attributes) {
        this.content = content;
        this.attributes = attributes;
    }

    // This method is needed by Script engine to access this object's properties.
    public TypedContent<?,?> getContent() {
        return content;
    }

    // This is a 'nice to have' method to make getting the content more readable
    // from the Script language e.g. message.content() instead of message.getContent().
    public TypedContent<?,?> content() {
        return content;
    }

    @SuppressWarnings("unchecked")
    public <T> T payload() {
        return (T) Optional.ofNullable(content)
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