package com.reedelk.module.descriptor.analyzer.autocomplete;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import static com.reedelk.runtime.api.autocomplete.AutocompleteItemType.VARIABLE;

@AutocompleteType
@AutocompleteItem(token = "correlationId", replaceValue = "correlationId", returnType = String.class, itemType = VARIABLE, description = "Returns the correlation id")
public class TestClassWithAutocompleteType {

    @AutocompleteItem(replaceValue = "attributes()", example = "message.attributes()", description = "Returns the attributes")
    public String attributes() {
        return "My attributes";
    }

    @AutocompleteItem(replaceValue = "contains('')", cursorOffset = 2, description = "Return the message typed content containing metadata")
    public boolean contains(String key) {
        return true;
    }

    @AutocompleteItem(replaceValue = "builderMethod('')", description = "My description")
    public TestClassWithAutocompleteType builderMethod(String value) {
        return this;
    }

    @AutocompleteItem(replaceValue = "info('')", cursorOffset = 2, description = "Logs a message with INFO level")
    public void info(String message) {
        // nothing
    }
}
