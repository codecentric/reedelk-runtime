package com.reedelk.runtime.api.message;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.TypeFunction;
import com.reedelk.runtime.api.annotation.TypeProperty;
import com.reedelk.runtime.api.component.Component;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import static com.reedelk.runtime.api.message.MessageAttributeKey.COMPONENT_NAME;

// TODO: Find a way to make it read only?
// TODO: Review all annotations.
@Type(description = "The message attributes type contains attributes " +
        "set by processors in the out message after their execution. " +
        "Message attributes contain information collected during the execution of a given component. " +
        "For example, the REST Listener sets in the attributes request's path parameters, query parameters, HTTP headers and so on.")
@TypeProperty(
        type = String.class,
        name = COMPONENT_NAME,
        example = "message.attributes.component",
        description = "The name of the component setting the attributes.")
public class MessageAttributes extends TreeMap<String, Serializable> {

    public MessageAttributes() {
        super(String.CASE_INSENSITIVE_ORDER);
    }

    public void setComponent(Class<? extends Component> component) {
        put(COMPONENT_NAME, component.getName());
    }

    @TypeFunction(cursorOffset = 1,
            signature = "get(String attributeName)",
            returnType = Serializable.class,
            example = "message.attributes().get('pathParams')",
            description = "Given the attribute key, returns the attribute value associated with the given key.")
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T get(String attributeName) {
        return (T) super.get(attributeName);
    }

    @TypeFunction(cursorOffset = 1,
            signature = "contains(String attributeName)",
            example = "message.attributes().contains('pathParams')",
            description = "If exists an attribute in the message attributes with the given key, returns true, false otherwise.")
    public boolean contains(String attributeName) {
        return super.containsKey(attributeName);
    }

    @Override
    public String toString() {
        Iterator<Map.Entry<String, Serializable>> i = entrySet().iterator();
        if (! i.hasNext()) {
            return this.getClass().getSimpleName() + "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(MessageAttributes.class.getSimpleName()).append('{');
        for (;;) {
            Map.Entry<String, Serializable> e = i.next();
            String key = e.getKey();
            Serializable value = e.getValue();
            sb.append(key);
            sb.append('=');
            sb.append(value);
            if (! i.hasNext()) {
                return sb.append('}').toString();
            }
            sb.append(',').append(' ');
        }
    }
}
