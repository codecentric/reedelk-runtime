package com.reedelk.module.descriptor.model.property;

import com.reedelk.runtime.api.annotation.ListInputType;

import java.util.List;

public class ListDescriptor extends CollectionAwareDescriptor {

    private static final transient Class<?> TYPE = List.class;

    private PropertyTypeDescriptor valueType;
    private String listDisplayProperty;
    private String hintBrowseFile;
    private ListInputType.ListInput listInput;

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

    public void setListInput(ListInputType.ListInput listInput) {
        this.listInput = listInput;
    }

    public ListInputType.ListInput getListInput() {
        return listInput;
    }

    public String getHintBrowseFile() {
        return hintBrowseFile;
    }

    public void setHintBrowseFile(String hintBrowseFile) {
        this.hintBrowseFile = hintBrowseFile;
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
