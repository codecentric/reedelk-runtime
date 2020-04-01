package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.Parts;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JavaType {

    private static final Map<MimeType, Class<?>> MIME_TYPE_JAVA_CLASS_MAP;
    static {
        Map<MimeType, Class<?>> tmp = new HashMap<>();
        tmp.put(MimeType.ANY, Object.class);

        tmp.put(MimeType.TEXT, String.class);
        tmp.put(MimeType.TEXT_XML, String.class);
        tmp.put(MimeType.TEXT_COMMA_SEPARATED_VALUES, String.class);
        tmp.put(MimeType.TEXT_CSS, String.class);
        tmp.put(MimeType.TEXT_JSON, String.class);
        tmp.put(MimeType.TEXT_HTML, String.class);
        tmp.put(MimeType.TEXT_JAVASCRIPT, String.class);
        tmp.put(MimeType.APPLICATION_JAVASCRIPT, String.class);
        tmp.put(MimeType.APPLICATION_RSS, String.class);
        tmp.put(MimeType.APPLICATION_ATOM, String.class);
        tmp.put(MimeType.APPLICATION_XML, String.class);
        tmp.put(MimeType.APPLICATION_JSON, String.class);
        tmp.put(MimeType.APPLICATION_JAVA, Object.class);
        tmp.put(MimeType.APPLICATION_FORM_URL_ENCODED, String.class);
        tmp.put(MimeType.MULTIPART_FORM_DATA, Parts.class);

        tmp.put(MimeType.IMAGE_PNG, byte[].class);
        tmp.put(MimeType.UNKNOWN, byte[].class);
        tmp.put(MimeType.APPLICATION_BINARY, byte[].class);

        MIME_TYPE_JAVA_CLASS_MAP = Collections.unmodifiableMap(tmp);
    }

    public static Class<?> from(MimeType mimeType) {
        if (MIME_TYPE_JAVA_CLASS_MAP.containsKey(mimeType)) {
            return MIME_TYPE_JAVA_CLASS_MAP.get(mimeType);
        }
        return Object.class;
    }
}
