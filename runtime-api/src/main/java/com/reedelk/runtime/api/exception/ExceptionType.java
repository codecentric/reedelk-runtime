package com.reedelk.runtime.api.exception;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.TypeFunction;
import com.reedelk.runtime.api.annotation.TypeProperty;

@Type(displayName = "Exception",
        description = "The Exception type encapsulates an error thrown by the execution of a component.")
@TypeProperty(name = "message", example = "error.message", type = String.class, description = "Returns the error message.")
public interface ExceptionType {

    @TypeFunction(
            signature = "toString()",
            example = "error.toString()",
            description = "Returns a string representation of the error.")
    String toString();
}
