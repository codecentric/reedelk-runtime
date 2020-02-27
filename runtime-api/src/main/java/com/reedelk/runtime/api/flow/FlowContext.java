package com.reedelk.runtime.api.flow;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.io.Serializable;
import java.util.Map;

import static com.reedelk.runtime.api.autocomplete.AutocompleteItemType.FUNCTION;
import static com.reedelk.runtime.api.autocomplete.AutocompleteItemType.VARIABLE;

@AutocompleteType
@AutocompleteItem(itemType = VARIABLE, returnType = String.class, token = "correlationId", replaceValue = "correlationId", description = "Returns the current flow correlation id.")
@AutocompleteItem(itemType = FUNCTION, returnType = Void.class, token = "put", replaceValue = "put('',)", cursorOffset = 3, description = "Puts a property with the given value into the flow context.")
@AutocompleteItem(itemType = FUNCTION, returnType = Serializable.class, token = "get", replaceValue = "get('')", cursorOffset = 2, description = "Retrieves a property from the flow context.")
@AutocompleteItem(itemType = FUNCTION, returnType = boolean.class, token = "contains", replaceValue = "contains('')", cursorOffset = 2,  description = "Checks whether an attribute with the given key exists in the context.")
@AutocompleteItem(itemType = FUNCTION, returnType = String.class, token = "toString", replaceValue = "toString()", description = "Returns a string representation of the flow context.")
public interface FlowContext extends Map<String, Serializable>, Disposable {

    /**
     * Registers a disposable object in the context so that it can
     * be disposed when the flow execution ends. This might be
     * used to cleanup Database cursors or other resources which needs
     * to stay open across the whole flow execution.
     *
     * @param disposable the object to be disposed when the flow ends.
     */
    void register(Disposable disposable);
}
