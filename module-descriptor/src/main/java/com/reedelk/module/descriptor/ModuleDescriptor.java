package com.reedelk.module.descriptor;

import com.reedelk.module.descriptor.model.AutocompleteItemDescriptor;
import com.reedelk.module.descriptor.model.ComponentDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ModuleDescriptor {

    public static final String RESOURCE_FILE_NAME = "module-descriptor.json";

    private String name;
    private List<ComponentDescriptor> components = new ArrayList<>();
    private List<AutocompleteItemDescriptor> autocompleteItems = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ComponentDescriptor> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentDescriptor> components) {
        this.components = components;
    }

    public List<AutocompleteItemDescriptor> getAutocompleteItems() {
        return autocompleteItems;
    }

    public void setAutocompleteItems(List<AutocompleteItemDescriptor> autocompleteItems) {
        this.autocompleteItems = autocompleteItems;
    }
}
