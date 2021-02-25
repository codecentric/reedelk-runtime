package de.codecentric.reedelk.module.descriptor.model.property;

import java.util.Map;

public class MapDescriptor extends CollectionAwareDescriptor {

    private static final transient Class<?> TYPE = Map.class;

    protected String keyName;
    protected String valueName;
    protected PropertyTypeDescriptor valueType;

    @Override
    public Class<?> getType() {
        return TYPE;
    }

    public PropertyTypeDescriptor getValueType() {
        return valueType;
    }

    public void setValueType(PropertyTypeDescriptor valueType) {
        this.valueType = valueType;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    @Override
    public String toString() {
        return "MapDescriptor{" +
                "keyName='" + keyName + '\'' +
                ", valueName='" + valueName + '\'' +
                ", valueType=" + valueType +
                ", tabGroup='" + tabGroup + '\'' +
                ", dialogTitle='" + dialogTitle + '\'' +
                '}';
    }
}
