package com.reedelk.platform.module.deserializer;


import com.reedelk.platform.commons.ResourcePath;
import com.reedelk.platform.module.DeSerializedModule;
import com.reedelk.platform.services.resource.ResourceLoader;
import com.reedelk.platform.test.utils.TmpDir;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class FileSystemDeserializerTest {

    private Path projectDir;

    @BeforeEach
    void setUp() {
        String tmpDir = TmpDir.get();
        this.projectDir = Paths.get(tmpDir);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @AfterEach
    void tearDown() {
        projectDir.toFile().delete();
    }

    @Test
    void shouldDeserializeFlows() throws IOException {
        // Given
        createProjectFile(Paths.get("flows", "flow1.flow"), flowWith("aaa", "Flow1"));
        createProjectFile(Paths.get("flows", "flow2.flow"), flowWith("bbb", "Flow2"));
        createProjectFile(Paths.get("flows", "flow3.flow"), flowWith("ccc", "Flow3"));
        createProjectFile(Paths.get("flows", "flow4.flow"), flowWith("ddd", "Flow4"));
        createProjectFile(Paths.get("flows", "nested", "flow4.flow"), flowWith("eee", "Flow5"));

        FileSystemDeserializer deserializer = new FileSystemDeserializer(projectDir.toString());

        // When
        DeSerializedModule deSerializedModule = deserializer.deserialize();

        // Then
        Set<JSONObject> flows = deSerializedModule.getFlows();
        assertExists(flows, "id", "aaa");
        assertExists(flows, "id", "bbb");
        assertExists(flows, "id", "ccc");
        assertExists(flows, "id", "ddd");
        assertExists(flows, "id", "eee");
        assertThat(flows).hasSize(5);

        assertThat(deSerializedModule.getConfigurations()).isEmpty();
        assertThat(deSerializedModule.getSubflows()).isEmpty();
        assertThat(deSerializedModule.getScripts()).isEmpty();
    }

    @Test
    void shouldDeserializeSubFlows() throws IOException {
        // Given
        createProjectFile(Paths.get("flows", "subflow1.subflow"), subFlowWith("aaa", "SubFlow1"));
        createProjectFile(Paths.get("flows", "subflow2.subflow"), subFlowWith("bbb", "SubFlow2"));
        createProjectFile(Paths.get("flows", "subflow3.subflow"), subFlowWith("ccc", "SubFlow3"));
        createProjectFile(Paths.get("flows", "subflow4.subflow"), subFlowWith("ddd", "SubFlow4"));
        createProjectFile(Paths.get("flows", "nested", "subflow5.subflow"), subFlowWith("eee", "SubFlow5"));

        FileSystemDeserializer deserializer = new FileSystemDeserializer(projectDir.toString());

        // When
        DeSerializedModule deSerializedModule = deserializer.deserialize();

        // Then
        Set<JSONObject> subFlows = deSerializedModule.getSubflows();
        assertExists(subFlows, "id", "aaa");
        assertExists(subFlows, "id", "bbb");
        assertExists(subFlows, "id", "ccc");
        assertExists(subFlows, "id", "ddd");
        assertExists(subFlows, "id", "eee");
        assertThat(subFlows).hasSize(5);

        assertThat(deSerializedModule.getConfigurations()).isEmpty();
        assertThat(deSerializedModule.getFlows()).isEmpty();
        assertThat(deSerializedModule.getScripts()).isEmpty();
    }

    @Test
    void shouldDeserializeConfigurations() throws IOException {
        // Given
        createProjectFile(Paths.get("configs", "config1.fconfig"), configWith("aaa", "Config1"));
        createProjectFile(Paths.get("configs", "config2.fconfig"), configWith("bbb", "Config2"));
        createProjectFile(Paths.get("configs", "config3.fconfig"), configWith("ccc", "Config3"));
        createProjectFile(Paths.get("configs", "config4.fconfig"), configWith("ddd", "Config4"));
        createProjectFile(Paths.get("configs", "nested", "config5.fconfig"), configWith("eee", "Config5"));

        FileSystemDeserializer deserializer = new FileSystemDeserializer(projectDir.toString());

        // When
        DeSerializedModule deSerializedModule = deserializer.deserialize();

        // Then
        Collection<JSONObject> configurations = deSerializedModule.getConfigurations();
        assertExists(configurations, "id", "aaa");
        assertExists(configurations, "id", "bbb");
        assertExists(configurations, "id", "ccc");
        assertExists(configurations, "id", "ddd");
        assertExists(configurations, "id", "eee");
        assertThat(configurations).hasSize(5);

        assertThat(deSerializedModule.getFlows()).isEmpty();
        assertThat(deSerializedModule.getSubflows()).isEmpty();
        assertThat(deSerializedModule.getScripts()).isEmpty();
    }

    @Test
    void shouldDeserializeScripts() throws IOException {
        // Given
        String script1Body = "return 'aaa'";
        createProjectFile(Paths.get("scripts", "script1.js"), script1Body);
        createProjectFile(Paths.get("scripts", "script2.js"), "return 'bbb'");
        createProjectFile(Paths.get("scripts", "script3.js"), "return 'ccc'");
        createProjectFile(Paths.get("scripts", "script4.js"), "return 'ddd'");
        createProjectFile(Paths.get("scripts", "nested", "script5.js"), "return 'eee'");

        FileSystemDeserializer deserializer = new FileSystemDeserializer(projectDir.toString());

        // When
        DeSerializedModule deSerializedModule = deserializer.deserialize();

        // Then
        Collection<ResourceLoader> resourceLoaders = deSerializedModule.getScripts();
        assertExist(resourceLoaders,  Paths.get(projectDir.toString(), "scripts", "script1.js").toUri().toURL(), script1Body);
        assertThat(resourceLoaders).hasSize(5);

        assertThat(deSerializedModule.getFlows()).isEmpty();
        assertThat(deSerializedModule.getSubflows()).isEmpty();
        assertThat(deSerializedModule.getConfigurations()).isEmpty();
    }

    private void createProjectFile(Path filePathAndName, String fileContent) throws IOException {
        Path path = Paths.get(projectDir.toString(), filePathAndName.toString());
        createFile(path, fileContent);
    }

    private String flowWith(String id, String title) {
        return "{ \"id\": " + id +", \"title\":" + title + ", \"flow\": []}";
    }

    private String subFlowWith(String id, String title) {
        return "{ \"id\": " + id +", \"title\":" + title + ", \"subflow\": []}";
    }

    private String configWith(String id, String title) {
        return "{ \"id\": " + id +", \"title\":" + title + "}";
    }

    private void assertExists(Collection<JSONObject> objects, String propertyKey, String propertyValue) {
        boolean found = objects.stream()
                .anyMatch(jsonObj -> jsonObj.has(propertyKey) && jsonObj.get(propertyKey).equals(propertyValue));
        assertThat(found)
                .withFailMessage("Object with property key=[%s] and value=[%s] not found", propertyKey, propertyValue)
                .isTrue();
    }

    private void assertExist(Collection<ResourceLoader> resourceLoaders, URL scriptFilePath, String scriptBody) {
        boolean found = resourceLoaders.stream()
                .anyMatch(resourceLoader -> {
                    String bodyAsString = resourceLoader.bodyAsString();
                    System.out.println(bodyAsString);
                    String resourceFilePath = resourceLoader.getResourceFilePath();
                    System.out.println(resourceFilePath);
                    return bodyAsString.equals(scriptBody) && resourceLoader.getResourceFilePath().equals(ResourcePath.from(scriptFilePath));
                });
        assertThat(found)
                .withFailMessage("Script with file path=[%s], and body=[%s] not found", scriptFilePath, scriptBody)
                .isTrue();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createFile(Path filePathAndName, String content) throws IOException {
        new File(filePathAndName.toFile().getParent()).mkdirs();
        try (FileOutputStream os = new FileOutputStream(filePathAndName.toFile())) {
            os.write(content.getBytes());
            os.flush();
        }
    }
}
