package de.codecentric.reedelk.module.descriptor.model.property;

import java.io.Serializable;

public class WhenDescriptor implements Serializable {

    private String propertyName;
    private String propertyValue;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Override
    public String toString() {
        return "WhenDescriptor{" +
                "propertyName='" + propertyName + '\'' +
                ", propertyValue='" + propertyValue + '\'' +
                '}';
    }
}
