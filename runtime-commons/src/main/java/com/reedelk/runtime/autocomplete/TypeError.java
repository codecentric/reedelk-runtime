package com.reedelk.runtime.autocomplete;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.autocomplete.AutocompleteItemType;
import com.reedelk.runtime.api.autocomplete.ScriptType;

@AutocompleteType(TypeError.TYPE)
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = ScriptType.STRING,
        token = "getMessage",
        replaceValue = "getMessage()",
        description = "Returns the exception message")
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = ScriptType.STRING,
        token = "toString",
        replaceValue = "toString()",
        description = "Converts the error to a string")
public class TypeError {

    public static final String TYPE = "Exception";
}
