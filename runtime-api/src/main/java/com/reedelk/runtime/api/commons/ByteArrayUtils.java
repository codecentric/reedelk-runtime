package com.reedelk.runtime.api.commons;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayUtils {

    private static final int DEFAULT_BUFFER_SIZE = 16384;

    private ByteArrayUtils() {
    }

    public static byte[] from(final InputStream input) throws IOException {
        return from(input, DEFAULT_BUFFER_SIZE);
    }

    public static byte[] from(final InputStream input, int bufferSize) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[bufferSize];
        while ((nRead = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
}
