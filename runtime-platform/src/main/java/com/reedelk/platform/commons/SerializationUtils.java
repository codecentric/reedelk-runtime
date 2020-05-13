package com.reedelk.platform.commons;

import com.reedelk.runtime.api.commons.Preconditions;
import com.reedelk.runtime.api.message.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtils {

    private SerializationUtils() {
    }

    // The message content and/or attributes might come from a type (or class) defined in a
    // different bundle hence, with its own classloader. Therefore we must collect the classloader,
    // of the message attributes and payload.
    public static <T extends Serializable> T clone(final Message message) {
        Collection<ClassLoader> classLoaders = new ArrayList<>();
        if (message.attributes() != null) classLoaders.add(message.attributes().getClass().getClassLoader());
        if (message.payload() != null) classLoaders.add(message.payload().getClass().getClassLoader());
        return cloneInternal(message, classLoaders);
    }

    private static <T extends Serializable> T cloneInternal(final Message object, Collection<ClassLoader> classLoaders) {
        if (object == null) {
            return null;
        }
        final byte[] objectData = serialize(object);
        final ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
        try (ClassLoaderAwareObjectInputStream in = new ClassLoaderAwareObjectInputStream(bais, classLoaders)) {
            /*
             * when we serialize and deserialize an object,
             * it is reasonable to assume the deserialized object
             * is of the same type as the original serialized object
             */
            @SuppressWarnings("unchecked") // see above
            final T readObject = (T) in.readObject();
            return readObject;

        } catch (final ClassNotFoundException ex) {
            throw new SerializationException("ClassNotFoundException while reading cloned object data", ex);
        } catch (final IOException ex) {
            throw new SerializationException("IOException while reading or closing cloned object data", ex);
        }
    }

    private static void serialize(final Serializable obj, final OutputStream outputStream) {
        Preconditions.checkNotNull(outputStream, "The OutputStream must not be null");
        try (ObjectOutputStream out = new ObjectOutputStream(outputStream)) {
            out.writeObject(obj);
        } catch (final IOException ex) {
            throw new SerializationException(ex);
        }
    }

    private static byte[] serialize(final Serializable obj) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        serialize(obj, baos);
        return baos.toByteArray();
    }

    static class ClassLoaderAwareObjectInputStream extends ObjectInputStream {
        private static final Map<String, Class<?>> primitiveTypes = new HashMap<>();

        static {
            primitiveTypes.put("byte", byte.class);
            primitiveTypes.put("short", short.class);
            primitiveTypes.put("int", int.class);
            primitiveTypes.put("long", long.class);
            primitiveTypes.put("float", float.class);
            primitiveTypes.put("double", double.class);
            primitiveTypes.put("boolean", boolean.class);
            primitiveTypes.put("char", char.class);
            primitiveTypes.put("void", void.class);
        }

        private Collection<ClassLoader> classLoaders;

        ClassLoaderAwareObjectInputStream(final InputStream in, final Collection<ClassLoader> classLoaders) throws IOException {
            super(in);
            this.classLoaders = classLoaders;
        }

        @Override
        protected Class<?> resolveClass(final ObjectStreamClass desc) throws ClassNotFoundException {
            final String name = desc.getName();
            try {
                return findClassOrThrowIfNotFound(name);
            } catch (final ClassNotFoundException ex) {
                try {
                    return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
                } catch (final ClassNotFoundException cnfe) {
                    final Class<?> cls = primitiveTypes.get(name);
                    if (cls != null) {
                        return cls;
                    }
                    throw cnfe;
                }
            }
        }

        private Class<?> findClassOrThrowIfNotFound(String name) throws ClassNotFoundException {
            for (ClassLoader classLoader : classLoaders) {
                try {
                    return Class.forName(name, false, classLoader);
                } catch (final ClassNotFoundException ex) {
                    // we continue to the next classloader...
                }
            }
            throw new ClassNotFoundException();
        }

        @Override
        public void close() throws IOException {
            super.close();
            if (classLoaders != null) {
                // Make sure we release all references to any bundle classloader.
                classLoaders.clear();
                classLoaders = null;
            }
        }
    }
}
