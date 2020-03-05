package com.reedelk.runtime.commons;


import com.reedelk.runtime.api.script.dynamicmap.DynamicMap;
import com.reedelk.runtime.converter.DeserializerConverter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class ReflectionUtils {

    private static final String SETTER_PREFIX = "set";

    private static final Pattern MATCH_GENERIC_TYPE = Pattern.compile(".*<(.*)>.*");

    private ReflectionUtils() {
    }

    public static void setProperty(Object source, Method method, Object value) {
        try {
            method.invoke(source, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(format("Method named [%s] invocation error", method.getName()), e);
        } catch (IllegalArgumentException e) {
            String message = format("Method named [%s] expected argument/s of type/s [%s], but argument of type [%s] was given",
                    method.getName(), Arrays.toString(method.getParameterTypes()),
                    value != null ? value.getClass().getName() : null);
            throw new IllegalArgumentException(message, e);
        }
    }

    public static Optional<Method> getSetter(Object object, String propertyName) {
        String methodName = setterName(propertyName);
        return stream(object.getClass().getMethods())
                .filter(method -> (method.getName().equals(methodName)))
                .findFirst();
    }

    public static <T> List<GetterMethod> listGetters(Class<T> clazz) {
        return stream(clazz.getDeclaredMethods())
                .filter(GetterMethod::isGetter)
                .map(GetterMethod::new)
                .collect(toList());
    }

    @SuppressWarnings("unchecked")
    public static <T, U> SetterArgument<T, U> argumentOf(T source, String propertyName) {
        Optional<Method> maybeSetter = getSetter(source, propertyName);
        if (!maybeSetter.isPresent()) {
            throw new IllegalStateException(format("Setter for property [%s] could not be found", propertyName));
        }

        Method method = maybeSetter.get();
        if (method.getParameterTypes().length != 1) {
            throw new IllegalStateException(format("Setter for property [%s] must have one argument", propertyName));
        }

        Class<?> parameterType = method.getParameterTypes()[0];

        if (!isMap(parameterType)) {
            // A map has multiple parameter types (e.g Map<String,Object>) and we don't
            // support yet multiple parameter types.
            Optional<String> maybeGenericType = getGenericType(method);
            if (maybeGenericType.isPresent()) {
                try {
                    return new SetterArgument(parameterType, Class.forName(maybeGenericType.get()));
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException(format("Class for generic type [%s] could not be found", maybeGenericType.get()));
                }
            }
        }
        return new SetterArgument(parameterType);
    }

    private static Optional<String> getGenericType(Method method) {
        Type genericParameterType = method.getGenericParameterTypes()[0];
        String typeName = genericParameterType.getTypeName();
        Matcher matcher = MATCH_GENERIC_TYPE.matcher(typeName);

        if (matcher.matches() && matcher.groupCount() > 0) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }

    private static String setterName(String propertyName) {
        return SETTER_PREFIX + capitalize(propertyName);
    }

    private static String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private static boolean isMap(Class<?> clazz) {
        return Map.class.equals(clazz);
    }

    private static boolean isDynamicMap(Class<?> clazz) {
        return DynamicMap.class.equals(clazz.getSuperclass());
    }

    public static class SetterArgument<C, G> {

        private final Class<C> clazz;
        private final Class<G> genericType;

        SetterArgument(Class<C> clazz) {
            this.clazz = clazz;
            this.genericType = null;
        }

        SetterArgument(Class<C> clazz, Class<G> genericType) {
            this.clazz = clazz;
            this.genericType = genericType;
        }

        public Class<C> getClazz() {
            return clazz;
        }

        public Class<G> getGenericType() {
            return genericType;
        }

        public String getFullyQualifiedName() {
            return clazz.getName();
        }

        public boolean isEnum() {
            return clazz.isEnum();
        }

        public boolean isMap() {
            return ReflectionUtils.isMap(clazz);
        }

        public boolean isDynamicMap() {
            return ReflectionUtils.isDynamicMap(clazz);
        }

        public boolean isGenericTypePrimitive() {
            return DeserializerConverter.getInstance().isPrimitive(genericType);
        }
    }
}
