package de.codecentric.reedelk.runtime.api.commons;

import de.codecentric.reedelk.runtime.api.message.content.MimeType;

public class MimeTypeUtils {

    private MimeTypeUtils() {
    }

    public static MimeType fromFileExtensionOrParse(boolean autoMimeType, String filePath, String mimeType, MimeType defaultMime) {
        if (autoMimeType) {
            String pageFileExtension = FileUtils.getExtension(filePath);
            return MimeType.fromFileExtension(pageFileExtension, defaultMime);
        } else {
            return MimeType.parse(mimeType, defaultMime);
        }
    }
}
