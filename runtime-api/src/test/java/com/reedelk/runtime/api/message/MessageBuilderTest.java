package com.reedelk.runtime.api.message;

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

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class MessageBuilderTest {

    @Test
    void shouldCorrectlySetMessageAttributes() {
        // Given
        class MyAttributes extends MessageAttributes {
            public MyAttributes() {
                put("key1", "value1");
                put("key2", 2);
            }
        }

        MessageAttributes givenAttributes = new MyAttributes();
        
        // When
        Message message = MessageBuilder.get(MyComponent.class).attributes(givenAttributes).empty().build();

        // Then
        MessageAttributes attributes = message.attributes();
        assertThat(attributes).containsEntry("key1", "value1");
        assertThat(attributes).containsEntry("key2", 2);
        assertThat(attributes).containsEntry("component", MyComponent.class.getName());
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
            Message message = MessageBuilder.get(MyComponent.class).withXml(xml).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withXml(xml).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withHtml(html).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withHtml(html).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withText(text).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withText(text).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withJson(json).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withJson(json).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withString(csv, mimeType).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withString(csv, mimeType).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withBinary(data).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withBinary(data, mimeType).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withBinary(binaryStream).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withBinary(binaryStream, mimeType).build();

            // Then
            byte[] expectedValue = "mysampletext".getBytes();

            TypedContent<byte[], byte[]> content = message.content();
            assertThat(content).isInstanceOf(ByteArrayContent.class);
            assertThat(content.data()).isEqualTo(expectedValue);
            assertThat(content.mimeType()).isEqualTo(MimeType.IMAGE_JPEG);
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
            Message message = MessageBuilder.get(MyComponent.class).withTypedPublisher(csv).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withTypedPublisher(csv, mimeType).build();

            // Then
            String expectedValue = "item1,item2,item3";

            TypedContent<String, String> content = message.content();
            assertThat(content).isInstanceOf(StringContent.class);
            assertThat(content.data()).isEqualTo(expectedValue);
            assertThat(content.mimeType()).isEqualTo(mimeType);
        }

        @SuppressWarnings("rawtypes")
        @Test
        void shouldCorrectlyBuildListContentFromTypedPublisher() {
            // Given
            DataRow<Serializable> row1 = DefaultDataRow.create(asList("column1", "column2"), asList(1, 2));
            DataRow<Serializable> row2 = DefaultDataRow.create(asList("column1", "column2"), asList(1, 2));
            TypedPublisher<DataRow> stream = TypedPublisher.from(Flux.just(row1, row2), DataRow.class);

            // When
            Message message = MessageBuilder.get(MyComponent.class).withTypedPublisher(stream).build();

            // Then
            TypedContent<List<DataRow>, DataRow> content = message.content();
            assertThat(content).isInstanceOf(ListContent.class);
            assertThat(content.isStream()).isTrue();
            assertThat(content.getStreamType()).isEqualTo(DataRow.class);
            assertThat(content.getType()).isEqualTo(List.class);
            assertThat(content.mimeType()).isEqualTo(MimeType.APPLICATION_JAVA);
            assertThat(content.data()).isEqualTo(Arrays.asList(row1, row2));
        }

        @Test
        void shouldCorrectlyBuildByteStreamPayloadWithMimeType() {
            // Given
            byte[] bytes1 = "item1,".getBytes();
            byte[] bytes2 = "item2,".getBytes();
            TypedPublisher<byte[]> byteStream = TypedPublisher.fromByteArray(Flux.just(bytes1, bytes2));
            MimeType mimeType = MimeType.APPLICATION_BINARY;

            // When
            Message message = MessageBuilder.get(MyComponent.class).withTypedPublisher(byteStream, mimeType).build();

            // Then
            TypedContent<byte[], byte[]> content = message.content();
            assertThat(content).isInstanceOf(ByteArrayContent.class);
            assertThat(content.mimeType()).isEqualTo(mimeType);
            StepVerifier.create(content.stream())
                    .expectNext(bytes1)
                    .expectNext(bytes2)
                    .verifyComplete();
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
            Message message = MessageBuilder.get(MyComponent.class).withJavaObject(item).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withJavaObject(item).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withJavaObject(items).build();

            // Then
            TypedContent<List<MyItem>, MyItem> content = message.content();
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
            Message message = MessageBuilder.get(MyComponent.class).withJavaObject(items).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withJavaObject(input).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withJavaObject(input).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withJavaObject(input, mimeType).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withJavaObject(itemMono,MyItem.class).build();

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
            Message message = MessageBuilder.get(MyComponent.class).withJavaObject(items).build();

            // Then
            TypedContent<List<MyItem>, MyItem> content = message.content();
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
            Message message = MessageBuilder.get(MyComponent.class).withList(myCollection, MyItem.class).build();

            // Then
            TypedContent<List<MyItem>, MyItem> content = message.content();
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
