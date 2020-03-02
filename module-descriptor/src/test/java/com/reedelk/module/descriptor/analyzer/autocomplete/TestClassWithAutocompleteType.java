package com.reedelk.module.descriptor.analyzer.autocomplete;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import static com.reedelk.runtime.api.autocomplete.AutocompleteItemType.VARIABLE;

@AutocompleteType
@AutocompleteItem(token = "correlationId", signature = "correlationId", returnType = String.class, itemType = VARIABLE, description = "Returns the correlation id")
public class TestClassWithAutocompleteType {

    @AutocompleteItem(signature = "attributes()", example = "message.attributes()", description = "Returns the attributes")
    public String attributes() {
        return "My attributes";
    }

    @AutocompleteItem(signature = "contains('')", cursorOffset = 2, description = "Return the message typed content containing metadata")
    public boolean contains(String key) {
        return true;
    }

    @AutocompleteItem(signature = "builderMethod('')", description = "My description")
    public TestClassWithAutocompleteType builderMethod(String value) {
        return this;
    }

    @AutocompleteItem(signature = "info('')", cursorOffset = 2, description = "Logs a message with INFO level")
    public void info(String message) {
        // nothing
    }
}
