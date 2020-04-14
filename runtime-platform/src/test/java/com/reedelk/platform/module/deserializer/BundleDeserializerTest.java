package com.reedelk.platform.module.deserializer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.Bundle;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class BundleDeserializerTest {

    @Mock
    private Bundle mockBundle;

    private BundleDeserializer deserializer;

    @BeforeEach
    void setUp() {
        deserializer = new BundleDeserializer(mockBundle);
    }

    @Test
    void shouldFindEntriesWithFilePatternGivenSuffix() throws MalformedURLException {
        // Given
        String directory = "/flows";
        String suffix = "flow";
        String expectedFilePath = "*." + suffix;

        URL url1 = new URL("file://test/one");
        URL url2 = new URL("file://test/two");
        List<URL> urls = new ArrayList<>();
        urls.add(url1);
        urls.add(url2);
        Enumeration<URL> expectedEnum = Collections.enumeration(urls);

        doReturn(expectedEnum).when(mockBundle).findEntries(directory, expectedFilePath, true);

        // When
        List<URL> actualList = deserializer.getResources(directory, suffix);

        // Then
        assertThat(actualList).containsExactlyInAnyOrder(url1, url2);
    }
}
