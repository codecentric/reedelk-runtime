package de.codecentric.reedelk.runtime.component;


import de.codecentric.reedelk.runtime.api.annotation.Description;
import de.codecentric.reedelk.runtime.api.annotation.Example;
import de.codecentric.reedelk.runtime.api.annotation.ModuleComponent;
import de.codecentric.reedelk.runtime.api.annotation.Property;
import de.codecentric.reedelk.runtime.api.component.Component;

@ModuleComponent("Flow Reference")
@Description("Executes a given subflow within the current flow. " +
                "The subflow shares the same context of the calling flow. " +
                "When completed the result Message of its execution is sent back " +
                "to the original flow continuing its execution.")
public class FlowReference implements Component {

    @Property("Subflow Reference")
    @Description("The id of the referenced subflow")
    @Example("1edc7e0d-6be9-46d9-8fa4-d64e3ea21de1")
    private String ref;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
