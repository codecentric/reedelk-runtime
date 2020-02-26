package com.reedelk.runtime.autocomplete;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.autocomplete.AutocompleteItemType;
import com.reedelk.runtime.api.autocomplete.ScriptType;

@AutocompleteType(TypeMessageAttributes.TYPE)
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = ScriptType.SERIALIZABLE,
        token = "get",
        replaceValue = "get('')",
        cursorOffset = 2, // TODO: Consider to use -2
        description = "Returns the given attribute")
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = ScriptType.BOOLEAN,
        token = "contains",
        replaceValue = "contains('')",
        cursorOffset = 2, // TODO: Consider to use -2
        description = "Returns the given attribute")
public class TypeMessageAttributes {

    public static final String TYPE = "MessageAttributes";
}
