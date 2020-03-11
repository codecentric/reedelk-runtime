package com.reedelk.esb.flow.deserializer;

import com.reedelk.esb.graph.ExecutionNode;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.commons.CollectionFactory;
import com.reedelk.runtime.commons.JsonParser;
import com.reedelk.runtime.commons.ReflectionUtils;
import com.reedelk.runtime.commons.SetterArgument;
import com.reedelk.runtime.converter.DeserializerConverter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import static com.reedelk.esb.commons.Messages.Deserializer.CONFIGURATION_NOT_FOUND;
import static com.reedelk.esb.commons.Messages.Deserializer.UNSUPPORTED_COLLECTION_TYPE;
import static com.reedelk.runtime.api.commons.Preconditions.checkArgument;
import static com.reedelk.runtime.api.commons.Preconditions.checkState;
import static com.reedelk.runtime.commons.JsonParser.Component;
import static com.reedelk.runtime.commons.JsonParser.Config;
import static com.reedelk.runtime.commons.ReflectionUtils.*;

public class GenericComponentDefinitionDeserializer {

    private final ExecutionNode executionNode;
    private final FlowDeserializerContext context;

    public GenericComponentDefinitionDeserializer(final ExecutionNode executionNode, final FlowDeserializerContext context) {
        this.executionNode = executionNode;
        this.context = context;
    }

    public void deserialize(JSONObject componentDefinition, Implementor implementor) {
        Iterator<String> iterator = componentDefinition.keys();
        while (iterator.hasNext()) {
            String propertyName = iterator.next();
            getSetter(implementor, propertyName).ifPresent(setter -> {
                Optional<String> maybeReference = isReference(componentDefinition, propertyName);
                Object deSerializedObject = maybeReference.isPresent() ?
                        deserialize(maybeReference.get()) :
                        deserialize(componentDefinition, implementor, propertyName);
                setProperty(implementor, setter, deSerializedObject);
            });
        }
    }

    private Object deserialize(JSONObject componentDefinition, Implementor bean, String propertyName) {
        Object propertyValue = componentDefinition.get(propertyName);
        SetterArgument setterArgument = argumentOf(bean, propertyName);

        // Dynamic Map or declared Implementor object
        if (propertyValue instanceof JSONObject) {
            return deserializeObject(componentDefinition, propertyName, setterArgument);

            // Collection
        } else if (propertyValue instanceof JSONArray) {
            checkArgument(CollectionFactory.isSupported(setterArgument.getArgumentClazz()),
                    UNSUPPORTED_COLLECTION_TYPE.format(propertyName));
            return deserializeArray(componentDefinition, propertyName, setterArgument);

            // Enum
        } else if (setterArgument.isEnum()){
            Class<?> enumClazz = setterArgument.getArgumentClazz();
            return context.converter().convert(enumClazz, componentDefinition, propertyName);

            // Primitive or Dynamic Value
        } else {
            Class<?> clazz = setterArgument.getArgumentClazz();
            return context.converter().convert(clazz, componentDefinition, propertyName);
        }
    }

    private Object deserializeObject(JSONObject componentDefinition, String propertyName, SetterArgument setterArgument) {
        if (setterArgument.isMap()) {
            // A map can only have as key type 'String'. This is because a
            // JSON object can have as property keys only strings.
            checkState(String.class.getName().equals(setterArgument.getMapKeyType()),
                    "Map type supports only String type for keys.");

            // The map value type could be either:
            // 1. primitive: normal case, we use the converter to create a Java map.
            // 2. non primitive: a custom module provided Java type (which implements Implementor interface).
            //  in this case we must instantiate the implementor.
            boolean isPrimitive = DeserializerConverter.getInstance().isPrimitive(setterArgument.getMapValueType());
            if (isPrimitive) {
                // primitive
                // The setter argument for this property is a Map<String,Object> where object is a primitive type.
                // We use the converter to convert the map values from the JSON object component definition.
                Class<?> clazz = setterArgument.getArgumentClazz();
                return context.converter().convert(clazz, componentDefinition, propertyName);

            } else {
                // non primitive
                // Custom module-specific type i.e a Java class implementing the Implementor interface.
                String valueType = setterArgument.getMapValueType();
                JSONObject customValueTypeMap = componentDefinition.getJSONObject(propertyName);
                Map<String,Object> keyAndValues = new HashMap<>();
                customValueTypeMap.keySet().forEach(key -> {
                    Implementor implementor = instantiateImplementor(valueType);
                    deserialize(customValueTypeMap.getJSONObject(key), implementor);
                    keyAndValues.put(key, implementor);
                });
                return keyAndValues;
            }


        } else if (setterArgument.isDynamicMap()){
            // The setter argument for this property is any type of Dynamic map,
            // we must wrap the de-serialized java map object with a type specific
            // dynamic map which adds a UUID identifying the dynamic map function to
            // be used by the Script engine as a reference for the pre-compiled script
            // to be used at runtime evaluation.
            Class<?> clazz = setterArgument.getArgumentClazz();
            return context.converter().convert(clazz, componentDefinition, propertyName);

        } else {
            // It is a complex type implementing implementor interface.
            // We expect that this JSONObject satisfies the properties
            // of the property setter argument's object type.
            String fullyQualifiedName = setterArgument.getArgumentClazz().getName();
            Implementor deSerialized = instantiateImplementor(fullyQualifiedName);
            JSONObject jsonObject = componentDefinition.getJSONObject(propertyName);
            deserialize(jsonObject, deSerialized);
            return deSerialized;
        }
    }

    private Collection<?> deserializeArray(JSONObject componentDefinition, String propertyName, SetterArgument argument) {
        JSONArray array = componentDefinition.getJSONArray(propertyName);
        Class<?> collectionClazz = argument.getArgumentClazz();
        Class<?> collectionType = ReflectionUtils.asClass(argument.getCollectionType());
        Collection<Object> collection = CollectionFactory.from(collectionClazz);
        for (int index = 0; index < array.length(); index++) {
            Object converted = context.converter().convert(collectionType, array, index);
            collection.add(converted);
        }
        return collection;
    }

    private Object deserialize(String referenceId) {
        JSONObject jsonConfig = findReferenceDefinition(referenceId);
        Implementor bean = instantiateImplementor(jsonConfig);
        deserialize(jsonConfig, bean);
        return bean;
    }

    private Optional<String> isReference(JSONObject componentDefinition, String propertyName) {
        Object propertyValue = componentDefinition.get(propertyName);
        if (propertyValue instanceof JSONObject) {
            JSONObject possibleReference = (JSONObject) propertyValue;
            if (possibleReference.has(Component.ref())) {
                return Optional.ofNullable(Component.ref(possibleReference));
            }
        }
        return Optional.empty();
    }

    private Implementor instantiateImplementor(JSONObject jsonObject) {
        String implementorFullyQualifiedName = JsonParser.Implementor.name(jsonObject);
        return instantiateImplementor(implementorFullyQualifiedName);
    }

    private Implementor instantiateImplementor(String implementorFullyQualifiedName) {
        return context.instantiateImplementor(executionNode, implementorFullyQualifiedName);
    }

    private JSONObject findReferenceDefinition(String reference) {
        return context.deserializedModule()
                .getConfigurations()
                .stream()
                .filter(referenceJsonObject -> reference.equals(Config.id(referenceJsonObject)))
                .findFirst()
                .orElseThrow(() -> new ESBException(CONFIGURATION_NOT_FOUND.format(reference)));
    }
}
