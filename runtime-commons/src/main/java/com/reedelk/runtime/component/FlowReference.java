package com.reedelk.runtime.component;


import com.reedelk.runtime.api.annotation.ESBComponent;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.component.Component;

@ESBComponent("Flow Reference")
public class FlowReference implements Component {

    @Property("Subflow Reference")
    private String ref;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

}
