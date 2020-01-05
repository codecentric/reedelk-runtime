package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.message.content.Parts;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
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

        public static byte[] consume(Publisher<byte[]> stream) {
            List<byte[]> bytesBlocks = Flux.from(stream).collectList().block();
            if (bytesBlocks == null) return new byte[]{};

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                bytesBlocks.forEach(bytesBlock -> {
                    try {
                        out.write(bytesBlock);
                    } catch (IOException e) {
                        throw new ESBException(e);
                    }
                });
                return out.toByteArray();
            } catch (IOException e) {
                throw new ESBException(e);
            }
        }
    }

    public static class FromString {

        private FromString() {
        }

        public static String consume(Publisher<byte[]> byteArrayStream, Charset charset) {
            Flux<String> streamAsString = Flux.from(byteArrayStream)
                    .map(bytes -> new String(bytes, charset));
            return consume(streamAsString);
        }

        public static String consume(Publisher<String> stream) {
            List<String> result = Flux.from(stream).collectList().block();
            return result == null ? null : String.join(StringUtils.EMPTY, result);
        }
    }

    public static class FromParts {

        private FromParts() {
        }

        public static Parts consume(Publisher<Parts> stream) {
            return Mono.from(stream).block();
        }
    }
}
