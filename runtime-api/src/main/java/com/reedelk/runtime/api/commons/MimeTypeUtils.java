package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.message.content.MimeType;

public class MimeTypeUtils {

    private MimeTypeUtils() {
    }

    public static MimeType mimeTypeFrom(boolean autoMimeType, String mimeType, String filePath, MimeType defaultMime) {
        if (autoMimeType) {
            String pageFileExtension = FileUtils.getExtension(filePath);
            return MimeType.fromFileExtension(pageFileExtension);
        } else {
            return MimeType.parse(mimeType, defaultMime);
        }
    }
}
