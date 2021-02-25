package de.codecentric.reedelk.module.descriptor.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.codecentric.reedelk.module.descriptor.model.property.PropertyTypeDescriptor;

import java.lang.reflect.Type;

/**
 * It serializes a TypeDescriptor object. Output example:
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
class PropertyTypeDescriptorSerializer implements JsonSerializer<PropertyTypeDescriptor> {

    @Override
    public JsonElement serialize(PropertyTypeDescriptor typeDescriptor, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject propertyTypeObject = new JsonObject();

        // Class name of the descriptor (e.g com.reedelk.module.descriptor.model.property.TypeDynamicValueDescriptor)
        String typeDescriptorClassName = typeDescriptor.getClass().getName();
        propertyTypeObject.addProperty(PropertyTypeDescriptorAttributes.CLASSNAME.value(), typeDescriptorClassName);

        // We add the type which the TypeDescriptor is referring to:
        // e.g com.reedelk.runtime.api.script.dynamicvalue.DynamicString for TypeDynamicValueDescriptor
        JsonObject instanceObject = (JsonObject) jsonSerializationContext.serialize(typeDescriptor);
        instanceObject.addProperty(PropertyTypeDescriptorAttributes.TYPE.value(), typeDescriptor.getType().getName());

        // We add to the outer object the serialized instance of the type descriptor.
        propertyTypeObject.add(PropertyTypeDescriptorAttributes.INSTANCE.value(), instanceObject);
        return propertyTypeObject;
    }
}
