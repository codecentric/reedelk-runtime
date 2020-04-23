package com.reedelk.runtime.api.commons;

import com.reedelk.runtime.api.message.content.MimeType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MimeTypeUtilsTest {

    @Test
    void shouldReturnDefaultWhenFileExtensionDoesNotExist() {
        // Given
        boolean autoMimeType = true;
        String filePath = "/Users/documents/myfile";

        // When
        MimeType actual =
                MimeTypeUtils.fromFileExtensionOrParse(autoMimeType, filePath, null, MimeType.APPLICATION_BINARY);

        // Then
        assertThat(actual).isEqualTo(MimeType.APPLICATION_BINARY);
    }

    @Test
    void shouldReturnParsedMimeTypeWhenNotAutoMimeType() {
        // Given
        boolean autoMimeType = false;
        String filePath = "/Users/documents/myfile.txt";

        // When
        MimeType actual =
                MimeTypeUtils.fromFileExtensionOrParse(autoMimeType, filePath, "image/jpeg", MimeType.APPLICATION_BINARY);

        // Then
        assertThat(actual).isEqualTo(MimeType.IMAGE_JPEG);
    }
}
