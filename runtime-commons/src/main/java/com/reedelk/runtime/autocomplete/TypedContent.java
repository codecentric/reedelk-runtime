package com.reedelk.runtime.autocomplete;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.autocomplete.AutocompleteItemType;
import com.reedelk.runtime.api.autocomplete.ScriptType;

@AutocompleteType(TypedContent.TYPE)
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        token = "type",
        replaceValue = "type()",
        description = "Returns the type of the content.")
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = TypeMimeType.TYPE,
        token = "mimeType",
        replaceValue = "mimeType()",
        description = "Returns the mime type of the content.")
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        token = "stream",
        replaceValue = "stream()",
        description = "Returns content as a stream.")
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = ScriptType.BOOLEAN,
        token = "isStream",
        replaceValue = "isStream()",
        description = "Returns true if the content is a stream, false otherwise.")
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = ScriptType.BOOLEAN,
        token = "isConsumed",
        replaceValue = "isConsumed()",
        description = "Returns true if the stream content has been consumed already, false otherwise.")
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        token = "consume",
        replaceValue = "consume()",
        description = "Consumes the stream content and loads it into memory.")
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = ScriptType.ANY,
        token = "data",
        replaceValue = "data()",
        description = "Returns the data of this object. If it is a stream, the data is resolved and loaded into memory.")
public class TypedContent {

    public static final String TYPE = "TypedContent";
}
