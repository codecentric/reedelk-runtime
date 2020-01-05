package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.message.content.ByteArrayContent;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.StringContent;
import com.reedelk.runtime.api.message.content.TypedContent;
import org.reactivestreams.Publisher;

import java.nio.charset.Charset;

public class TypedContentUtils {

    public static TypedContent<?> from(byte[] byteArray, MimeType mimeType) {
        if (String.class == JavaType.from(mimeType)) {
            // If it  is a String, then we check if the charset is present
            // in the mime type to be used for the string conversion.
            Charset charset = mimeType.getCharset().orElseGet(Charset::defaultCharset);
            // Convert the byte array to a string with the given charset.
            String content = new String(byteArray, charset);
            // The TypedContent is a String.
            return new StringContent(content, mimeType);
        } else {
            // The TypeContent is byte array
            return new ByteArrayContent(byteArray, mimeType);
        }
    }

    public static TypedContent<?> from(Publisher<byte[]> byteArrayStream, MimeType mimeType) {
        if (String.class == JavaType.from(mimeType)) {
            // If it  is a String, then we check if the charset is present
            // in the mime type to be used for the string conversion.
            Charset charset = mimeType.getCharset().orElse(null);
            // Map each byte array of the stream to a string
            Publisher<String> streamAsString = StreamUtils.FromByteArray.asStringStream(byteArrayStream, charset);
            // The TypedContent is String stream.
            return new StringContent(streamAsString, mimeType);
        } else {
            return new ByteArrayContent(byteArrayStream, mimeType);
        }
    }
}