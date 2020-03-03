package com.reedelk.runtime.component;

import com.reedelk.runtime.api.annotation.Description;
import com.reedelk.runtime.api.annotation.Example;
import com.reedelk.runtime.api.annotation.ModuleComponent;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.component.Component;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;

@ModuleComponent("For Each")
@Description("The for each component applies the following flow " +
        "to each element of the input collection.")
public class ForEach implements Component {

    @Property("Collection")
    @Example("<code>message.payload()</code>")
    private DynamicObject collection;

    public DynamicObject getCollection() {
        return collection;
    }

    public void setCollection(DynamicObject collection) {
        this.collection = collection;
    }
}
