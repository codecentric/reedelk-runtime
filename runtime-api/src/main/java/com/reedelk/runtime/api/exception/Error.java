package com.reedelk.runtime.api.exception;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

@AutocompleteType
public interface Error {

    @AutocompleteItem(replaceValue = "getMessage()", description = "Returns the error message.")
    String getMessage();

    @AutocompleteItem(replaceValue = "toString()", description = "Returns a string representation of the error.")
    String toString();
}
