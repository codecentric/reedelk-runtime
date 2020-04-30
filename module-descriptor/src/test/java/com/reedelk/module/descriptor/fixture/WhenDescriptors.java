package com.reedelk.module.descriptor.fixture;

import com.reedelk.module.descriptor.model.commons.WhenDescriptor;

public class WhenDescriptors {

    private WhenDescriptors() {
    }

    public static final WhenDescriptor whenDescriptorDefault = newWhenDescriptor("myProperty", "VALUE1");
    public static final WhenDescriptor whenDescriptorAlternative = newWhenDescriptor("anotherProperty", "VALUE2");

    private static WhenDescriptor newWhenDescriptor(String propertyName, String propertyValue) {
        WhenDescriptor whenDescriptor = new WhenDescriptor();
        whenDescriptor.setPropertyName(propertyName);
        whenDescriptor.setPropertyValue(propertyValue);
        return whenDescriptor;
    }
}
