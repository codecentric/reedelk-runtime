package com.reedelk.esb.services.resource;

import com.reedelk.esb.commons.ResourcePath;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static com.reedelk.runtime.api.commons.FileUtils.ReadFromURL;

/**
 * A Resource Loader which lazy load resources from the given URL.
 */
public class ResourceLoader {

    public static final int DEFAULT_BUFFER_SIZE = 65536;

    private final URL resourceURL;

    public ResourceLoader(URL resourceURL) {
        this.resourceURL = resourceURL;
    }

    public String getResourceFilePath() {
        return ResourcePath.from(resourceURL);
    }

    // Used by script resolver (scripts are small)
    public String bodyAsString() {
        return ReadFromURL.asString(resourceURL);
    }

    public Publisher<byte[]> body() {
        return body(DEFAULT_BUFFER_SIZE);
    }

    // Load body creating a sink out of the readable byte channel.
    public Publisher<byte[]> body(int readBufferSize) {

        return Flux.create(sink -> {

            try (ReadableByteChannel channel = Channels.newChannel(resourceURL.openStream())) {

                ByteBuffer byteBuffer = ByteBuffer.allocate(readBufferSize);

                while (channel.read(byteBuffer) > 0) {

                    byteBuffer.flip();

                    byte[] chunk = new byte[byteBuffer.remaining()];

                    byteBuffer.get(chunk);

                    byteBuffer.clear();

                    sink.next(chunk);
                }

                sink.complete();

            } catch (Exception exception) {
                // We MUST catch any exception. If we don't do it we risk to *never* close the sink,
                // and the inbound component might wait indefinitely!
                sink.error(exception);
            }
        });
    }
}