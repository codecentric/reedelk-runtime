package com.reedelk.runtime.api.exception;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.autocomplete.AutocompleteItemType;

@AutocompleteType
@AutocompleteItem(token = "message", replaceValue = "message", itemType = AutocompleteItemType.VARIABLE, description = "Returns the error message.")
public interface Error {

    @AutocompleteItem(replaceValue = "getMessage()", description = "Returns the error message.")
    String getMessage();

    @AutocompleteItem(replaceValue = "toString()", description = "Returns a string representation of the error.")
    String toString();
}
