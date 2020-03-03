package com.reedelk.runtime.api.flow;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.io.Serializable;
import java.util.Map;

import static com.reedelk.runtime.api.autocomplete.AutocompleteItemType.FUNCTION;
import static com.reedelk.runtime.api.autocomplete.AutocompleteItemType.VARIABLE;



@AutocompleteType(
        description = "The FlowContext type encapsulates the execution context for a flow. " +
                "The execution context allows to store and retrieve data which can be accessed by components during " +
                "the execution of a flow. For example the correlation id which is a unique identifier generated " +
                "every time a flow is executed.")
@AutocompleteItem(
        itemType = VARIABLE,
        returnType = String.class,
        example = "context.correlationId",
        token = "correlationId",
        signature = "correlationId",
        description = "Returns the current flow correlation id.")
@AutocompleteItem(
        cursorOffset = 1,
        itemType = FUNCTION,
        returnType = Void.class,
        token = "put",
        signature = "put(key: String, object: Serializable)",
        example = "context.put('myJson', message.payload())",
        description = "Puts an object with the given key into the flow context.")
@AutocompleteItem(
        cursorOffset = 1,
        itemType = FUNCTION,
        returnType = Serializable.class,
        token = "get",
        signature = "get(key: String)",
        example = "context.get('myJson')",
        description = "Retrieves the object stored in the context given the key.")
@AutocompleteItem(
        cursorOffset = 1,
        itemType = FUNCTION,
        returnType = boolean.class,
        token = "contains",
        signature = "contains(key: String)",
        example = "context.contains('myJson')",
        description = "Checks whether an object with the given key exists in the context.")
@AutocompleteItem(
        itemType = FUNCTION,
        returnType = String.class,
        token = "toString",
        signature = "toString()",
        example = "context.toString()",
        description = "Returns a string representation of the flow context.")
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
