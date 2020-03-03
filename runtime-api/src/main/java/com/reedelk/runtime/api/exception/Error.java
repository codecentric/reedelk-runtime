package com.reedelk.runtime.api.exception;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

@AutocompleteType(description = "The Error type encapsulates an error.")
public interface Error {

    @AutocompleteItem(
            signature = "getMessage()",
            example = "error.getMessage()",
            description = "Returns the error message.")
    String getMessage();

    @AutocompleteItem(
            signature = "toString()",
            example = "error.toString()",
            description = "Returns a string representation of the error.")
    String toString();
}
