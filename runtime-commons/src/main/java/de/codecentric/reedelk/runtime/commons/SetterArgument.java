package de.codecentric.reedelk.runtime.commons;

public class SetterArgument {

    private final Class<?> argumentClazz;
    private final String collectionType;
    private final String mapKeyType;
    private final String mapValueType;

    // Object
    SetterArgument(Class<?> argumentClazz) {
        this.argumentClazz = argumentClazz;
        this.mapKeyType = null;
        this.mapValueType = null;
        this.collectionType = null;
    }

    // Collection
    SetterArgument(Class<?> argumentClazz, String collectionType) {
        this.argumentClazz = argumentClazz;
        this.collectionType = collectionType;
        this.mapKeyType = null;
        this.mapValueType = null;
    }

    // Map
    SetterArgument(Class<?> argumentClazz, String mapKeyType, String mapValueType) {
        this.argumentClazz = argumentClazz;
        this.collectionType = null;
        this.mapKeyType = mapKeyType;
        this.mapValueType = mapValueType;
    }

    public Class<?> getArgumentClazz() {
        return argumentClazz;
    }

    public String getMapKeyType() {
        return mapKeyType;
    }

    public String getMapValueType() {
        return mapValueType;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public boolean isEnum() {
        return argumentClazz.isEnum();
    }

    public boolean isMap() {
        return ReflectionUtils.isMap(argumentClazz);
    }

    public boolean isDynamicMap() {
        return ReflectionUtils.isDynamicMap(argumentClazz);
    }
}
