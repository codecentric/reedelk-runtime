package com.reedelk.runtime.api.type;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.message.content.Attachment;

import java.util.HashMap;

@Type(mapKeyType = String.class, mapValueType = Attachment.class)
public class MapOfAttachments extends HashMap<String, Attachment> {
}
