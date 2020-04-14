package com.reedelk.platform.module;

import com.reedelk.platform.services.resource.ResourceLoader;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Set;

public class DeSerializedModule {

    private final Set<JSONObject> flows;
    private final Set<JSONObject> subflows;
    private final Collection<JSONObject> configurations;
    private final Collection<ResourceLoader> scripts;
    private final Collection<ResourceLoader> resources;

    public DeSerializedModule(Set<JSONObject> flows,
                              Set<JSONObject> subflows,
                              Collection<JSONObject> configurations,
                              Collection<ResourceLoader> scripts,
                              Collection<ResourceLoader> resources) {
        this.flows = flows;
        this.scripts = scripts;
        this.subflows = subflows;
        this.resources = resources;
        this.configurations = configurations;
    }

    public Set<JSONObject> getFlows() {
        return flows;
    }

    public Set<JSONObject> getSubflows() {
        return subflows;
    }

    public Collection<JSONObject> getConfigurations() {
        return configurations;
    }

    public Collection<ResourceLoader> getScripts() {
        return scripts;
    }

    public Collection<ResourceLoader> getResources() {
        return resources;
    }
}
