package com.reedelk.module.descriptor.model.type;

import java.util.ArrayList;
import java.util.List;

public class TypeDescriptor {

    private boolean global;

    // The fully qualified name of the type.
    private String fullyQualifiedName;
    // The display name is to make something nicer, for instance if the name is Map for attributes
    // then the display name could be used to display e.g. MapXYZAttributes and so on.
    private String displayName;
    private String description;

    private String listItemType;

    private List<TypeFunctionDescriptor> functions = new ArrayList<>();
    private List<TypePropertyDescriptor> properties = new ArrayList<>();

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public void setFullyQualifiedName(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getListItemType() {
        return listItemType;
    }

    public void setListItemType(String listItemType) {
        this.listItemType = listItemType;
    }

    public List<TypeFunctionDescriptor> getFunctions() {
        return functions;
    }

    public void setFunctions(List<TypeFunctionDescriptor> functions) {
        this.functions = functions;
    }

    public List<TypePropertyDescriptor> getProperties() {
        return properties;
    }

    public void setProperties(List<TypePropertyDescriptor> properties) {
        this.properties = properties;
    }
}
