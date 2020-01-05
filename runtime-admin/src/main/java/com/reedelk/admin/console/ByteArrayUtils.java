package com.reedelk.admin.console;

import com.reedelk.runtime.api.exception.ESBException;

import java.io.FileOutputStream;
import java.io.IOException;

class ByteArrayUtils {

    static void writeTo(String fileName, byte[] data) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(data);
        } catch (IOException e) {
            throw new ESBException(e);
        }
    }
}
