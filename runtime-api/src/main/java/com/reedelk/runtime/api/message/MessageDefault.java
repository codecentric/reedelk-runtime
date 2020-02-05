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
    public <ItemType,PayloadType,R extends TypedContent<ItemType,PayloadType>> R getContent() {
        return (R) content;
    }

    @Override
    public <ItemType,PayloadType,R extends TypedContent<ItemType,PayloadType>> R content() {
        return (R) content;
    }

    @Override
    public <PayloadType> PayloadType payload() {
        return (PayloadType) Optional.ofNullable(content)
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