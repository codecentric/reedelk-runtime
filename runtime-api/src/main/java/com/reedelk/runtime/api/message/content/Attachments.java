package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.util.HashMap;

import static com.reedelk.runtime.api.autocomplete.AutocompleteItemType.FUNCTION;

@AutocompleteType(description =
        "An Attachments object groups several attachments objects. " +
        "An attachment object encapsulates a REST Multipart Part or a Mail Attachment.")
@AutocompleteItem(
        cursorOffset = 1,
        itemType = FUNCTION,
        returnType = Void.class,
        token = "put",
        signature = "put(attachmentName: String, attachment: Attachment)",
        example = "attachments.put('file', attachment)",
        description = "Puts the given attachment object into the attachments map with the given name.")
@AutocompleteItem(
        cursorOffset = 1,
        itemType = FUNCTION,
        returnType = Attachment.class,
        token = "get",
        signature = "get(attachmentName: String)",
        example = "attachments.get('file')",
        description = "Returns the attachment object with the given name.")
public class Attachments extends HashMap<String, Attachment> {
}
