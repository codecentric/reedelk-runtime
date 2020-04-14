package com.reedelk.runtime.converter;

import com.reedelk.runtime.api.resource.DynamicResource;
import com.reedelk.runtime.api.resource.ResourceBinary;
import com.reedelk.runtime.api.resource.ResourceText;
import com.reedelk.runtime.api.script.Script;
import com.reedelk.runtime.api.script.dynamicmap.DynamicBooleanMap;
import com.reedelk.runtime.api.script.dynamicmap.DynamicObjectMap;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import com.reedelk.runtime.api.script.dynamicvalue.*;
import com.reedelk.runtime.converter.json.JsonObjectConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static java.lang.String.format;

class DeserializerConverterDefault implements DeserializerConverter {

    static final DeserializerConverter INSTANCE = new DeserializerConverterDefault();

    private static final Map<Class<?>, Converter<?>> CONVERTERS;
    static {
        Map<Class<?>,Converter<?>> tmp = new HashMap<>();

        // Primitive type
        tmp.put(Boolean.class, new AsBooleanObject());
        tmp.put(boolean.class, new AsBoolean());
        tmp.put(Double.class, new AsDoubleObject());
        tmp.put(double.class, new AsDouble());
        tmp.put(Float.class, new AsFloatObject());
        tmp.put(float.class, new AsFloat());
        tmp.put(Integer.class, new AsIntegerObject());
        tmp.put(int.class, new AsInteger());
        tmp.put(Long.class, new AsLongObject());
        tmp.put(long.class, new AsLong());
        tmp.put(Character.class, new AsCharObject());
        tmp.put(char.class, new AsChar());
        tmp.put(String.class, new AsString());
        tmp.put(BigInteger.class, new AsBigInteger());
        tmp.put(BigDecimal.class, new AsBigDecimal());
        tmp.put(Map.class, new AsMap());
        tmp.put(List.class, new AsList());
        // Enum handled in the convert method

        // Dynamic value type
        tmp.put(DynamicLong.class, new AsDynamicLong());
        tmp.put(DynamicFloat.class, new AsDynamicFloat());
        tmp.put(DynamicDouble.class, new AsDynamicDouble());
        tmp.put(DynamicObject.class, new AsDynamicObject());
        tmp.put(DynamicString.class, new AsDynamicString());
        tmp.put(DynamicBoolean.class, new AsDynamicBoolean());
        tmp.put(DynamicInteger.class, new AsDynamicInteger());
        tmp.put(DynamicResource.class, new AsDynamicResource());
        tmp.put(DynamicByteArray.class, new AsDynamicByteArray());
        tmp.put(DynamicBigInteger.class, new AsDynamicBigInteger());
        tmp.put(DynamicBigDecimal.class, new AsDynamicBigDecimal());
        tmp.put(DynamicBooleanMap.class, new AsDynamicBooleanMap());
        tmp.put(DynamicStringMap.class, new AsDynamicStringMap());
        tmp.put(DynamicObjectMap.class, new AsDynamicObjectMap());

        // Script type
        tmp.put(Script.class, new AsScript());
        tmp.put(ResourceText.class, new AsResourceText());
        tmp.put(ResourceBinary.class, new AsResourceBinary());
        // Combo and Password are Strings.

        CONVERTERS = Collections.unmodifiableMap(tmp);
    }

    private DeserializerConverterDefault() {
    }

    static int size() {
        return CONVERTERS.size();
    }

    static Set<Class<?>> supportedConverters() {
        return CONVERTERS.keySet();
    }

    @Override
    public boolean isPrimitive(Class<?> clazz) {
        return CONVERTERS.containsKey(clazz);
    }

