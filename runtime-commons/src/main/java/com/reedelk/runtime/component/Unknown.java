package com.reedelk.runtime.component;

import com.reedelk.runtime.api.annotation.ModuleComponent;
import com.reedelk.runtime.api.annotation.Hidden;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.component.Component;

@Hidden
@ModuleComponent("Unknown")
public class Unknown implements Component {

    @Property("Unknown component")
    private String implementor;

}
