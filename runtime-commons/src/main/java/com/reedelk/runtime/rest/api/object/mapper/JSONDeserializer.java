package com.reedelk.runtime.rest.api.object.mapper;

import com.reedelk.runtime.commons.CollectionFactory;
import com.reedelk.runtime.commons.ReflectionUtils;
import com.reedelk.runtime.commons.SetterArgument;
import com.reedelk.runtime.converter.DeserializerConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static com.reedelk.runtime.api.commons.Preconditions.checkArgument;
import static com.reedelk.runtime.commons.CollectionFactory.isSupported;
import static com.reedelk.runtime.commons.ReflectionUtils.*;
import static java.lang.String.format;

public class JSONDeserializer {

    public static <T> T deserialize(String json, Class<T> dto) {
        JSONObject object = new JSONObject(json);
        return deserialize(object, dto);
    }

    private static <T> T deserialize(JSONObject object, Class<T> dto) {
        Set<String> propertyNames = object.keySet();
        T instance = instantiate(dto);

        for (String propertyName : propertyNames) {
            Object propertyValue = object.get(propertyName);

            Optional<Method> setter = getSetter(instance, propertyName);
            setter.ifPresent(method -> {
                Object deserialized;

                // Object
                if (propertyValue instanceof JSONObject) {
                    SetterArgument setterArgument = argumentOf(instance, propertyName);
                    deserialized = deserialize((JSONObject) propertyValue, setterArgument.getArgumentClazz());

                    // Collection
                } else if (propertyValue instanceof JSONArray) {
                    SetterArgument setterArgument = argumentOf(instance, propertyName);
                    Class<?> clazz = setterArgument.getArgumentClazz();
                    checkArgument(isSupported(clazz), format("Could not map property %s: not a supported collection type", propertyName));
                    deserialized = deserialize((JSONArray) propertyValue, setterArgument);

                    // Primitive
                } else {
                    SetterArgument setterArgument = argumentOf(instance, propertyName);
                    Class<?> clazz = setterArgument.getArgumentClazz();
                    deserialized = DeserializerConverter.getInstance().convert(clazz, object, propertyName);
                }

                setProperty(instance, method, deserialized);
            });

        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    private static Collection<?> deserialize(JSONArray array, SetterArgument setterArgument) {
        Class<Collection<?>> clazz = (Class<Collection<?>>) setterArgument.getArgumentClazz();
        Class<?> genericType = ReflectionUtils.asClass(setterArgument.getCollectionType());
        Collection<Object> collection = CollectionFactory.from(clazz);
        boolean isPrimitive = DeserializerConverter.getInstance().isPrimitive(setterArgument.getCollectionType());
        if (isPrimitive) {
            for (int index = 0; index < array.length(); index++) {
                Object converted = DeserializerConverter.getInstance().convert(genericType, array, index);
                collection.add(converted);
            }
        } else {
            for (int index = 0; index < array.length(); index++) {
                Object converted = deserialize((JSONObject) array.get(index), genericType);
                collection.add(converted);
            }
        }
        return collection;
    }

    private static <T> T instantiate(Class<T> dto) {
        try {
            return dto.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Could not parse");
        }
    }
}
