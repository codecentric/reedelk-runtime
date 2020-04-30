package com.reedelk.runtime.api.exception;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.TypeFunction;

@Type(description = "The Error type encapsulates an error.")
public interface Error {

    @TypeFunction(
            signature = "getMessage()",
            example = "error.getMessage()",
            description = "Returns the error message.")
    String getMessage();

    @TypeFunction(
            signature = "toString()",
            example = "error.toString()",
            description = "Returns a string representation of the error.")
    String toString();
}
