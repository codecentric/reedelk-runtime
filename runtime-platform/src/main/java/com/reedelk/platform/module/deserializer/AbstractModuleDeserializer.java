package com.reedelk.platform.module.deserializer;

import com.reedelk.platform.module.DeSerializedModule;
import com.reedelk.platform.module.ModuleDeserializer;
import com.reedelk.platform.services.resource.ResourceLoader;
import com.reedelk.runtime.api.commons.FileUtils;
import com.reedelk.runtime.commons.JsonParser;
import org.json.JSONObject;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.reedelk.runtime.commons.FileExtension.*;
import static com.reedelk.runtime.commons.ModuleProperties.*;
import static java.util.stream.Collectors.toSet;

abstract class AbstractModuleDeserializer implements ModuleDeserializer {

    @Override
    public DeSerializedModule deserialize() {

        Set<JSONObject> flows = objectsWithRoot(
                getResources(Flow.RESOURCE_DIRECTORY, FLOW.value()),
                Flow.ROOT_PROPERTY);

        Set<JSONObject> subflows = objectsWithRoot(
                getResources(Subflow.RESOURCE_DIRECTORY, SUBFLOW.value()),
                Subflow.ROOT_PROPERTY);

        Collection<JSONObject> configurations = getConfigurations();

        Collection<ResourceLoader> scripts = getScripts();

        Collection<ResourceLoader> resources = getResources();

        return new DeSerializedModule(flows, subflows, configurations, scripts, resources);
    }

    protected abstract List<URL> getResources(String directory);

    protected abstract List<URL> getResources(String directory, String suffix);


    private Set<JSONObject> objectsWithRoot(List<URL> resourcesURL, String rootPropertyName) {
        return resourcesURL.stream()
                .map(FileUtils.ReadFromURL::asString)
                .map(JsonParser::from)
                .filter(object -> object.has(rootPropertyName))
                .collect(toSet());
    }

    private Collection<JSONObject> getConfigurations() {
        List<URL> resourcesURL = getResources(Config.RESOURCE_DIRECTORY, CONFIG.value());
        return resourcesURL.stream()
                .map(FileUtils.ReadFromURL::asString)
                .map(JsonParser::from)
                .collect(toSet());
    }

    // Must provide name of the script starting from resources/scripts base path.
    private Collection<ResourceLoader> getScripts() {
        List<URL> resourcesURL = getResources(Script.RESOURCE_DIRECTORY, SCRIPT.value());
        return resourcesURL.stream()
                .filter(Objects::nonNull)
                .map(ResourceLoader::new)
                .collect(toSet());
    }

    // Load all resources from src/main/resources folder.
    private Collection<ResourceLoader> getResources() {
        List<URL> resourcesURL = getResources(Resource.RESOURCE_DIRECTORY);
        return resourcesURL.stream()
                .filter(Objects::nonNull)
                .map(ResourceLoader::new)
                .collect(toSet());
    }
}
