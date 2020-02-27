package com.reedelk.runtime.api.message;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.io.Serializable;
import java.util.Map;

@AutocompleteType
public interface MessageAttributes extends Map<String, Serializable> {

    @AutocompleteItem(replaceValue = "get('')", cursorOffset = 2, returnType = Serializable.class, description = "Given the key, returns attribute associated with the given key.")
    <T> T get(String key);

    @AutocompleteItem(replaceValue = "contains('')", cursorOffset = 2, description = "If exists an attribute in the message attributes with the given key, returns true, false otherwise.")
    boolean contains(String key);
}
