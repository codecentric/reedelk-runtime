package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AutocompleteType(description = "An attachment encapsulates an HTTP multipart form data object or an email attachment.")
@SuppressWarnings("unchecked")
public class Attachment implements Serializable {

    private final String name;
    private final TypedContent<?,?> content;
    private final Map<String,String> attributes = new HashMap<>();

    // The name of an attachment might be null. This might be the case when the user
    // creates an HTTP Multipart attachment from the script language. In that case the name
    // of the part is the key of the attachments map.
    private Attachment(String name, TypedContent<?,?> content, Map<String,String> attributes) {
        this.name = name;
        this.content = content;
        this.attributes.putAll(attributes);
    }

    public static Builder builder() {
        return new Builder();
    }

    @AutocompleteItem(
            signature = "name()",
            example = "attachment.name()",
            description = "Returns the name of the attachment.")
    public String name() {
        return name;
    }

    public String getName() {
        return name;
    }

    @AutocompleteItem(
            signature = "payload()",
            example = "attachment.payload()",
            description = "Returns the payload of the attachment.")
    public <Type> Type payload() {
        return (Type) Optional.ofNullable(content)
                .map(TypedContent::data)
                .orElse(null);
    }

    @AutocompleteItem(
            signature = "getPayload()",
            example = "attachment.getPayload()",
            description = "Returns the payload of the attachment.")
    public <Type> Type getPayload() {
        return payload();
    }

    @AutocompleteItem(
            signature = "content()",
            example = "attachment.content()",
            description = "Returns the content of the attachment.")
    public <Type, StreamType, T extends TypedContent<Type, StreamType>> T content() {
        return (T) content;
    }

    public <Type, StreamType, T extends TypedContent<Type, StreamType>> T getContent() {
        return (T) content;
    }

    @AutocompleteItem(
            signature = "attributes()",
            example = "attachment.attributes()",
            description = "Returns the attributes of the attachment.")
    public Map<String, String> attributes() {
        return attributes;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "name='" + name + '\'' +
                ", content=" + content +
                ", attributes=" + attributes +
                '}';
    }

    public static class Builder {

        private String name;
        private TypedContent<?,?> content;
        private Map<String,String> attributes = new HashMap<>();

        public Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder content(TypedContent<?,?> content) {
            this.content = content;
            return this;
        }

        public Builder attribute(String key, String value) {
            this.attributes.put(key, value);
            return this;
        }

        public Attachment build() {
            return new Attachment(name, content, attributes);
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
