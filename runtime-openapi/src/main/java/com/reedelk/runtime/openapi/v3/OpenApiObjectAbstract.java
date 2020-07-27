package com.reedelk.runtime.openapi.v3;

import com.reedelk.runtime.openapi.OpenApiSerializableAbstract;
import com.reedelk.runtime.openapi.OpenApiSerializableContext;

import java.util.*;

import static java.util.Optional.ofNullable;

public class OpenApiObjectAbstract extends OpenApiSerializableAbstract {

    private static final String OPEN_API_VERSION = "3.0.3";
    
    // Info Object is required by spec
    private InfoObject info = new InfoObject();
    private ComponentsObject components = new ComponentsObject();
    private List<ServerObject> servers = new ArrayList<>();
    private PathsObject paths = new PathsObject();
    private String basePath;

    public InfoObject getInfo() {
        return info;
    }

    public void setInfo(InfoObject info) {
        this.info = info;
    }

    public List<ServerObject> getServers() {
        return servers;
    }

    public void setServers(List<ServerObject> servers) {
        this.servers = servers;
    }

    public void setPaths(PathsObject paths) {
        this.paths = paths;
    }

    public PathsObject getPaths() {
        return paths;
    }

    public ComponentsObject getComponents() {
        return components;
    }

    public void setComponents(ComponentsObject components) {
        this.components = components;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public Map<String, Object> serialize(OpenApiSerializableContext context) {
        Map<String, Object> map = new LinkedHashMap<>();
        set(map, "openapi", OPEN_API_VERSION); // REQUIRED
        set(map, "info", info, context); // REQUIRED

        if (servers == null || servers.isEmpty()) {
            // From OpenAPI spec 3.0.3:
            // If the servers property is not provided, or is an empty array,
            // the default value would be a Server Object with a url value of /.
            ServerObject serverObject = new ServerObject();
            serverObject.setUrl(ofNullable(basePath).orElse("/"));
            servers = Collections.singletonList(serverObject);
        }

        set(map, "servers", servers, context);
        set(map, "paths", paths, context); // REQUIRED
        set(map, "components", components, context);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> serialized) {
        if (serialized.containsKey("info")) {
            info.deserialize(getMap(serialized, "info"));
        }
        if (serialized.containsKey("components")) {
            components.deserialize(getMap(serialized, "components"));
        }
        if (serialized.containsKey("servers")) {
            List<Map<String, Object>> serversList = getList(serialized, "servers");
            serversList.forEach(objectMap -> {
                ServerObject serverObject = new ServerObject();
                serverObject.deserialize(objectMap);
                servers.add(serverObject);
            });
        }
        if (serialized.containsKey("paths")) {
            paths.deserialize(getMap(serialized, "paths"));
        }
        basePath = getString(serialized, "basePath");
    }
}