    @Override
    public boolean isPrimitive(String fullyQualifiedName) {
        return CONVERTERS.keySet()
                .stream()
                .anyMatch(typeClazz -> typeClazz.getName().equals(fullyQualifiedName));
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public <T> T convert(Class<T> expectedClass, JSONObject jsonObject, String propertyName, DeserializerConverterContext context) {
        if (expectedClass.isEnum()) {
            return JsonObjectConverter.getInstance().convert(expectedClass, jsonObject, propertyName);
        } else if (CONVERTERS.containsKey(expectedClass)) {
            return (T) CONVERTERS.get(expectedClass).convert(jsonObject, propertyName, context);
        }
        throw new IllegalStateException(format("Could not convert property with name '%s' to Class '%s'", propertyName, expectedClass.getName()));
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public <T> T convert(Class<T> expectedClass, JSONArray jsonArray, int index, DeserializerConverterContext context) {
        if (expectedClass.isEnum()) {
            return JsonObjectConverter.getInstance().convert(expectedClass, jsonArray, index);
        } else if (CONVERTERS.containsKey(expectedClass)) {
            return (T) CONVERTERS.get(expectedClass).convert(jsonArray, index, context);
        }
        throw new IllegalStateException(format("Could not convert item with index '%d' to Class '%s'", index, expectedClass.getName()));
    }

    interface Converter<T> {

        T convert(JSONObject object, String key, DeserializerConverterContext context);

        default T convert(JSONArray array, int index, DeserializerConverterContext context) {
            throw new UnsupportedOperationException();
        }
    }

    // Primitive types

    private static abstract class BaseConverter<T> implements Converter<T> {
        @Override
        public T convert(JSONObject object, String key, DeserializerConverterContext context) {
            return JsonObjectConverter.getInstance().convert(typeClazz(), object, key);
        }

        @Override
        public T convert(JSONArray array, int index, DeserializerConverterContext context) {
            return JsonObjectConverter.getInstance().convert(typeClazz(), array, index);
        }

        protected abstract Class<T> typeClazz();
    }

    private static class AsString extends BaseConverter<String> {
        @Override
        protected Class<String> typeClazz() {
            return String.class;
        }
    }

    private static class AsCharObject extends BaseConverter<Character> {
        @Override
        protected Class<Character> typeClazz() {
            return Character.class;
        }
    }


    private static class AsChar extends BaseConverter<Character> {
        @Override
        protected Class<Character> typeClazz() {
            return char.class;
        }
    }

    private static class AsLongObject extends BaseConverter<Long> {
        @Override
        protected Class<Long> typeClazz() {
            return Long.class;
        }
    }

    private static class AsLong extends BaseConverter<Long> {
        @Override
        protected Class<Long> typeClazz() {
            return long.class;
        }
    }

    private static class AsIntegerObject extends BaseConverter<Integer> {
        @Override
        protected Class<Integer> typeClazz() {
            return Integer.class;
        }
    }

    private static class AsInteger extends BaseConverter<Integer> {
        @Override
        protected Class<Integer> typeClazz() {
            return int.class;
        }
    }

    private static class AsDoubleObject extends BaseConverter<Double> {
        @Override
        protected Class<Double> typeClazz() {
            return Double.class;
        }
    }

    private static class AsDouble extends BaseConverter<Double> {
        @Override
        protected Class<Double> typeClazz() {
            return double.class;
        }
    }

    private static class AsFloatObject extends BaseConverter<Float> {
        @Override
        protected Class<Float> typeClazz() {
            return Float.class;
        }
    }

    private static class AsFloat extends BaseConverter<Float> {
        @Override
        protected Class<Float> typeClazz() {
            return float.class;
        }
    }

    private static class AsBooleanObject extends BaseConverter<Boolean> {
        @Override
        protected Class<Boolean> typeClazz() {
            return Boolean.class;
        }
    }

    private static class AsBoolean extends BaseConverter<Boolean> {
        @Override
        protected Class<Boolean> typeClazz() {
            return boolean.class;
        }
    }

    private static class AsBigDecimal extends BaseConverter<BigDecimal> {
        @Override
        protected Class<BigDecimal> typeClazz() {
            return BigDecimal.class;
        }
    }

    private static class AsBigInteger extends BaseConverter<BigInteger> {
        @Override
        protected Class<BigInteger> typeClazz() {
            return BigInteger.class;
        }
    }

    @SuppressWarnings("rawtypes")
    private static class AsMap extends BaseConverter<Map> {
        @Override
        protected Class<Map> typeClazz() {
            return Map.class;
        }
    }

    @SuppressWarnings("rawtypes")
    private static class AsList extends BaseConverter<List> {
        @Override
        protected Class<List> typeClazz() {
            return List.class;
        }
    }

    // Dynamic types

    // A non-dynamic byte array is always a string.
    private static class AsDynamicByteArray implements Converter<DynamicByteArray> {
        @Override
        public DynamicByteArray convert(JSONObject object, String key, DeserializerConverterContext context) {
            Object value = getAsStringIfDynamicOrWithType(object, key, String.class);
            return DynamicByteArray.from(value, context.moduleContext());
        }

        @Override
        public DynamicByteArray convert(JSONArray array, int index, DeserializerConverterContext context) {
            Object value = getAsStringIfDynamicOrWithType(array, index, String.class);
            return DynamicByteArray.from(value, context.moduleContext());
        }
    }

    private static class AsDynamicLong implements Converter<DynamicLong> {
        @Override
        public DynamicLong convert(JSONObject object, String key, DeserializerConverterContext context) {
            Object value = getAsStringIfDynamicOrWithType(object, key, Long.class);
            return DynamicLong.from(value, context.moduleContext());
        }

        @Override
        public DynamicLong convert(JSONArray array, int index, DeserializerConverterContext context) {
            Object value = getAsStringIfDynamicOrWithType(array, index, Long.class);
            return DynamicLong.from(value, context.moduleContext());
        }
    }

    private static class AsDynamicDouble implements Converter<DynamicDouble> {
        @Override
        public DynamicDouble convert(JSONObject object, String key, DeserializerConverterContext context) {
            Object value = getAsStringIfDynamicOrWithType(object, key, Double.class);
            return DynamicDouble.from(value, context.moduleContext());
        }

        @Override
        public DynamicDouble convert(JSONArray array, int index, DeserializerConverterContext context) {
            Object value = getAsStringIfDynamicOrWithType(array, index, Double.class);
            return DynamicDouble.from(value, context.moduleContext());
        }
    }

    private static class AsDynamicFloat implements Converter<DynamicFloat> {
        @Override
        public DynamicFloat convert(JSONObject object, String key, DeserializerConverterContext context) {
            Object value = getAsStringIfDynamicOrWithType(object, key, Float.class);
            return DynamicFloat.from(value, context.moduleContext());
        }

        @Override
        public DynamicFloat convert(JSONArray array, int index, DeserializerConverterContext context) {
            Object value = getAsStringIfDynamicOrWithType(array, index, Float.class);
            return DynamicFloat.from(value, context.moduleContext());
        }
    }

    private static class AsDynamicBigDecimal implements Converter<DynamicBigDecimal> {
        @Override
        public DynamicBigDecimal convert(JSONObject object, String key, DeserializerConverterContext context) {
            Object value = getAsStringIfDynamicOrWithType(object, key, BigDecimal.class);
            return DynamicBigDecimal.from(value, context.moduleContext());
        }

        @Override
        public DynamicBigDecimal convert(JSONArray array, int index, DeserializerConverterContext context) {
            Object value = getAsStringIfDynamicOrWithType(array, index, BigDecimal.class);
            return DynamicBigDecimal.from(value, context.moduleContext());
        }
    }

    private static class AsDynamicBigInteger implements Converter<DynamicBigInteger> {
        @Override
        public DynamicBigInteger convert(JSONObject object, String key, DeserializerConverterContext context) {
            Object value = getAsStringIfDynamicOrWithType(object, key, BigInteger.class);
            return DynamicBigInteger.from(value, context.moduleContext());
        }

        @Override
        public DynamicBigInteger convert(JSONArray array, int index, DeserializerConverterContext context) {
            Object value = getAsStringIfDynamicOrWithType(array, index, BigInteger.class);
            return DynamicBigInteger.from(value, context.moduleContext());
        }
    }

    private static class AsDynamicBoolean implements Converter<DynamicBoolean> {
        @Override
        public DynamicBoolean convert(JSONObject object, String key, DeserializerConverterContext context) {
            Object value = getAsStringIfDynamicOrWithType(object, key, Boolean.class);
            return DynamicBoolean.from(value, context.moduleContext());
        }

        @Override
        public DynamicBoolean convert(JSONArray array, int index, DeserializerConverterContext context) {
            Object value = getAsStringIfDynamicOrWithType(array, index, Boolean.class);
            return DynamicBoolean.from(value, context.moduleContext());
        }
    }

    private static class AsDynamicInteger implements Converter<DynamicInteger> {
        @Override
        public DynamicInteger convert(JSONObject object, String key, DeserializerConverterContext context) {
            Object value = getAsStringIfDynamicOrWithType(object, key, Integer.class);
            return DynamicInteger.from(value, context.moduleContext());
        }

        @Override
        public DynamicInteger convert(JSONArray array, int index, DeserializerConverterContext context) {
            Object value = getAsStringIfDynamicOrWithType(array, index, Integer.class);
            return DynamicInteger.from(value, context.moduleContext());
        }
    }

    private static class AsDynamicString implements Converter<DynamicString> {
        @Override
        public DynamicString convert(JSONObject object, String key, DeserializerConverterContext context) {
            String value = JsonObjectConverter.getInstance().convert(String.class, object, key);
            return DynamicString.from(value, context.moduleContext());
        }

        @Override
        public DynamicString convert(JSONArray array, int index, DeserializerConverterContext context) {
            String value = JsonObjectConverter.getInstance().convert(String.class, array, index);
            return DynamicString.from(value, context.moduleContext());
        }
    }

    private static class AsDynamicObject implements Converter<DynamicObject> {
        @Override
        public DynamicObject convert(JSONObject object, String key, DeserializerConverterContext context) {
            Object value = JsonObjectConverter.getInstance().convert(Object.class, object, key);
            return DynamicObject.from(value, context.moduleContext());
        }

        @Override
        public DynamicObject convert(JSONArray array, int index, DeserializerConverterContext context) {
            Object value = JsonObjectConverter.getInstance().convert(Object.class, array, index);
            return DynamicObject.from(value, context.moduleContext());
        }
    }

    private static class AsScript implements Converter<Script> {
        @Override
        public Script convert(JSONObject object, String key, DeserializerConverterContext context) {
            String value = JsonObjectConverter.getInstance().convert(String.class, object, key);
            return Script.from(value, context.moduleContext());
        }
    }

    private static class AsResourceText implements Converter<ResourceText> {
        @Override
        public ResourceText convert(JSONObject object, String key, DeserializerConverterContext context) {
            String value = JsonObjectConverter.getInstance().convert(String.class, object, key);
            return ResourceText.from(value);
        }
    }

    private static class AsResourceBinary implements Converter<ResourceBinary> {
        @Override
        public ResourceBinary convert(JSONObject object, String key, DeserializerConverterContext context) {
            String value = JsonObjectConverter.getInstance().convert(String.class, object, key);
            return ResourceBinary.from(value);
        }
    }

    private static class AsDynamicResource implements Converter<DynamicResource> {
        @Override
        public DynamicResource convert(JSONObject object, String key, DeserializerConverterContext context) {
            String value = JsonObjectConverter.getInstance().convert(String.class, object, key);
            return DynamicResource.from(value, context.moduleContext());
        }
    }

    // Dynamic map types

    @SuppressWarnings("unchecked")
    private static class AsDynamicBooleanMap implements Converter<DynamicBooleanMap> {
        @Override
        public DynamicBooleanMap convert(JSONObject object, String key, DeserializerConverterContext context) {
            Map<String,Object> mapValue = JsonObjectConverter.getInstance().convert(Map.class, object, key);
            return DynamicBooleanMap.from(mapValue, context.moduleContext());
        }

        @Override
        public DynamicBooleanMap convert(JSONArray array, int index, DeserializerConverterContext context) {
            Map<String,Object> mapValue = JsonObjectConverter.getInstance().convert(Map.class, array, index);
            return DynamicBooleanMap.from(mapValue, context.moduleContext());
        }
    }

    @SuppressWarnings("unchecked")
    private static class AsDynamicStringMap implements Converter<DynamicStringMap> {
        @Override
        public DynamicStringMap convert(JSONObject object, String key, DeserializerConverterContext context) {
            Map<String,Object> mapValue = JsonObjectConverter.getInstance().convert(Map.class, object, key);
            return DynamicStringMap.from(mapValue, context.moduleContext());
        }

        @Override
        public DynamicStringMap convert(JSONArray array, int index, DeserializerConverterContext context) {
            Map<String,Object> mapValue = JsonObjectConverter.getInstance().convert(Map.class, array, index);
            return DynamicStringMap.from(mapValue, context.moduleContext());
        }
    }

    @SuppressWarnings("unchecked")
    private static class AsDynamicObjectMap implements Converter<DynamicObjectMap> {
        @Override
        public DynamicObjectMap convert(JSONObject object, String key, DeserializerConverterContext context) {
            Map<String,Object> mapValue = JsonObjectConverter.getInstance().convert(Map.class, object, key);
            return DynamicObjectMap.from(mapValue, context.moduleContext());
        }

        @Override
        public DynamicObjectMap convert(JSONArray array, int index, DeserializerConverterContext context) {
            Map<String,Object> mapValue = JsonObjectConverter.getInstance().convert(Map.class, array, index);
            return DynamicObjectMap.from(mapValue, context.moduleContext());
        }
    }

    private static <T> Object getAsStringIfDynamicOrWithType(JSONObject object, String key, Class<T> target) {
        Object value = JsonObjectConverter.getInstance().convert(Object.class, object, key);
        return value instanceof String ?
                // Dynamic value (script expression)
                JsonObjectConverter.getInstance().convert(String.class, object, key) :
                JsonObjectConverter.getInstance().convert(target, object, key);
    }

    private static <T> Object getAsStringIfDynamicOrWithType(JSONArray array, int index, Class<T> target) {
        Object value = JsonObjectConverter.getInstance().convert(Object.class, array, index);
        return value instanceof String ?
                // Dynamic value (script expression)
                JsonObjectConverter.getInstance().convert(String.class, array, index) :
                JsonObjectConverter.getInstance().convert(target, array, index);
    }
}
