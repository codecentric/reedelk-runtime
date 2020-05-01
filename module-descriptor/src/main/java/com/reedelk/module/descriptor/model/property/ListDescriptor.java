package com.reedelk.module.descriptor.model.property;

import java.util.List;

public class ListDescriptor extends CollectionAwareDescriptor {

    private static final transient Class<?> TYPE = List.class;

    private PropertyTypeDescriptor valueType;
    private String listDisplayProperty;

    @Override
    public Class<?> getType() {
        return TYPE;
    }

    public void setValueType(PropertyTypeDescriptor valueType) {
        this.valueType = valueType;
    }

    public PropertyTypeDescriptor getValueType() {
        return valueType;
    }

    public String getListDisplayProperty() {
        return listDisplayProperty;
    }

    public void setListDisplayProperty(String listDisplayProperty) {
        this.listDisplayProperty = listDisplayProperty;
    }

    @Override
    public String toString() {
        return "ListDescriptor{" +
                "valueType=" + valueType +
                ", listDisplayProperty='" + listDisplayProperty + '\'' +
                ", tabGroup='" + tabGroup + '\'' +
                ", dialogTitle='" + dialogTitle + '\'' +
                '}';
    }
}
