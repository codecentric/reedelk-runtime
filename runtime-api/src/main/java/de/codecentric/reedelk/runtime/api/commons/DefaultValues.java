package de.codecentric.reedelk.runtime.api.commons;

public final class DefaultValues {

    private static final Double DOUBLE_DEFAULT = 0.0D;
    private static final Float FLOAT_DEFAULT = 0.0F;

    private DefaultValues() {
    }

    public static <T> Object defaultValue(Class<T> type) {
        Preconditions.checkArgument(type != null, "type");
        if (type == boolean.class) {
            return false;
        } else if (type == int.class) {
            return 0;
        } else if (type == long.class) {
            return 0L;
        } else if (type == float.class) {
            return FLOAT_DEFAULT;
        } else if (type == double.class) {
            return DOUBLE_DEFAULT;
        } else if (type == short.class) {
            return (short) 0;
        } else if (type == byte.class) {
            return (byte) 0;
        } else if (type == char.class) {
            return '\u0000';
        } else {
            // Any other type is a complex type and must return null.
            // This includes any primitive wrapper such as Integer, Float, Double and so on.
            return null;
        }
    }
}
