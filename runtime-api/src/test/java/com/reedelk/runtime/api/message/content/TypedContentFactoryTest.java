package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.message.content.factory.TypedContentFactory;
import com.reedelk.runtime.api.message.content.utils.TypedMono;
import com.reedelk.runtime.api.message.content.utils.TypedPublisher;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

class TypedContentFactoryTest {

    @Test
    void shouldCreateTypedContentFromParts() {
        // Given
        Parts parts = new Parts();

        // When
        TypedContent<?> content = TypedContentFactory.from(parts, MimeType.MULTIPART_FORM_DATA);

        // Then
        assertThat(content).isInstanceOf(MultipartContent.class);

        MultipartContent multipartContent = (MultipartContent) content;
        assertThat(multipartContent.data()).isEqualTo(parts);
        assertThat(multipartContent.mimeType()).isEqualTo(MimeType.MULTIPART_FORM_DATA);
    }

    @Test
    void shouldCreateEmptyContentFromNull() {
        // Given
        byte[] object = null;

        // When
        TypedContent<?> content = TypedContentFactory.from(object, MimeType.APPLICATION_BINARY);

        // Then
        assertThat(content).isInstanceOf(EmptyContent.class);

        EmptyContent emptyContent = (EmptyContent) content;
        assertThat(emptyContent.data()).isNull();
        assertThat(emptyContent.mimeType()).isEqualTo(MimeType.APPLICATION_BINARY);
    }

    @Test
    void shouldReturnTypedContentIfContentIsAlreadyTypedContent() {
        // Given
        ByteArrayContent byteArrayContent = new ByteArrayContent(new byte[0], MimeType.TEXT);

        // When
        TypedContent<?> content = TypedContentFactory.from(byteArrayContent, MimeType.TEXT_JAVASCRIPT);

        // Then
        assertThat(content).isEqualTo(byteArrayContent);
    }

    @Test
    void shouldReturnByteArrayContentFromByteArray() {
        // Given
        byte[] messageAsBytes = "this is a test".getBytes();

        // When
        TypedContent<?> content = TypedContentFactory.from(messageAsBytes, MimeType.TEXT);

        // Then
        assertThat(content).isInstanceOf(ByteArrayContent.class);

        ByteArrayContent byteArrayContent = (ByteArrayContent) content;
        assertThat(byteArrayContent.data()).isEqualTo(messageAsBytes);
        assertThat(byteArrayContent.mimeType()).isEqualTo(MimeType.TEXT);
    }

    @Test
    void shouldReturnStringContentFromString() {
        // Given
        String message = "{'name':'john'}";

        // When
        TypedContent<?> content = TypedContentFactory.from(message, MimeType.APPLICATION_JSON);

        // Then
        assertThat(content).isInstanceOf(StringContent.class);

        StringContent stringContent = (StringContent) content;
        assertThat(stringContent.data()).isEqualTo(message);
        assertThat(stringContent.mimeType()).isEqualTo(MimeType.APPLICATION_JSON);
    }

    @Test
    void shouldReturnObjectContentFromObject() {
        // Given
        MyTestClass myTestObject = new MyTestClass();

        // When
        TypedContent<?> content = TypedContentFactory.from(myTestObject, MimeType.ANY);

        // Then
        assertThat(content).isInstanceOf(ObjectContent.class);

        ObjectContent objectContent = (ObjectContent) content;
        assertThat(objectContent.data()).isEqualTo(myTestObject);
        assertThat(objectContent.type()).isEqualTo(MyTestClass.class);
        assertThat(objectContent.mimeType()).isEqualTo(MimeType.ANY);
    }

    @Test
    void shouldReturnStringContentFromStringTypedPublisher() {
        // Given
        TypedPublisher<String> stringStream = TypedPublisher.fromString(Mono.just("my test"));

        // When
        TypedContent<?> content = TypedContentFactory.from(stringStream, MimeType.TEXT);

        // Then
        assertThat(content).isInstanceOf(StringContent.class);

        StringContent stringContent = (StringContent) content;
        assertThat(stringContent.data()).isEqualTo("my test");
        assertThat(stringContent.mimeType()).isEqualTo(MimeType.TEXT);
    }

    @Test
    void shouldReturnByteArrayContentFromByteArrayTypedPublisher() {
        // Given
        byte[] payload = "test message".getBytes();
        TypedPublisher<byte[]> byteArrayStream = TypedMono.just(payload);

        // When
        TypedContent<?> content = TypedContentFactory.from(byteArrayStream, MimeType.TEXT);

        // Then
        assertThat(content).isInstanceOf(ByteArrayContent.class);

        ByteArrayContent byteArrayContent = (ByteArrayContent) content;
        assertThat(byteArrayContent.data()).isEqualTo(payload);
        assertThat(byteArrayContent.mimeType()).isEqualTo(MimeType.TEXT);
    }

    private class MyTestClass {
    }
}