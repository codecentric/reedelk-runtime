package com.reedelk.module.descriptor.model;

import com.reedelk.module.descriptor.model.component.ComponentDescriptor;
import com.reedelk.module.descriptor.model.type.TypeDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ModuleDescriptor {

    /**
     * The name of the JSON file name to be created in the module containing descriptions
     * of the components and types provided by this module.
     */
    public static final String RESOURCE_FILE_NAME = "module-descriptor.json";

    /**
     * The name of this module (the OSGi bundle name).
     */
    private String name;
    /**
     * The display name of this module.
     */
    private String displayName;
    /**
     * A list of components descriptors. A module might contain 0 or more components
     * to be used inside integration flows.
     */
    private List<ComponentDescriptor> components = new ArrayList<>();
    /*
     * A list of types defined by this module. The types might be global or local.
     * A type might be a map returned in the Message attributes or the type returned in the
     * message payload.
     */
    private List<TypeDescriptor> types = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<ComponentDescriptor> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentDescriptor> components) {
        this.components = components;
    }

    public List<TypeDescriptor> getTypes() {
        return types;
    }

    public void setTypes(List<TypeDescriptor> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "ModuleDescriptor{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", components=" + components +
                ", types=" + types +
                '}';
    }
}
