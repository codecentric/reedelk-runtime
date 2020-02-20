package com.reedelk.module.descriptor;

import com.reedelk.module.descriptor.model.AutoCompleteContributorDescriptor;
import com.reedelk.module.descriptor.model.ComponentDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ModuleDescriptor {

    public static final String RESOURCE_FILE_NAME = "module-descriptor.json";

    private List<ComponentDescriptor> components = new ArrayList<>();
    private List<AutoCompleteContributorDescriptor> autocompleteContributors = new ArrayList<>();

    public List<ComponentDescriptor> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentDescriptor> components) {
        this.components = components;
    }

    public List<AutoCompleteContributorDescriptor> getAutocompleteContributors() {
        return autocompleteContributors;
    }

    public void setAutocompleteContributors(List<AutoCompleteContributorDescriptor> autocompleteContributors) {
        this.autocompleteContributors = autocompleteContributors;
    }
}
