package com.reedelk.runtime.commons;

import com.reedelk.runtime.api.exception.PlatformException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class ObjectToBytes {

    private ObjectToBytes() {
    }

    public static byte[] from(Object object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
            try (ObjectOutput out = new ObjectOutputStream(byteArrayOutputStream)) {
                out.writeObject(object);
                out.flush();
                return byteArrayOutputStream.toByteArray();
            } catch (IOException exception) {
                throw new PlatformException(exception);
            }
        } catch (IOException exception) {
            throw new PlatformException(exception);
        }
    }
}
