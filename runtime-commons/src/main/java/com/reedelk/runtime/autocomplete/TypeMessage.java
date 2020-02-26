package com.reedelk.runtime.autocomplete;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.autocomplete.AutocompleteItemType;
import com.reedelk.runtime.api.autocomplete.ScriptType;

@AutocompleteType(TypeMessage.TYPE)
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = TypeMessageAttributes.TYPE,
        token = "attributes",
        replaceValue = "attributes()",
        description = "Returns the attributes")
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = TypedContent.TYPE,
        token = "content",
        replaceValue = "content()",
        description = "Return the message typed content containing metadata")
@AutocompleteItem(
        itemType = AutocompleteItemType.FUNCTION,
        returnType = ScriptType.ANY,
        token = "payload",
        replaceValue = "payload()",
        description = "Return the message payload")
public class TypeMessage {

    public static final String TYPE = "Message";

}
