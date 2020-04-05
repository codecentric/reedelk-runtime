package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@AutocompleteType(description = "An attachment encapsulates an HTTP multipart form data object or an email attachment.")
public class Attachment implements Serializable {

    private final TypedContent<?,?> content;
    private final Map<String,String> attributes = new HashMap<>();

    private Attachment(TypedContent<?,?> content, Map<String,String> attributes) {
        this.content = content;
        this.attributes.putAll(attributes);
    }

    public static Builder builder() {
        return new Builder();
    }

    @AutocompleteItem(
            signature = "content()",
            example = "attachment.content()",
            description = "Returns the content of the attachment.")
    public TypedContent<?,?> content() {
        return content;
    }

    public TypedContent<?,?> getContent() {
        return content;
    }

    @AutocompleteItem(
            signature = "attributes()",
            example = "attachment.attributes()",
            description = "Returns the attributes of the attachment.")
    public Map<String,String> attributes() {
        return attributes;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                ", content=" + content +
                ", attributes=" + attributes +
                '}';
    }

    public static class Builder {

        private TypedContent<?,?> content;
        private Map<String,String> attributes = new HashMap<>();

        public Builder content(TypedContent<?,?> content) {
            this.content = content;
            return this;
        }

        public Builder attribute(String key, String value) {
            this.attributes.put(key, value);
            return this;
        }

        public Attachment build() {
            return new Attachment(content, attributes);
        }
    }

    /**
     * A map is an 'attachment' map if and only if all its keys are
     * type string and all its values are of type attachment.
     */
    public static boolean isAttachmentMap(Object value) {
        if (value == null) return false;
        if (!(value instanceof Map)) return false;
        Map<?,?> maybeAttachmentMap = (Map<?,?>) value;
        for (Map.Entry<?,?> entry : maybeAttachmentMap.entrySet()) {
            Object key = entry.getKey();
            Object entryValue = entry.getValue();
            boolean isKeyString = key instanceof String;
            boolean isValueAttachment = entryValue instanceof Attachment;
            if (!isKeyString || !isValueAttachment) return false;
        }
        return true;
    }
}
