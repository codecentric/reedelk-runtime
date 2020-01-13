package com.reedelk.module.descriptor;

import com.reedelk.module.descriptor.model.AutoCompleteContributorDescriptor;
import com.reedelk.module.descriptor.model.ComponentDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ModuleDescriptor {

    public static final String RESOURCE_FILE_NAME = "components-descriptors.json";

    private List<ComponentDescriptor> componentDescriptors = new ArrayList<>();
    private List<AutoCompleteContributorDescriptor> autocompleteContributorDescriptors = new ArrayList<>();

    public List<ComponentDescriptor> getComponentDescriptors() {
        return componentDescriptors;
    }

    public void setComponentDescriptors(List<ComponentDescriptor> componentDescriptors) {
        this.componentDescriptors = componentDescriptors;
    }

    public List<AutoCompleteContributorDescriptor> getAutocompleteContributorDescriptors() {
        return autocompleteContributorDescriptors;
    }

    public void setAutocompleteContributorDescriptors(List<AutoCompleteContributorDescriptor> autocompleteContributorDescriptors) {
        this.autocompleteContributorDescriptors = autocompleteContributorDescriptors;
    }
}
