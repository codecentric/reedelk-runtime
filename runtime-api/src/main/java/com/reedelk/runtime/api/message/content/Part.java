package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@AutocompleteType
public class Part implements Serializable {

    private final String name;
    private final TypedContent<?,?> content;
    private final Map<String,String> attributes = new HashMap<>();

    private Part(String name, TypedContent<?,?> content, Map<String,String> attributes) {
        this.name = name;
        this.content = content;
        this.attributes.putAll(attributes);
    }

    public static Builder builder() {
        return new Builder();
    }

    @AutocompleteItem(replaceValue = "getName()", description = "Returns the name of the part object.")
    public String getName() {
        return name;
    }

    @AutocompleteItem(replaceValue = "getContent()", description = "Returns content of the part object.")
    public TypedContent<?,?> getContent() {
        return content;
    }

    @AutocompleteItem(replaceValue = "getAttributes()", description = "Returns the attributes of the part object.")
    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "Part{" +
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

        public Part build() {
            return new Part(name, content, attributes);
        }
    }
}
