package de.codecentric.reedelk.admin.console;

import de.codecentric.reedelk.runtime.api.exception.PlatformException;

import java.io.FileOutputStream;
import java.io.IOException;

class ByteArrayUtils {

    static void writeTo(String fileName, byte[] data) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(data);
        } catch (IOException exception) {
            throw new PlatformException(exception);
        }
    }
}
