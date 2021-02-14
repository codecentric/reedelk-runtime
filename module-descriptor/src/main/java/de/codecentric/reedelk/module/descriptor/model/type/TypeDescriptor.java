package de.codecentric.reedelk.module.descriptor.model.type;

import java.util.ArrayList;
import java.util.List;

public class TypeDescriptor {

    private boolean global;

    // The fully qualified name of the type.
    private String type;
    // The fully qualified name of the type it extends (example: public class DatabaseAttributes extends MessageAttributes).
    private String extendsType;

    // The display name is to make something nicer, for instance if the name is Map for attributes
    // then the display name could be used to display e.g. MapXYZAttributes and so on.
    private String displayName;
    private String description;

    // List
    private String listItemType;

    // Map
    private String mapKeyType;
    private String mapValueType;

    private List<TypeFunctionDescriptor> functions = new ArrayList<>();
    private List<TypePropertyDescriptor> properties = new ArrayList<>();

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtendsType() {
        return extendsType;
    }

    public void setExtendsType(String extendsType) {
        this.extendsType = extendsType;
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

    public String getMapKeyType() {
        return mapKeyType;
    }

    public void setMapKeyType(String mapKeyType) {
        this.mapKeyType = mapKeyType;
    }

    public String getMapValueType() {
        return mapValueType;
    }

    public void setMapValueType(String mapValueType) {
        this.mapValueType = mapValueType;
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

    @Override
    public String toString() {
        return "TypeDescriptor{" +
                "global=" + global +
                ", type='" + type + '\'' +
                ", extendsType='" + extendsType + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", listItemType='" + listItemType + '\'' +
                ", mapKeyType='" + mapKeyType + '\'' +
                ", mapValueType='" + mapValueType + '\'' +
                ", functions=" + functions +
                ", properties=" + properties +
                '}';
    }
}
