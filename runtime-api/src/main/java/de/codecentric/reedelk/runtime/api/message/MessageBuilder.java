package de.codecentric.reedelk.runtime.api.message;

import de.codecentric.reedelk.runtime.api.commons.Preconditions;
import de.codecentric.reedelk.runtime.api.message.content.*;
import de.codecentric.reedelk.runtime.api.message.content.*;
import de.codecentric.reedelk.runtime.api.component.Component;
import de.codecentric.reedelk.runtime.api.message.content.*;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class MessageBuilder {

    private TypedContent<?,?> typedContent;
    private MessageAttributes attributes;
    private final Class<? extends Component> component;

    private MessageBuilder(Class<? extends Component> component) {
        Preconditions.checkNotNull(component, "component");
        this.component = component;
    }

    public static MessageBuilder get(Class<? extends Component> component) {
        return new MessageBuilder(component);
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
        this.typedContent = new StringContent(text, MimeType.TEXT_PLAIN);
        return this;
    }

    public MessageBuilder withText(Publisher<String> textStream) {
        this.typedContent = new StringContent(textStream, MimeType.TEXT_PLAIN);
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

    // TYPED PUBLISHER

    public <StreamType> MessageBuilder withTypedPublisher(TypedPublisher<StreamType> typedPublisher) {
        withStream(typedPublisher, typedPublisher.getType(), MimeType.APPLICATION_JAVA);
        return this;
    }

    public <StreamType> MessageBuilder withTypedPublisher(TypedPublisher<StreamType> typedPublisher, MimeType mimeType) {
        withStream(typedPublisher, typedPublisher.getType(), mimeType);
        return this;
    }

    @SuppressWarnings("unchecked")
    private <StreamType> void withStream(TypedPublisher<StreamType> typedStream, Class<StreamType> clazz, MimeType mimeType) {
        if (String.class.equals(clazz)) {
            TypedPublisher<String> stringStream = (TypedPublisher<String>) typedStream;
            this.typedContent = new StringContent(stringStream, mimeType);
        } else if (byte[].class.equals(clazz)) {
            TypedPublisher<byte[]> byteArrayStream = (TypedPublisher<byte[]>) typedStream;
            this.typedContent = new ByteArrayContent(byteArrayStream, mimeType);
        } else if (Byte[].class.equals(clazz)) {
            TypedPublisher<Byte[]> byteArrayStream = (TypedPublisher<Byte[]>) typedStream;
            Publisher<byte[]> byteArray = Flux.from(byteArrayStream).map(ByteArrayContent::toPrimitives);
            this.typedContent = new ByteArrayContent(byteArray, mimeType);
        } else {
            this.typedContent = new ListContent<>(typedStream, clazz);
        }
    }

    // JAVA OBJECT

    public MessageBuilder withJavaObject(Object object) {
        withJavaObject(object, MimeType.APPLICATION_JAVA);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <ItemType> MessageBuilder withJavaObject(Mono<ItemType> monoStream, Class<ItemType> type) {
        if (String.class.equals(type)) {
            Mono<String> monoAsString = (Mono<String>) monoStream;
            withString(monoAsString, MimeType.TEXT_PLAIN);
        } else if (byte[].class.equals(type) || Byte[].class.equals(type)) {
            Mono<byte[]> monoAsBytes = (Mono<byte[]>) monoStream;
            withBinary(monoAsBytes, MimeType.APPLICATION_BINARY);
        } else {
            this.typedContent = new ObjectContent<>(monoStream, type);
        }
        return this;
    }

    /**
     * Note that the mime type is only used for non Java only types, e.g byte array and String.
     * For Java only types the mime type used is only MimeType.APPLICATION_JAVA. This is because
     * it would not make sense to have a Java object with mime type MimeType.APPLICATION_JSON.
     */
    @SuppressWarnings("unchecked")
    public MessageBuilder withJavaObject(Object object, MimeType mimeType) {
        if (object == null) {
            this.typedContent = new EmptyContent();
        } else if (object instanceof Flux) {
            Flux<Object> objectStream = (Flux<Object>) object;
            this.typedContent = new ListContent<>(objectStream, Object.class);
        } else if (object instanceof Mono) {
            // A mono is considered a single object content. This is needed for instance when
            // we have Attachments map from REST Listener. The REST Listener mapper forces us
            // to have a map of Attachments inside a Mono.
            Mono<Object> objectStream = (Mono<Object>) object;
            this.typedContent = new ObjectContent<>(objectStream, Object.class);
        } else if (object instanceof String) {
            this.typedContent = new StringContent((String) object, mimeType);
        } else if (object instanceof byte[]) {
            this.typedContent = new ByteArrayContent((byte[]) object, mimeType);
        } else if (object instanceof Byte[]) {
            this.typedContent = new ByteArrayContent((Byte[]) object, mimeType);
        } else if (object instanceof List) {
            List<Object> list = (List<Object>) object;
            this.typedContent = new ListContent<>(list, Object.class);
        } else {
            this.typedContent = new ObjectContent<>(object);
        }
        return this;
    }

    // TYPED CONTENT

    public MessageBuilder withTypedContent(TypedContent<?,?> typedContent) {
        this.typedContent = typedContent;
        return this;
    }

    // LIST

    public <ItemType> MessageBuilder withList(List<ItemType> list, Class<ItemType> listItemType) {
        this.typedContent = new ListContent<>(list, listItemType);
        return this;
    }

    // EMPTY CONTENT

    public MessageBuilder empty() {
        this.typedContent = new EmptyContent();
        return this;
    }

    public MessageBuilder attributes(MessageAttributes attributes) {
        this.attributes = attributes;
        return this;
    }

    public Message build() {
        MessageAttributes messageAttributes =
                attributes == null ? new MessageAttributes() : attributes;
        messageAttributes.setComponent(component);

        if (typedContent == null) {
            throw new IllegalStateException("Typed content missing");
        }
        return new MessageDefault(typedContent, messageAttributes);
    }
}
