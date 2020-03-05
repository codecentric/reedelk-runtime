package com.reedelk.runtime.component;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Component;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;

@ModuleComponent("For Each")
@Description("The for each component applies the following flow " +
        "to each element of the input collection.")
public class ForEach implements Component {

    @Property("Items collection")
    @InitValue("#[message.payload()]")
    @Example("<code>message.payload()</code>")
    @Description("The collection of items this component should loop through. " +
            "For each item in the collection a new message is created with payload a single item of the collection " +
            "and the elements following this component will be executed with every new message.")
    private DynamicObject collection;

    public DynamicObject getCollection() {
        return collection;
    }

    public void setCollection(DynamicObject collection) {
        this.collection = collection;
    }
}
