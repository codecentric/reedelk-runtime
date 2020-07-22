package com.reedelk.runtime.openapi.v3;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class TestYamlParsing {

    @Test
    void shouldDoSomething() {
        // Given
        Yaml yaml = new Yaml();
        String petstoreYamlAsString = string("/petstore.json");

        // When

        Map<String,Object> result = yaml.load(petstoreYamlAsString);

        // Then
        assertThat(result).isNotNull();
    }

    String string(String resourceName) {
        URL resource = TestYamlParsing.class.getResource(resourceName);
        try (Scanner scanner = new Scanner(resource.openStream(), UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } catch (IOException e) {
            throw new RuntimeException("String from URI could not be read.", e);
        }
    }
}
