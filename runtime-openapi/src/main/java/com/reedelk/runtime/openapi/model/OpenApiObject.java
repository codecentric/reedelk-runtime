package com.reedelk.runtime.openapi.model;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.openapi.AbstractOpenApiSerializable;
import com.reedelk.runtime.openapi.JsonObjectFactory;
import com.reedelk.runtime.openapi.OpenApiSerializableContext;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;

@Collapsible
@Component(service = OpenApiObject.class, scope = ServiceScope.PROTOTYPE)
public class OpenApiObject extends AbstractOpenApiSerializable implements Implementor {

    private static final String OPEN_API_VERSION = "3.0.3";
    
    // Info Object is required by spec
    @Property("Info")
    private InfoObject info = new InfoObject();

    @Property("Components")
    private ComponentsObject components = new ComponentsObject();

    @Property("Servers")
    @TabGroup("Servers")
    @ListDisplayProperty("url")
    @DialogTitle("Server Configuration")
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
    public JSONObject serialize(OpenApiSerializableContext context) {
        JSONObject serialized = JsonObjectFactory.newJSONObject();
        set(serialized, "openapi", OPEN_API_VERSION); // REQUIRED
        set(serialized, "info", info, context); // REQUIRED

        if (servers == null || servers.isEmpty()) {
            // From OpenAPI spec 3.0.3:
            // If the servers property is not provided, or is an empty array,
            // the default value would be a Server Object with a url value of /.
            ServerObject serverObject = new ServerObject();
            serverObject.setUrl(ofNullable(basePath).orElse("/"));
            servers = Collections.singletonList(serverObject);
        }
        set(serialized, "servers", servers, context);
        set(serialized, "paths", paths, context); // REQUIRED
        set(serialized, "components", components, context);
        return serialized;
    }
}
