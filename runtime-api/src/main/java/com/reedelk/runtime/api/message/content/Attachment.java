package com.reedelk.runtime.api.message.content;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.TypeFunction;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.reedelk.runtime.api.commons.Preconditions.checkNotNull;

@Type(description = "An attachment encapsulates an HTTP multipart form data object or an email attachment.")
public class Attachment implements Serializable {

    private final String name;
    private final byte[] data;
    private final MimeType mimeType;
    private final Map<String,String> attributes = new HashMap<>();

    // The name of an attachment might be null. This might be the case when the user
    // creates an HTTP Multipart attachment from the script language. In that case the name
    // of the part is the key of the attachments map.
    private Attachment(String name, byte[] data, MimeType mimeType, Map<String,String> attributes) {
        checkNotNull(mimeType, "attachment mimeType must not be null");
        this.name = name;
        this.data = data;
        this.mimeType = mimeType;
        this.attributes.putAll(attributes);
    }

    public static Builder builder() {
        return new Builder();
    }

    @TypeFunction(
            signature = "name()",
            example = "attachment.name()",
            description = "Returns the name of the attachment.")
    public String name() {
        return name;
    }

    @TypeFunction(
            signature = "data()",
            example = "attachment.data()",
            description = "Returns the data of the attachment.")
    public byte[] data() {
        return data;
    }

    @TypeFunction(
            signature = "mimeType()",
            example = "attachment.mimeType()",
            description = "Returns the mime type of the attachment.")
    public MimeType mimeType() {
        return mimeType;
    }

    @TypeFunction(
            signature = "attributes()",
            example = "attachment.attributes()",
            description = "Returns the attributes of the attachment.")
    public Map<String, String> attributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "name='" + name + '\'' +
                ", data=" + Arrays.toString(data) +
                ", mimeType=" + mimeType +
                ", attributes=" + attributes +
                '}';
    }

    public static class Builder {

        private String name;
        private byte[] data;
        private MimeType mimeType;
        private Map<String,String> attributes = new HashMap<>();

        public Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder data(byte[] data) {
            this.data = data;
            return this;
        }

        public Builder mimeType(MimeType mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder attribute(String key, String value) {
            this.attributes.put(key, value);
            return this;
        }

        public Attachment build() {
            return new Attachment(name, data, mimeType, attributes);
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
