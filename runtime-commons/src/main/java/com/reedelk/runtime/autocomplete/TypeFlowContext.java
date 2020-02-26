package com.reedelk.runtime.autocomplete;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.autocomplete.AutocompleteItemType;
import com.reedelk.runtime.api.autocomplete.ScriptType;

@AutocompleteType(TypeFlowContext.TYPE)
@AutocompleteItem(
        itemType = AutocompleteItemType.VARIABLE,
        returnType = ScriptType.STRING,
        token = "correlationId",
        replaceValue = "correlationId",
        description = "The current flow correlation id")
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        token = "put",
        replaceValue = "put('',value)",
        cursorOffset = 7,
        description = "Puts an attribute with the given value into the flow attributes map")
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = ScriptType.SERIALIZABLE,
        token = "get",
        replaceValue = "get('')",
        cursorOffset = 2,
        description = "Retrieves an attribute from the flow attributes map")
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = ScriptType.BOOLEAN,
        token = "contains",
        replaceValue = "contains('')",
        cursorOffset = 2,
        description = "Checks if an attribute with the given key exists in the attributes collection.")
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = ScriptType.STRING,
        token = "toString",
        replaceValue = "toString()",
        description = "Convert this object to string.")
public class TypeFlowContext {

    public static final String TYPE = "FlowContext";
}
