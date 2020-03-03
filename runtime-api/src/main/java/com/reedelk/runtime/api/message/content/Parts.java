package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.util.HashMap;

import static com.reedelk.runtime.api.autocomplete.AutocompleteItemType.FUNCTION;

@AutocompleteType(description = "The Parts type contains all the parts composing a REST Multipart payload.")
@AutocompleteItem(
        cursorOffset = 1,
        itemType = FUNCTION,
        returnType = Void.class,
        token = "put",
        signature = "put(partName: String, part: Part)",
        example = "parts.put('file', part)",
        description = "Puts the part object into the parts map with the given partName.")
@AutocompleteItem(
        cursorOffset = 1,
        itemType = FUNCTION,
        returnType = Part.class,
        token = "get",
        signature = "get(partName: String)",
        example = "parts.get('file')",
        description = "Returns the part object with the given partName.")
public class Parts extends HashMap<String,Part> {
}
