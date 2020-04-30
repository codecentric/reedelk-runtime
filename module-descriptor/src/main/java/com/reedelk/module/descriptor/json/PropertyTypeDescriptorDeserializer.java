package com.reedelk.module.descriptor.json;

import com.google.gson.*;
import com.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;
import com.reedelk.module.descriptor.model.property.TypeDescriptors;
import com.reedelk.runtime.api.commons.PlatformTypes;

import java.lang.reflect.Type;

/**
 * It de-serializes the following structure in a TypeDescriptor object.
 *  "propertyType": {
 *    "classname": "com.reedelk.module.descriptor.model.property.TypeEnumDescriptor",
 *    "instance": {
 *      "nameAndDisplayNameMap": {
 *        "STREAM": "Stream",
 *        "DEFAULT": "Default"
 *      },
 *      "type": "java.lang.Enum"
 *    }
 *  }
 */
class PropertyTypeDescriptorDeserializer implements JsonDeserializer<PropertyTypeDescriptor> {

    @Override
    public PropertyTypeDescriptor deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {

        if (!jsonElement.isJsonObject()) {
            throw new JsonParseException("Expected json object");
        }

        JsonObject myObject = jsonElement.getAsJsonObject();
        JsonElement typeDescriptorClassName = myObject.get(PropertyTypeDescriptorAttributes.CLASSNAME.value());

        // Instantiate the type descriptor class.
        Class<? extends PropertyTypeDescriptor> typeDescriptorClazz = TypeDescriptors.from(typeDescriptorClassName.getAsString());

        // Deserialize the content of  the type descriptor
        JsonObject instanceObject = myObject.getAsJsonObject(PropertyTypeDescriptorAttributes.INSTANCE.value());
        PropertyTypeDescriptor deSerialized = context.deserialize(instanceObject, typeDescriptorClazz);

        // Fill in the TypeDescriptor type class
        JsonElement typeDescriptorClass = instanceObject.get(PropertyTypeDescriptorAttributes.TYPE.value());
        String clazzFullyQualifiedName = typeDescriptorClass.getAsString();

        if (Object.class.getName().equals(clazzFullyQualifiedName)) {
            // This is a special case for
            // com.reedelk.module.descriptor.model.property.TypeDynamicValueDescriptor
            // with class Object (DynamicObject).
            deSerialized.setType(Object.class);

        } else if (byte[].class.getName().equals(clazzFullyQualifiedName)) {
            // This is a special case for
            // com.reedelk.module.descriptor.model.property.TypeDynamicValueDescriptor
            // with class byte[] (DynamicByteArray).
            deSerialized.setType(byte[].class);

        } else {
            // The type of the descriptor is a platform type and it must be known.
            Class<?> typeClazz = PlatformTypes.from(clazzFullyQualifiedName);
            deSerialized.setType(typeClazz);
        }

        return deSerialized;
    }
}
