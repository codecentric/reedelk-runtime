package com.reedelk.runtime.api.message.content;

import java.util.HashMap;
import java.util.Map;

public class Part {

    private final String name;
    private final TypedContent<?> content;
    private final Map<String,String> attributes = new HashMap<>();

    private Part(String name, TypedContent<?> content, Map<String,String> attributes) {
        this.name = name;
        this.content = content;
        this.attributes.putAll(attributes);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public TypedContent<?> getContent() {
        return content;
    }

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
        private TypedContent<?> content;
        private Map<String,String> attributes = new HashMap<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder content(TypedContent<?> content) {
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
