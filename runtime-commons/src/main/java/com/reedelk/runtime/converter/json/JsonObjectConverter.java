package com.reedelk.runtime.converter.json;

import com.reedelk.runtime.api.commons.DefaultValues;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public class JsonObjectConverter {

    private static final JsonObjectConverter INSTANCE = new JsonObjectConverter();

    private static class JsonObjectConverterHelper {
        
        private static final Map<Class<?>, TypeConverter<?>> CONVERTERS;
        static {
            Map<Class<?>, TypeConverter<?>> tmp = new HashMap<>();
            tmp.put(String.class, new AsString());
            tmp.put(Long.class, new AsLongObject());
            tmp.put(long.class, new AsLong());
            tmp.put(Integer.class, new AsIntegerObject());
            tmp.put(int.class, new AsInteger());
            tmp.put(Double.class, new AsDoubleObject());
            tmp.put(double.class, new AsDouble());
            tmp.put(Float.class, new AsFloatObject());
            tmp.put(float.class, new AsFloat());
            tmp.put(Boolean.class, new AsBooleanObject());
            tmp.put(boolean.class, new AsBoolean());
            tmp.put(BigDecimal.class, new AsBigDecimal());
            tmp.put(BigInteger.class, new AsBigInteger());
            tmp.put(Object.class, new AsObject());
            tmp.put(Map.class, new AsMap());
            CONVERTERS = Collections.unmodifiableMap(tmp);
        }
    }

    private JsonObjectConverter() {
    }

    public static JsonObjectConverter getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> T convert(Class<T> expectedClass, JSONObject jsonObject, String propertyName) {
        if (expectedClass.isEnum()) {
            return (T) jsonObject.getEnum((Class<Enum>) expectedClass, propertyName);
        } else if (JsonObjectConverterHelper.CONVERTERS.containsKey(expectedClass)) {
            return (T) JsonObjectConverterHelper.CONVERTERS.get(expectedClass).convert(jsonObject, propertyName);
        }
        throw new IllegalStateException(format("Could not convert property with name '%s' to Class '%s'", propertyName, expectedClass.getName()));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> T convert(Class<T> expectedClass, JSONArray jsonArray, int index) {
        if (expectedClass.isEnum()) {
            return (T) jsonArray.getEnum((Class<Enum>) expectedClass, index);
        } else if (JsonObjectConverterHelper.CONVERTERS.containsKey(expectedClass)) {
            return (T) JsonObjectConverterHelper.CONVERTERS.get(expectedClass).convert(jsonArray, index);
        }
        throw new IllegalStateException(format("Could not convert property with name '%d' to Class '%s'", index, expectedClass.getName()));
    }
    
    interface TypeConverter<T> {
        T convert(JSONObject object, String key);
        T convert(JSONArray array, int index);
    }

    private static class AsString implements TypeConverter<String> {
        @Override
        public String convert(JSONObject object, String key) {
            return object.isNull(key) ? null : object.getString(key);
        }

        @Override
        public String convert(JSONArray array, int index) {
            return array.isNull(index) ? null : array.getString(index);
        }
    }

    private static class AsLongObject implements TypeConverter<Long> {
        @Override
        public Long convert(JSONObject object, String key) {
            return object.isNull(key) ? null : object.getLong(key);
        }

        @Override
        public Long convert(JSONArray array, int index) {
            return array.isNull(index) ? null : array.getLong(index);
        }
    }

    private static class AsLong implements TypeConverter<Long> {

        @SuppressWarnings("ConstantConditions")
        @Override
        public Long convert(JSONObject object, String key) {
            return object.isNull(key) ?
                    (Long) DefaultValues.defaultValue(long.class) :
                    object.getLong(key);
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public Long convert(JSONArray array, int index) {
            return array.isNull(index) ?
                    (Long) DefaultValues.defaultValue(long.class) : array.getLong(index);
        }
    }

    private static class AsIntegerObject implements TypeConverter<Integer> {
        @Override
        public Integer convert(JSONObject object, String key) {
            return object.isNull(key) ? null : object.getInt(key);
        }

        @Override
        public Integer convert(JSONArray array, int index) {
            return array.isNull(index) ? null : array.getInt(index);
        }
    }

    private static class AsInteger implements TypeConverter<Integer> {

        @SuppressWarnings("ConstantConditions")
        @Override
        public Integer convert(JSONObject object, String key) {
            return object.isNull(key) ?
                    (Integer) DefaultValues.defaultValue(int.class) : object.getInt(key);
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public Integer convert(JSONArray array, int index) {
            return array.isNull(index) ?
                    (Integer) DefaultValues.defaultValue(int.class) : array.getInt(index);
        }
    }

    private static class AsDoubleObject implements TypeConverter<Double> {
        @Override
        public Double convert(JSONObject object, String key) {
            return object.isNull(key) ? null : object.getDouble(key);
        }

        @Override
        public Double convert(JSONArray array, int index) {
            return array.isNull(index) ? null : array.getDouble(index);
        }
    }

    private static class AsDouble implements TypeConverter<Double> {

        @SuppressWarnings("ConstantConditions")
        @Override
        public Double convert(JSONObject object, String key) {
            return object.isNull(key) ?
                    (Double) DefaultValues.defaultValue(double.class) : object.getDouble(key);
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public Double convert(JSONArray array, int index) {
            return array.isNull(index) ?
                    (Double) DefaultValues.defaultValue(double.class) : array.getDouble(index);
        }
    }

    private static class AsFloatObject implements TypeConverter<Float> {
        @Override
        public Float convert(JSONObject object, String key) {
            return object.isNull(key) ? null : object.getFloat(key);
        }

        @Override
        public Float convert(JSONArray array, int index) {
            return array.isNull(index) ? null : array.getFloat(index);
        }
    }

    private static class AsFloat implements TypeConverter<Float> {

        @SuppressWarnings("ConstantConditions")
        @Override
        public Float convert(JSONObject object, String key) {
            return object.isNull(key) ?
                    (Float) DefaultValues.defaultValue(float.class) : object.getFloat(key);
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public Float convert(JSONArray array, int index) {
            return array.isNull(index) ?
                    (Float) DefaultValues.defaultValue(float.class) : array.getFloat(index);
        }
    }

    private static class AsBooleanObject implements TypeConverter<Boolean> {
        @Override
        public Boolean convert(JSONObject object, String key) {
            return object.isNull(key) ? null : object.getBoolean(key);
        }

        @Override
        public Boolean convert(JSONArray array, int index) {
            return array.isNull(index) ? null : array.getBoolean(index);
        }
    }

    private static class AsBoolean implements TypeConverter<Boolean> {

        @SuppressWarnings("ConstantConditions")
        @Override
        public Boolean convert(JSONObject object, String key) {
            return object.isNull(key) ?
                    (Boolean) DefaultValues.defaultValue(boolean.class) : object.getBoolean(key);
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public Boolean convert(JSONArray array, int index) {
            return array.isNull(index) ?
                    (Boolean) DefaultValues.defaultValue(boolean.class) : array.getBoolean(index);
        }
    }

    private static class AsBigDecimal implements TypeConverter<BigDecimal> {
        @Override
        public BigDecimal convert(JSONObject object, String key) {
            return object.isNull(key) ? null : object.getBigDecimal(key);
        }

        @Override
        public BigDecimal convert(JSONArray array, int index) {
            return array.isNull(index) ? null : array.getBigDecimal(index);
        }
    }

    private static class AsBigInteger implements TypeConverter<BigInteger> {
        @Override
        public BigInteger convert(JSONObject object, String key) {
            return object.isNull(key) ? null : object.getBigInteger(key);
        }

        @Override
        public BigInteger convert(JSONArray array, int index) {
            return array.isNull(index) ? null : array.getBigInteger(index);
        }
    }

    private static class AsObject implements TypeConverter<Object> {
        @Override
        public Object convert(JSONObject object, String key) {
            return object.isNull(key) ? null : object.get(key);
        }

        @Override
        public Object convert(JSONArray array, int index) {
            return array.isNull(index) ? null : array.get(index);
        }
    }

    private static class AsMap implements TypeConverter<Map<String,Object>> {

        @Override
        public Map<String, Object> convert(JSONObject object, String key) {
            if (object.isNull(key)) {
                return null;
            } else {
                JSONObject mapJsonObject = object.getJSONObject(key);
                return mapJsonObject.toMap();
            }
        }

        @Override
        public Map<String, Object> convert(JSONArray array, int index) {
            if (array.isNull(index)) {
                return null;
            } else {
                JSONObject mapJsonObject = array.getJSONObject(index);
                return mapJsonObject.toMap();
            }
        }
    }
}
