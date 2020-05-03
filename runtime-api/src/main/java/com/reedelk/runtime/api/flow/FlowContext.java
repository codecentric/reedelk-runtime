package com.reedelk.runtime.api.flow;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.TypeFunction;
import com.reedelk.runtime.api.annotation.TypeProperty;

import java.io.Serializable;
import java.util.Map;

@Type(displayName = "FlowContext",
        description = "The FlowContext type encapsulates the execution context for a flow. " +
                "The execution context allows to store and retrieve data which can be accessed by components during " +
                "the execution of a flow. For example the correlation id which is a unique identifier generated " +
                "every time a flow is executed.")
@TypeProperty(
        name = "correlationId",
        type = String.class,
        example = "context.correlationId",
        description = "Returns the current flow correlation id.")
@TypeFunction(
        name = "put",
        cursorOffset = 1,
        returnType = Void.class,
        signature = "put(key: String, object: Object)",
        example = "context.put('myJson', message.payload())",
        description = "Puts an object with the given key into the flow context.")
@TypeFunction(
        name = "get",
        cursorOffset = 1,
        returnType = Serializable.class,
        signature = "get(key: String)",
        example = "context.get('myJson')",
        description = "Retrieves the object stored in the context given the key.")
@TypeFunction(
        name = "contains",
        cursorOffset = 1,
        returnType = boolean.class,
        signature = "contains(key: String)",
        example = "context.contains('myJson')",
        description = "Checks whether an object with the given key exists in the context.")
@TypeFunction(
        name = "toString",
        returnType = String.class,
        signature = "toString()",
        example = "context.toString()",
        description = "Returns a string representation of the flow context.")
public interface FlowContext extends Map<String, Object>, Disposable {

    /**
     * Checks whether the context contains a value with the given key.
     *
     * @param key the key to be checked for existence in the context.
     * @return true if exists a value mapped to the given key in the context, false otherwise.
     */
    boolean contains(String key);

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
