package de.codecentric.reedelk.runtime.api.type;

import de.codecentric.reedelk.runtime.api.annotation.Type;
import de.codecentric.reedelk.runtime.api.message.content.Attachment;

import java.util.HashMap;

@Type(mapKeyType = String.class, mapValueType = Attachment.class)
public class MapOfAttachments extends HashMap<String, Attachment> {
}
