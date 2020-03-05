package com.reedelk.runtime.component;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Component;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;

@ModuleComponent("For Each")
@Description("The For Each component applies to each element in the given collection the components following the For Each processor. " +
        "A Join component can be added right outside the scope of the For Each to merge together all the results. " +
        "If no Join component is present all the results after the execution are collected in a list.")
public class ForEach implements Component {

    @Property("Items collection")
    @InitValue("#[message.payload()]")
    @Example("<code>message.payload()</code>")
    @Description("The collection of items this component should loop through. " +
            "For each item in the collection a new message is created with a payload containing a single item of the collection." +
            " The components following For Each will be executed with every message created out of the collection.")
    private DynamicObject collection;

    public DynamicObject getCollection() {
        return collection;
    }

    public void setCollection(DynamicObject collection) {
        this.collection = collection;
    }
}
