package com.reedelk.runtime.autocomplete;


import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.autocomplete.AutocompleteItemType;
import com.reedelk.runtime.api.autocomplete.ScriptType;

@AutocompleteType(TypeMimeType.TYPE)
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = ScriptType.STRING,
        token = "toString",
        replaceValue = "toString()",
        description = "Returns the mime type as a string")
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = ScriptType.STRING,
        token = "getPrimaryType",
        replaceValue = "getPrimaryType()",
        description = "Returns the primary type")
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = ScriptType.STRING,
        token = "getSubType",
        replaceValue = "getSubType()",
        description = "Returns the sub type")
public class TypeMimeType {

    public static final String TYPE = "MimeType";
}
