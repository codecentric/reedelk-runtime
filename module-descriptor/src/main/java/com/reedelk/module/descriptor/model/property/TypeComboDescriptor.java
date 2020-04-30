package com.reedelk.module.descriptor.model.property;

import com.reedelk.runtime.api.annotation.Combo;

import java.util.Arrays;

public class TypeComboDescriptor implements PropertyTypeDescriptor {

    private static final transient Class<?> type = Combo.class;
    private boolean editable;
    private String prototype;
    private String[] comboValues;

    @Override
    public Class<?> getType() {
        return type;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getPrototype() {
        return prototype;
    }

    public void setPrototype(String prototype) {
        this.prototype = prototype;
    }

    public String[] getComboValues() {
        return comboValues;
    }

    public void setComboValues(String[] comboValues) {
        this.comboValues = comboValues;
    }

    @Override
    public String toString() {
        return "TypeComboDescriptor{" +
                "type=" + type +
                ", editable=" + editable +
                ", prototype='" + prototype + '\'' +
                ", comboValues=" + Arrays.toString(comboValues) +
                '}';
    }
}
