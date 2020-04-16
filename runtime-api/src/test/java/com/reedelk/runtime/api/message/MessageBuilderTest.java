package com.reedelk.runtime.api.message;

import com.reedelk.runtime.api.commons.ImmutableMap;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.content.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class MessageBuilderTest {

    @Test
    void shouldCorrectlySetMessageAttributes() {
        // Given
        MessageAttributes givenAttributes =
                new DefaultMessageAttributes(MyComponent.class, ImmutableMap.of("key1", "value1", "key2", 2));

        // When
        Message message = MessageBuilder.get().attributes(givenAttributes).empty().build();

        // Then
        MessageAttributes attributes = message.attributes();
        assertThat(attributes).containsEntry("key1", "value1");
        assertThat(attributes).containsEntry("key2", 2);
        assertThat(attributes).containsEntry("componentName", MyComponent.class.getSimpleName());
        assertThat(attributes).hasSize(3);
    }

    @Nested
    @DisplayName("XML Payload")
    class XMLPayload {

        @Test
        void shouldCorrectlyBuildXMLPayload() {
            // Given
            String xml = "<node>Value</node>";

            // When
            Message message = MessageBuilder.get().withXml(xml).build();

            // Then
            TypedContent<String, String> content = message.content();
            assertThat(content).isInstanceOf(StringContent.class);
            assertThat(content.data()).isEqualTo(xml);
            assertThat(content.mimeType()).isEqualTo(MimeType.TEXT_XML);
        }

        @Test
        void shouldCorrectlyBuildXMLStreamPayload() {
            // Given
            Publisher<String> xml = Flux.just("<node>", "Value", "</node>");

            // When
            Message message = MessageBuilder.get().withXml(xml).build();

            // Then
            String expectedValue = "<node>Value</node>";

            TypedContent<String, String> content = message.content();
            assertThat(content).isInstanceOf(StringContent.class);
            assertThat(content.data()).isEqualTo(expectedValue);
            assertThat(content.mimeType()).isEqualTo(MimeType.TEXT_XML);
        }
    }

    @Nested
    @DisplayName("HTML Payload")
    class HTMLPayload {

        @Test
        void shouldCorrectlyBuildHTMLPayload() {
            // Given
            String html = "<node>Value</node>";

            // When
            Message message = MessageBuilder.get().withHtml(html).build();

            // Then
            TypedContent<String, String> content = message.content();
            assertThat(content).isInstanceOf(StringContent.class);
            assertThat(content.data()).isEqualTo(html);
            assertThat(content.mimeType()).isEqualTo(MimeType.TEXT_HTML);
        }

        @Test
        void shouldCorrectlyBuildHTMLStreamPayload() {
            // Given
            Publisher<String> html = Flux.just("<node>", "Value", "</node>");

            // When
            Message message = MessageBuilder.get().withHtml(html).build();

            // Then
            String expectedValue = "<node>Value</node>";

            TypedContent<String, String> content = message.content();
            assertThat(content).isInstanceOf(StringContent.class);
            assertThat(content.data()).isEqualTo(expectedValue);
            assertThat(content.mimeType()).isEqualTo(MimeType.TEXT_HTML);
        }
    }

    @Nested
    @DisplayName("TEXT Payload")
    class TextPayload {

        @Test
        void shouldCorrectlyBuildTextPayload() {
            // Given
            String text = "my super text";

            // When
            Message message = MessageBuilder.get().withText(text).build();

            // Then
            TypedContent<String, String> content = message.content();
            assertThat(content).isInstanceOf(StringContent.class);
            assertThat(content.data()).isEqualTo(text);
            assertThat(content.mimeType()).isEqualTo(MimeType.TEXT_PLAIN);
        }

        @Test
        void shouldCorrectlyBuildTextStreamPayload() {
            // Given
            Publisher<String> text = Flux.just("my", "super", "text");

            // When
            Message message = MessageBuilder.get().withText(text).build();

            // Then
            String expectedValue = "mysupertext";

            TypedContent<String, String> content = message.content();
            assertThat(content).isInstanceOf(StringContent.class);
            assertThat(content.data()).isEqualTo(expectedValue);
            assertThat(content.mimeType()).isEqualTo(MimeType.TEXT_PLAIN);
        }
    }

    @Nested
    @DisplayName("JSON Payload")
    class JsonPayload {

        @Test
        void shouldCorrectlyBuildJsonPayload() {
            // Given
            String json = "{'name':'John', 'age': 43 }";

            // When
            Message message = MessageBuilder.get().withJson(json).build();

            // Then
            TypedContent<String, String> content = message.content();
            assertThat(content).isInstanceOf(StringContent.class);
            assertThat(content.data()).isEqualTo(json);
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_JSON);
        }

        @Test
        void shouldCorrectlyBuildJsonStreamPayload() {
            // Given
            Publisher<String> json = Flux.just("{'name':'John'", ", 'age': 43 }");

            // When
            Message message = MessageBuilder.get().withJson(json).build();

            // Then
            String expectedValue = "{'name':'John', 'age': 43 }";

            TypedContent<String, String> content = message.content();
            assertThat(content).isInstanceOf(StringContent.class);
            assertThat(content.data()).isEqualTo(expectedValue);
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_JSON);
        }
    }

    @Nested
    @DisplayName("String Payload")
    class StringPayload {

        @Test
        void shouldCorrectlyBuildStringPayload() {
            // Given
            String csv = "item1,item2,item3";
            MimeType mimeType = MimeType.TEXT_CSV;

            // When
            Message message = MessageBuilder.get().withString(csv, mimeType).build();

            // Then
            TypedContent<String, String> content = message.content();
            assertThat(content).isInstanceOf(StringContent.class);
            assertThat(content.data()).isEqualTo(csv);
            assertThat(content.mimeType()).isEqualTo(MimeType.TEXT_CSV);
        }

        @Test
        void shouldCorrectlyBuildStringStreamPayload() {
            // Given
            Publisher<String> csv = Flux.just("item1,", "item2,", "item3");
            MimeType mimeType = MimeType.TEXT_CSV;

            // When
            Message message = MessageBuilder.get().withString(csv, mimeType).build();

            // Then
            String expectedValue = "item1,item2,item3";

            TypedContent<String, String> content = message.content();
            assertThat(content).isInstanceOf(StringContent.class);
            assertThat(content.data()).isEqualTo(expectedValue);
            assertThat(content.mimeType()).isEqualTo(MimeType.TEXT_CSV);
        }
    }

    @Nested
    @DisplayName("Binary Payload")
    class BinaryPayload {

        @Test
        void shouldCorrectlyBuildBinaryPayload() {
            // Given
            byte[] data = "my sample text".getBytes();

            // When
            Message message = MessageBuilder.get().withBinary(data).build();

            // Then
            TypedContent<byte[], byte[]> content = message.content();
            assertThat(content).isInstanceOf(ByteArrayContent.class);
            assertThat(content.data()).isEqualTo(data);
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_BINARY);
        }

        @Test
        void shouldCorrectlyBuildBinaryPayloadWithMimeType() {
            // Given
            byte[] data = "my sample text".getBytes();
            MimeType mimeType = MimeType.IMAGE_JPEG;

            // When
            Message message = MessageBuilder.get().withBinary(data, mimeType).build();

            // Then
            TypedContent<byte[], byte[]> content = message.content();
            assertThat(content).isInstanceOf(ByteArrayContent.class);
            assertThat(content.data()).isEqualTo(data);
            assertThat(content.mimeType()).isEqualTo(MimeType.IMAGE_JPEG);
        }

        @Test
        void shouldCorrectlyBuildBinaryStreamPayload() {
            // Given
            Publisher<byte[]> binaryStream = Flux.just("my".getBytes(), "sample".getBytes(), "text".getBytes());

            // When
            Message message = MessageBuilder.get().withBinary(binaryStream).build();

            // Then
            byte[] expectedValue = "mysampletext".getBytes();

            TypedContent<byte[], byte[]> content = message.content();
            assertThat(content).isInstanceOf(ByteArrayContent.class);
            assertThat(content.data()).isEqualTo(expectedValue);
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_BINARY);
        }

        @Test
        void shouldCorrectlyBuildBinaryStreamPayloadWithMimeType() {
            // Given
            Publisher<byte[]> binaryStream = Flux.just("my".getBytes(), "sample".getBytes(), "text".getBytes());
            MimeType mimeType = MimeType.IMAGE_JPEG;

            // When
            Message message = MessageBuilder.get().withBinary(binaryStream, mimeType).build();

            // Then
            byte[] expectedValue = "mysampletext".getBytes();

            TypedContent<byte[], byte[]> content = message.content();
            assertThat(content).isInstanceOf(ByteArrayContent.class);
            assertThat(content.data()).isEqualTo(expectedValue);
            assertThat(content.mimeType()).isEqualTo(MimeType.IMAGE_JPEG);
        }

    }

    @Nested
    @DisplayName("Typed Stream")
    class TypedStreamTests {

        @Test
        void shouldCorrectlyBuildStringStreamPayload() {
            // Given
            Publisher<String> csv = Flux.just("item1,", "item2,", "item3");

            // When
            Message message = MessageBuilder.get().withStream(csv, String.class).build();

            // Then
            String expectedValue = "item1,item2,item3";

            TypedContent<String, String> content = message.content();
            assertThat(content).isInstanceOf(StringContent.class);
            assertThat(content.data()).isEqualTo(expectedValue);
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_JAVA);
        }

        @Test
        void shouldCorrectlyBuildStringStreamPayloadWithMimeType() {
            // Given
            Publisher<String> csv = Flux.just("item1,", "item2,", "item3");
            MimeType mimeType = MimeType.TEXT_CSV;

            // When
            Message message = MessageBuilder.get().withStream(csv, String.class, mimeType).build();

            // Then
            String expectedValue = "item1,item2,item3";

            TypedContent<String, String> content = message.content();
            assertThat(content).isInstanceOf(StringContent.class);
            assertThat(content.data()).isEqualTo(expectedValue);
            assertThat(content.mimeType()).isEqualTo(MimeType.TEXT_CSV);
        }

        @Test
        void shouldCorrectlyBuildBinaryStreamPayload() {
            // Given
            Publisher<byte[]> binaryStream = Flux.just("my".getBytes(), "sample".getBytes(), "text".getBytes());

            // When
            Message message = MessageBuilder.get().withStream(binaryStream, byte[].class).build();

            // Then
            byte[] expectedValue = "mysampletext".getBytes();

            TypedContent<byte[], byte[]> content = message.content();
            assertThat(content).isInstanceOf(ByteArrayContent.class);
            assertThat(content.data()).isEqualTo(expectedValue);
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_JAVA);
        }

        @Test
        void shouldCorrectlyBuildCustomObjectStreamPayload() {
            // Given
            Publisher<MyItem> myItemStream = Flux.just(
                    new MyItem("One"),
                    new MyItem("Two"),
                    new MyItem("Three"));

            // When
            Message message = MessageBuilder.get().withStream(myItemStream, MyItem.class).build();

            // Then
            TypedContent<MyItem, List<MyItem>> content = message.content();
            assertThat(content).isInstanceOf(ListContent.class);
            StepVerifier.create(content.stream())
                    .expectNext(new MyItem("One"))
                    .expectNext(new MyItem("Two"))
                    .expectNext(new MyItem("Three"))
                    .verifyComplete();
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_JAVA);
        }
    }

    @Nested
    @DisplayName("Typed Publisher")
    class TypedPublisherTests {

        @Test
        void shouldCorrectlyBuildStringStreamPayload() {
            // Given
            TypedPublisher<String> csv = TypedPublisher.fromString(Flux.just("item1,", "item2,", "item3"));

            // When
            Message message = MessageBuilder.get().withTypedPublisher(csv).build();

            // Then
            String expectedValue = "item1,item2,item3";

            TypedContent<String, String> content = message.content();
            assertThat(content).isInstanceOf(StringContent.class);
            assertThat(content.data()).isEqualTo(expectedValue);
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_JAVA);
        }

        @Test
        void shouldCorrectlyBuildStringStreamPayloadWithMimeType() {
            // Given
            TypedPublisher<String> csv = TypedPublisher.fromString(Flux.just("item1,", "item2,", "item3"));
            MimeType mimeType = MimeType.TEXT_CSV;

            // When
            Message message = MessageBuilder.get().withTypedPublisher(csv, mimeType).build();

            // Then
            String expectedValue = "item1,item2,item3";

            TypedContent<String, String> content = message.content();
            assertThat(content).isInstanceOf(StringContent.class);
            assertThat(content.data()).isEqualTo(expectedValue);
            assertThat(content.mimeType()).isEqualTo(mimeType);
        }
    }

    @Nested
    @DisplayName("Java Object")
    class JavaObject {

        @Test
        void shouldCorrectlyBuildObjectContentPayload() {
            // Given
            MyItem item = new MyItem("Item test");

            // When
            Message message = MessageBuilder.get().withJavaObject(item).build();

            // Then
            TypedContent<MyItem, MyItem> content = message.content();
            assertThat(content).isInstanceOf(ObjectContent.class);
            assertThat(content.data()).isEqualTo(item);
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_JAVA);
        }

        @Test
        void shouldCorrectlyBuildEmptyObjectContentPayload() {
            // Given
            MyItem item = null;

            // When
            Message message = MessageBuilder.get().withJavaObject(item).build();

            // Then
            TypedContent<Void, Void> content = message.content();
            assertThat(content).isInstanceOf(EmptyContent.class);
            assertThat(content.data()).isNull();
            assertThat(content.mimeType()).isNull();
        }

        @Test
        void shouldCorrectlyBuildListContentWhenObjectIsFluxStream() {
            // Given
            Publisher<MyItem> items = Flux.just(new MyItem("One"), new MyItem("Two"));

            // When
            Message message = MessageBuilder.get().withJavaObject(items).build();

            // Then
            TypedContent<MyItem, List<MyItem>> content = message.content();
            assertThat(content).isInstanceOf(ListContent.class);
            assertThat(content.data()).containsExactlyInAnyOrder(new MyItem("One"), new MyItem("Two"));
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_JAVA);
        }

        @Test
        void shouldCorrectlyBuildObjectWhenObjectIsMono() {
            // Given
            MyItem one = new MyItem("One");
            Publisher<MyItem> items = Mono.just(one);

            // When
            Message message = MessageBuilder.get().withJavaObject(items).build();

            // Then
            TypedContent<MyItem, MyItem> content = message.content();
            assertThat(content).isInstanceOf(ObjectContent.class);
            assertThat(content.data()).isEqualTo(one);
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_JAVA);
        }

        @Test
        void shouldCorrectlyBuildStringContent() {
            // Given
            String input = "this is a test";

            // When
            Message message = MessageBuilder.get().withJavaObject(input).build();

            // Then
            TypedContent<String,String> content = message.content();
            assertThat(content).isInstanceOf(StringContent.class);
            assertThat(content.data()).isEqualTo("this is a test");
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_JAVA);
        }

        @Test
        void shouldCorrectlyBuildByteArrayContent() {
            // Given
            byte[] input = "this is a test".getBytes();

            // When
            Message message = MessageBuilder.get().withJavaObject(input).build();

            // Then
            TypedContent<byte[],byte[]> content = message.content();
            assertThat(content).isInstanceOf(ByteArrayContent.class);
            assertThat(content.data()).isEqualTo(input);
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_JAVA);
        }

        @Test
        void shouldCorrectlyBuildByteArrayContentWithMimeType() {
            // Given
            byte[] input = "this is a test".getBytes();
            MimeType mimeType = MimeType.IMAGE_JPEG;

            // When
            Message message = MessageBuilder.get().withJavaObject(input, mimeType).build();

            // Then
            TypedContent<byte[],byte[]> content = message.content();
            assertThat(content).isInstanceOf(ByteArrayContent.class);
            assertThat(content.data()).isEqualTo(input);
            assertThat(content.mimeType()).isEqualTo(MimeType.IMAGE_JPEG);
        }

        @Test
        void shouldCorrectlyBuildObjectContentWithMonoStream() {
            // Given
            Mono<MyItem> itemMono = Mono.just(new MyItem("One"));

            // When
            Message message = MessageBuilder.get().withJavaObject(itemMono,MyItem.class).build();

            // Then
            TypedContent<MyItem,MyItem> content = message.content();
            assertThat(content).isInstanceOf(ObjectContent.class);
            assertThat(content.data()).isEqualTo(new MyItem("One"));
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_JAVA);
        }

        @Test
        void shouldCorrectlyBuildListContentWithList() {
            // Given
            List<MyItem> items = asList(new MyItem("one"), new MyItem("two"));

            // When
            Message message = MessageBuilder.get().withJavaObject(items).build();

            // Then
            TypedContent<MyItem, List<MyItem>> content = message.content();
            assertThat(content).isInstanceOf(ListContent.class);

            List<MyItem> actualData = content.data();
            assertThat(actualData).isEqualTo(items);
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_JAVA);
        }
    }

    @Nested
    @DisplayName("Java Collection")
    class JavaCollection {

        @Test
        void shouldCorrectlyBuildListContentPayload() {
            // Given
            List<MyItem> myCollection = asList(new MyItem("One"), new MyItem("Two"));

            // When
            Message message = MessageBuilder.get().withList(myCollection, MyItem.class).build();

            // Then
            TypedContent<MyItem, List<MyItem>> content = message.content();
            assertThat(content).isInstanceOf(ListContent.class);
            assertThat(content.data()).containsExactlyInAnyOrder(new MyItem("One"), new MyItem("Two"));
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_JAVA);
        }
    }

    static class MyComponent implements ProcessorSync {
        @Override
        public Message apply(FlowContext flowContext, Message message) {
            throw new IllegalStateException("Test only component");
        }
    }

    static class MyItem {
        String name;

        MyItem(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyItem myItem = (MyItem) o;
            return Objects.equals(name, myItem.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
