package com.reedelk.runtime.api.message;

import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedContent;
import com.reedelk.runtime.api.message.content.factory.TypedContentFactory;
import com.reedelk.runtime.api.message.content.utils.TypedPublisher;
import org.reactivestreams.Publisher;

public class MessageBuilder {

    private Object content;
    private MimeType mimeType;
    private TypedContent<?> typedContent;
    private MessageAttributes attributes;

    private MessageBuilder() {
    }

    public static MessageBuilder get() {
        return new MessageBuilder();
    }

    // XML
    
    public MessageBuilder withXml(String xml) {
        this.mimeType = MimeType.TEXT_XML;
        this.content = xml;
        return this;
    }

    public MessageBuilder withXml(Publisher<String> xmlStream) {
        this.mimeType = MimeType.TEXT_XML;
        this.content = TypedPublisher.fromString(xmlStream);
        return this;
    }

    // HTML
    
    public MessageBuilder withHtml(String html) {
        this.mimeType = MimeType.TEXT_HTML;
        this.content = html;
        return this;
    }

    public MessageBuilder withHtml(Publisher<String> htmlStream) {
        this.mimeType = MimeType.TEXT_HTML;
        this.content = TypedPublisher.fromString(htmlStream);
        return this;
    }

    // TEXT
    
    public MessageBuilder withText(String text) {
        this.mimeType = MimeType.TEXT;
        this.content = text;
        return this;
    }

    public MessageBuilder withText(Publisher<String> textStream) {
        this.mimeType = MimeType.TEXT;
        this.content = TypedPublisher.fromString(textStream);
        return this;
    }

    // JSON

    public MessageBuilder withJson(String json) {
        this.mimeType = MimeType.APPLICATION_JSON;
        this.content = json;
        return this;
    }

    public MessageBuilder withJson(Publisher<String> jsonStream) {
        this.mimeType = MimeType.APPLICATION_JSON;
        this.content = TypedPublisher.fromString(jsonStream);
        return this;
    }

    public MessageBuilder withBinary(byte[] bytes) {
        this.mimeType = MimeType.APPLICATION_BINARY;
        this.content = bytes;
        return this;
    }

    public MessageBuilder withBinary(Publisher<byte[]> bytesStream) {
        this.mimeType = MimeType.APPLICATION_BINARY;
        this.content = TypedPublisher.fromByteArray(bytesStream);
        return this;
    }

    public MessageBuilder withJavaObject(Object object) {
        this.mimeType = MimeType.APPLICATION_JAVA;
        this.content = object;
        return this;
    }

    public MessageBuilder empty() {
        return this;
    }

    public MessageBuilder mimeType(MimeType mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public MessageBuilder typedContent(TypedContent typedContent) {
        this.typedContent = typedContent;
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
            typedContent = TypedContentFactory.from(content, mimeType);
        }
        return new Message(typedContent, attributes);
    }
}
