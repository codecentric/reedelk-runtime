package de.codecentric.reedelk.runtime.commons;

import de.codecentric.reedelk.runtime.api.commons.Preconditions;

import java.util.*;

import static de.codecentric.reedelk.runtime.api.commons.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.asList;

public class CollectionFactory {

    private static final Collection<Class<?>> SUPPORTED_COLLECTIONS = asList(Set.class, List.class, Collection.class);

    private CollectionFactory() {
    }

    public static Collection<Object> from(Class<?> collectionClass) {
        Preconditions.checkArgument(SUPPORTED_COLLECTIONS.contains(collectionClass), format("Collection class %s not supported", collectionClass.getName()));

        if (collectionClass == Set.class) return new HashSet<>();
        if (collectionClass == List.class) return new ArrayList<>();
        if (collectionClass == Collection.class) return new ArrayList<>();

        // Should never get to this point (unless SUPPORTED_COLLECTION does not reflect this method implementation)
        throw new IllegalArgumentException(format("Collection class %s not supported", collectionClass.getName()));
    }

    public static <C> boolean isSupported(Class<C> clazz) {
        for (Class<?> supportedClazz : SUPPORTED_COLLECTIONS) {
            if (supportedClazz.isAssignableFrom(clazz)) return true;
        }
        return false;
    }
}
