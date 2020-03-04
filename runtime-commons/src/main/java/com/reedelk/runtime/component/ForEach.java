package com.reedelk.runtime.component;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Component;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;

@ModuleComponent("For Each")
@Description("The for each component applies the following flow " +
        "to each element of the input collection.")
public class ForEach implements Component {

    @Property("Collection")
    @InitValue("#[message.payload()]")
    @Example("<code>message.payload()</code>")
    private DynamicObject collection;

    public DynamicObject getCollection() {
        return collection;
    }

    public void setCollection(DynamicObject collection) {
        this.collection = collection;
    }
}
