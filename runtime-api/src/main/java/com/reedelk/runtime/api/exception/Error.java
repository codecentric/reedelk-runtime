package com.reedelk.runtime.api.exception;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.TypeFunction;

// TODO: I think that this one should be specific to the methods provided by the RESTListener which might wrap
// the error to provide additional info like status code and so on.
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
