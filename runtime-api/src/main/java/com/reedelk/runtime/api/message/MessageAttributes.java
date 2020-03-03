package com.reedelk.runtime.api.message;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.io.Serializable;
import java.util.Map;

@AutocompleteType(description = "The message attributes type contains attributes " +
        "set by processors in the out message after their execution. " +
        "Message attributes contain information collected during the execution of a given component. " +
        "For example, the REST Listener sets in the attributes request's path parameters, query parameters, HTTP headers and so on.")
public interface MessageAttributes extends Map<String, Serializable> {

    @AutocompleteItem(
            signature = "get(attributeKey: String)",
            cursorOffset = 1,
            returnType = Serializable.class,
            example = "message.attributes().get('pathParams')",
            description = "Given the attribute key, returns the attribute value associated with the given key.")
    <T> T get(String key);

    @AutocompleteItem(
            cursorOffset = 1,
            signature = "contains(attributeKey: String)",
            example = "message.attributes().contains('pathParams')",
            description = "If exists an attribute in the message attributes with the given key, returns true, false otherwise.")
    boolean contains(String key);
}
