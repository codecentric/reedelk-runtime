package com.reedelk.runtime.api.message;

import com.reedelk.runtime.api.message.content.*;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Collection;

public class MessageBuilder {

    private MessageAttributes attributes;
    private TypedContent<?,?> typedContent;

    private MessageBuilder() {
    }

    public static MessageBuilder get() {
        return new MessageBuilder();
    }

    // XML

    public MessageBuilder withXml(String xml) {
        this.typedContent = new StringContent(xml, MimeType.TEXT_XML);
        return this;
    }

    public MessageBuilder withXml(Publisher<String> xmlStream) {
        this.typedContent = new StringContent(xmlStream, MimeType.TEXT_XML);
        return this;
    }

    // HTML

    public MessageBuilder withHtml(String html) {
        this.typedContent = new StringContent(html, MimeType.TEXT_HTML);
        return this;
    }

    public MessageBuilder withHtml(Publisher<String> htmlStream) {
        this.typedContent = new StringContent(htmlStream, MimeType.TEXT_HTML);
        return this;
    }

    // TEXT

    public MessageBuilder withText(String text) {
        this.typedContent = new StringContent(text, MimeType.TEXT);
        return this;
    }

    public MessageBuilder withText(Publisher<String> textStream) {
        this.typedContent = new StringContent(textStream, MimeType.TEXT);
        return this;
    }

    // JSON

    public MessageBuilder withJson(String json) {
        this.typedContent = new StringContent(json, MimeType.APPLICATION_JSON);
        return this;
    }

    public MessageBuilder withJson(Publisher<String> jsonStream) {
        this.typedContent = new StringContent(jsonStream, MimeType.APPLICATION_JSON);
        return this;
    }

    // STRING

    public MessageBuilder withString(String value, MimeType mimeType) {
        this.typedContent = new StringContent(value, mimeType);
        return this;
    }

    public MessageBuilder withString(Publisher<String> valueStream, MimeType mimeType) {
        this.typedContent = new StringContent(valueStream, mimeType);
        return this;
    }

    // BINARY

    public MessageBuilder withBinary(byte[] bytes) {
        this.typedContent = new ByteArrayContent(bytes, MimeType.APPLICATION_BINARY);
        return this;
    }

    public MessageBuilder withBinary(byte[] bytes, MimeType mimeType) {
        this.typedContent = new ByteArrayContent(bytes, mimeType);
        return this;
    }

    public MessageBuilder withBinary(Publisher<byte[]> bytesStream) {
        this.typedContent = new ByteArrayContent(bytesStream, MimeType.APPLICATION_BINARY);
        return this;
    }

    public MessageBuilder withBinary(Publisher<byte[]> bytesStream, MimeType mimeType) {
        this.typedContent = new ByteArrayContent(bytesStream, mimeType);
        return this;
    }

    // TYPED STREAM

    @SuppressWarnings("unchecked")
    public <ItemType> MessageBuilder withStream(Publisher<ItemType> typedStream, Class<ItemType> clazz, MimeType mimeType) {
        if (String.class.equals(clazz)) {
            Publisher<String> stringStream = (Publisher<String>) typedStream;
            this.typedContent = new StringContent(stringStream, mimeType);
        } else if (byte[].class.equals(clazz)) {
            Publisher<byte[]> byteArrayStream = (Publisher<byte[]>) typedStream;
            this.typedContent = new ByteArrayContent(byteArrayStream, mimeType);
        } else {
            this.typedContent = new ObjectCollectionContent<>(typedStream, clazz, mimeType);
        }
        return this;
    }

    public <ItemType> MessageBuilder withStream(Publisher<ItemType> typedStream, Class<ItemType> clazz) {
        withStream(typedStream, clazz, MimeType.APPLICATION_JAVA);
        return this;
    }

    // TYPED PUBLISHER

    public <ItemType> MessageBuilder withTypedPublisher(TypedPublisher<ItemType> typedPublisher) {
        withStream(typedPublisher, typedPublisher.getType());
        return this;
    }

    public <ItemType> MessageBuilder withTypedPublisher(TypedPublisher<ItemType> typedPublisher, MimeType mimeType) {
        withStream(typedPublisher, typedPublisher.getType(), mimeType);
        return this;
    }

    // JAVA OBJECT

    public MessageBuilder withJavaObject(Object object) {
        withJavaObject(object, MimeType.APPLICATION_JAVA);
        return this;
    }

    @SuppressWarnings("unchecked")
    public MessageBuilder withJavaObject(Object object, MimeType mimeType) {
        if (object == null) {
            empty();
        } else if (object instanceof Publisher) {
            Publisher<Object> objectStream = (Publisher<Object>) object;
            this.typedContent = new ObjectCollectionContent<>(objectStream, Object.class, mimeType);
        } else if (object instanceof String) {
            this.typedContent = new StringContent((String) object, mimeType);
        } else if (object instanceof byte[]) {
            this.typedContent = new ByteArrayContent((byte[]) object, mimeType);
        } else {
            this.typedContent = new ObjectContent<>(object, mimeType);
        }
        return this;
    }

    public <ItemType> MessageBuilder withJavaObject(Mono<ItemType> monoStream, Class<ItemType> type, MimeType mimeType) {
        this.typedContent = new ObjectContent<>(monoStream, type, mimeType);
        return this;
    }

    public <ItemType> MessageBuilder withJavaObject(Mono<ItemType> monoStream, Class<ItemType> type) {
        this.typedContent = new ObjectContent<>(monoStream, type, MimeType.APPLICATION_JAVA);
        return this;
    }

    // JAVA COLLECTION

    public <ItemType> MessageBuilder withJavaCollection(Collection<ItemType> collection, Class<ItemType> collectionType) {
        this.typedContent = new ObjectCollectionContent<>(collection, collectionType, MimeType.APPLICATION_JAVA);
        return this;
    }

    public <ItemType> MessageBuilder withJavaCollection(Collection<ItemType> collection, Class<ItemType> collectionType, MimeType mimeType) {
        this.typedContent = new ObjectCollectionContent<>(collection, collectionType, mimeType);
        return this;
    }

    public MessageBuilder empty() {
        this.typedContent = new EmptyContent();
        return this;
    }

    public MessageBuilder empty(MimeType mimeType) {
        this.typedContent = new EmptyContent(mimeType);
        return this;
    }

    public MessageBuilder attributes(MessageAttributes attributes) {
        this.attributes = attributes;
        return this;
    }

    public Message build() {
        if (attributes == null) {
            attributes = DefaultMessageAttributes.empty();
        }
        if (typedContent == null) {
            throw new IllegalStateException("Typed content missing");
        }
        return new MessageDefault(typedContent, attributes);
    }
}
