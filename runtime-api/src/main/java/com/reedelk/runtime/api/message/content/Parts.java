package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.util.HashMap;

import static com.reedelk.runtime.api.autocomplete.AutocompleteItemType.FUNCTION;

@AutocompleteType
@AutocompleteItem(itemType = FUNCTION, returnType = Void.class, token = "put", replaceValue = "put('',)", cursorOffset = 3, description = "Puts the part into the parts object.")
@AutocompleteItem(itemType = FUNCTION, returnType = Part.class, token = "get", replaceValue = "get('')", cursorOffset = 2, description = "Returns the part with the given name.")
public class Parts extends HashMap<String,Part> {
}
