package com.reedelk.runtime.api.exception;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.autocomplete.AutocompleteItemType;

@com.reedelk.runtime.api.annotation.AutocompleteType
@AutocompleteItem(token = "message", signature = "message", returnType = String.class, itemType = AutocompleteItemType.VARIABLE, description = "Returns the error message.")
public interface Error {

    @AutocompleteItem(signature = "getMessage()", description = "Returns the error message.")
    String getMessage();

    @AutocompleteItem(signature = "toString()", description = "Returns a string representation of the error.")
    String toString();
}
