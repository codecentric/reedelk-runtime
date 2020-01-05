package com.reedelk.esb.commons;

import com.reedelk.runtime.commons.ObjectToBytes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class SerializationUtils {

    private SerializationUtils() {
    }

    public static <T extends Serializable> T clone(final T object) {
        if (object == null) return null;

        final byte[] objectData = ObjectToBytes.from(object);
        final ByteArrayInputStream bais = new ByteArrayInputStream(objectData);

        try (ObjectInputStream in = new ObjectInputStream(bais)) {
            /* the result of serialization and deserialization produces an object of the same input type */
            @SuppressWarnings("unchecked")
            final T readObject = (T) in.readObject();
            return readObject;
        } catch (final ClassNotFoundException exception) {
            throw new SerializationException("ClassNotFoundException while reading cloned object data", exception);
        } catch (final IOException exception) {
            throw new SerializationException("IOException while reading or closing cloned object data", exception);
        }
    }
}