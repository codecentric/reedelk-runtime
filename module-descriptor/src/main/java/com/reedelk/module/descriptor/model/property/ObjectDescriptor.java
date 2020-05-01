package com.reedelk.module.descriptor.model.property;

import com.reedelk.module.descriptor.model.component.ComponentDataHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.reedelk.runtime.api.commons.StringUtils.EMPTY;

public class ObjectDescriptor implements PropertyTypeDescriptor {

    private static final transient Class<?> type = TypeObject.class;

    private Shared shared;
    private String dialogTitle;
    private Collapsible collapsible;
    private String typeFullyQualifiedName;
    private List<PropertyDescriptor> objectProperties = new ArrayList<>();

    @Override
    public Class<?> getType() {
        return type;
    }

    public Shared getShared() {
        return shared;
    }

    public void setShared(Shared shared) {
        this.shared = shared;
    }

    public Collapsible getCollapsible() {
        return collapsible;
    }

    public void setCollapsible(Collapsible collapsible) {
        this.collapsible = collapsible;
    }

    public String getTypeFullyQualifiedName() {
        return typeFullyQualifiedName;
    }

    public void setTypeFullyQualifiedName(String typeFullyQualifiedName) {
        this.typeFullyQualifiedName = typeFullyQualifiedName;
    }

    public List<PropertyDescriptor> getObjectProperties() {
        return objectProperties;
    }

    public void setObjectProperties(List<PropertyDescriptor> objectProperties) {
        this.objectProperties = objectProperties;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    @Override
    public String toString() {
        return "ObjectDescriptor{" +
                "shared=" + shared +
                ", dialogTitle='" + dialogTitle + '\'' +
                ", collapsible=" + collapsible +
                ", typeFullyQualifiedName='" + typeFullyQualifiedName + '\'' +
                ", objectProperties=" + objectProperties +
                '}';
    }

    public static TypeObject newInstance() {
        return new TypeObject();
    }

    public static class TypeObject implements ComponentDataHolder {

        public static final String DEFAULT_CONFIG_REF = EMPTY;

        private Map<String, Object> objectDataHolder = new HashMap<>();

        private TypeObject() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(String key) {
            return (T) objectDataHolder.get(key);
        }

        @Override
        public List<String> keys() {
            return new ArrayList<>(objectDataHolder.keySet());
        }

        @Override
        public void set(String propertyName, Object propertyValue) {
            objectDataHolder.put(propertyName, propertyValue);
        }

        @Override
        public boolean has(String key) {
            return objectDataHolder.containsKey(key);
        }
    }
}
