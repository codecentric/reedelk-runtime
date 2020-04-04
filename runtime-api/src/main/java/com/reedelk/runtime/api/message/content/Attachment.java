package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@AutocompleteType(description =
        "An attachment encapsulates a REST Multipart Part or a Mail Attachment.")
public class Attachment implements Serializable {

    private final String name;
    private final TypedContent<?,?> content;
    private final Map<String,String> attributes = new HashMap<>();

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
            description = "Returns the name of the attachment object.")
    public String name() {
        return name;
    }

    public String getName() {
        return name;
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
                "name='" + name + '\'' +
                ", content=" + content +
                ", attributes=" + attributes +
                '}';
    }

    public static class Builder {

        private String name;
        private TypedContent<?,?> content;
        private Map<String,String> attributes = new HashMap<>();

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
}
