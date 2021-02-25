package de.codecentric.reedelk.runtime.api.commons;

import de.codecentric.reedelk.runtime.api.message.content.TypedPublisher;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StreamUtils {

    private StreamUtils() {
    }

    public static class FromByteArray {

        private FromByteArray() {
        }

        public static Publisher<String> asStringStream(Publisher<byte[]> byteArrayStream, Charset charset) {
            return Flux.from(byteArrayStream).map(bytes -> {
                Charset conversionCharset = Optional.ofNullable(charset).orElseGet(Charset::defaultCharset);
                return new String(bytes, conversionCharset);
            });
        }

        public static Publisher<String> asStringStream(Publisher<byte[]> byteArrayStream) {
            return asStringStream(byteArrayStream, Charset.defaultCharset());
        }

        public static TypedPublisher<?> fromMimeType(Publisher<byte[]> byteArrayStream, MimeType mimeType) {
            if (String.class == mimeType.javaType()) {
                // If it  is a String, then we check if the charset is present
                // in the mime type to be used for the string conversion.
                Charset charset = mimeType.getCharset().orElse(null);
                // Map each byte array of the stream to a string
                Publisher<String> streamAsString = StreamUtils.FromByteArray.asStringStream(byteArrayStream, charset);
                // The typed publisher is a string type.
                return TypedPublisher.fromString(streamAsString);
            } else {
                return TypedPublisher.fromByteArray(byteArrayStream);
            }
        }

        public static byte[] consume(Publisher<byte[]> stream) {
            List<byte[]> bytesBlocks = Flux.from(stream).collectList().block();
            if (bytesBlocks == null || bytesBlocks.size() == 0) return new byte[0];
            if (bytesBlocks.size() == 1) return bytesBlocks.get(0);

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                bytesBlocks.forEach(bytesBlock -> {
                    try {
                        out.write(bytesBlock);
                    } catch (IOException exception) {
                        throw new PlatformException(exception);
                    }
                });
                return out.toByteArray();
            } catch (IOException exception) {
                throw new PlatformException(exception);
            }
        }
    }

    public static class FromString {

        private FromString() {
        }

        public static String consume(Publisher<byte[]> byteArrayStream, Charset charset) {
            byte[] byteArray = FromByteArray.consume(byteArrayStream);
            return new String(byteArray, charset);
        }

        public static String consume(Publisher<String> stream) {
            List<String> result = Flux.from(stream).collectList().block();
            return result == null ? null : String.join(StringUtils.EMPTY, result);
        }
    }

    public static class FromObject {

        private FromObject() {
        }

        public static <T> List<T> consume(Publisher<T> stream) {
            List<T> result = Flux.from(stream).collectList().block();
            return result == null ? new ArrayList<>() : result;
        }
    }
}
