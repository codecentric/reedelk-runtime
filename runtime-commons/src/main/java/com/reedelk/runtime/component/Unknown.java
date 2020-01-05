package com.reedelk.runtime.component;

import com.reedelk.runtime.api.annotation.ESBComponent;
import com.reedelk.runtime.api.annotation.Hidden;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.component.Component;

@Hidden
@ESBComponent("Unknown")
public class Unknown implements Component {

    @Property("Unknown component")
    private String implementor;

}
