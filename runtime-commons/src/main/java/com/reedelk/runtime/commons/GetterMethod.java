package com.reedelk.runtime.commons;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.String.format;

public class GetterMethod {

    private static final String GETTER_PREFIX = "get";

    private final Method method;

    public GetterMethod(Method method) {
        this.method = method;
    }

    public static boolean isGetter(Method method) {
        return method.getName().startsWith(GETTER_PREFIX);
    }

    private static String lowerCaseFirstLetter(String input) {
        return Character.toLowerCase(input.charAt(0)) +
                (input.length() > 1 ? input.substring(1) : "");
    }

    public <T> Object invoke(T object) {
        try {
            return method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(format("Invoking Method for method named [%s]", method.getName()), e);
        }
    }

    public String propertyName() {
        String name = method.getName();
        if (!name.startsWith(GETTER_PREFIX)) {
            throw new IllegalStateException(String.format("Method named [%s] is not a getter", name));
        }

        String propertyName = name.substring(GETTER_PREFIX.length());
        return lowerCaseFirstLetter(propertyName);
    }
}
