package com.reedelk.runtime.api.message;

import com.reedelk.runtime.api.message.content.TypedContent;

import java.util.Optional;

@SuppressWarnings("unchecked")
public class MessageDefault implements Message {

    private final MessageAttributes attributes;
    private final TypedContent<?,?> content;

    MessageDefault(TypedContent<?,?> content, MessageAttributes attributes) {
        this.content = content;
        this.attributes = attributes;
    }

    @Override
    public <T, StreamType, U extends TypedContent<T, StreamType>> U getContent() {
        return (U) content;
    }

    @Override
    public <T, StreamType, U extends TypedContent<T, StreamType>> U content() {
        return (U) content;
    }

    @Override
    public <T> T payload() {
        return (T) Optional.ofNullable(content)
                .map(TypedContent::data)
                .orElse(null);
    }

    @Override
    public MessageAttributes getAttributes() {
        return attributes;
    }

    @Override
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
