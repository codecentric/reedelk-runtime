package de.codecentric.reedelk.runtime.rest.api.object.mapper;

import de.codecentric.reedelk.runtime.commons.CollectionFactory;
import de.codecentric.reedelk.runtime.commons.GetterMethod;
import de.codecentric.reedelk.runtime.commons.ReflectionUtils;
import de.codecentric.reedelk.runtime.converter.DeserializerConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

public class JSONSerializer {

    public static <T> String serialize(T instance) {
        JSONObject jsonObject = _serialize(instance);
        return jsonObject.toString(4);
    }

    private static <T> JSONObject _serialize(T instance) {
        JSONObject jsonObject = new JSONObject();
        List<GetterMethod> getters = ReflectionUtils.listGetters(instance.getClass());

        getters.forEach(method -> {

            Object result = method.invoke(instance);

            // We don't add JSON properties if values are null
            if (result == null) return;

            // Primitive Type
            if (DeserializerConverter.getInstance().isPrimitive(result.getClass())) {
                jsonObject.put(method.propertyName(), result);

                // JSON Array
            } else if (CollectionFactory.isSupported(result.getClass())) {
                JSONArray array = _serialize((Collection<?>) result);
                jsonObject.put(method.propertyName(), array);

                // JSON Object
            } else {
                JSONObject nested = _serialize(result);
                jsonObject.put(method.propertyName(), nested);
            }

        });

        return jsonObject;
    }

    private static JSONArray _serialize(Collection<?> collection) {
        JSONArray array = new JSONArray();
        for (Object item : collection) {
            if (item == null) {
                array.put(JSONObject.NULL);
            } else if (DeserializerConverter.getInstance().isPrimitive(item.getClass())) {
                array.put(item);
            } else {
                array.put(_serialize(item));
            }
        }
        return array;
    }
}
