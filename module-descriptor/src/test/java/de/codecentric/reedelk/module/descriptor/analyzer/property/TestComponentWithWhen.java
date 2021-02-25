package de.codecentric.reedelk.module.descriptor.analyzer.property;

import de.codecentric.reedelk.runtime.api.annotation.Property;
import de.codecentric.reedelk.runtime.api.annotation.When;
import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;

public class TestComponentWithWhen implements ProcessorSync {

    @Property("Property 1")
    private String property1;

    @Property("Property 2")
    @When(propertyName = "property1", propertyValue = "ITEM1")
    private String property2;

    @Property("Property 3")
    @When(propertyName = "property1", propertyValue = "ITEM2")
    @When(propertyName = "property2", propertyValue = "ITEM4")
    private String property3;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        throw new UnsupportedOperationException("not supposed to be called");
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    public void setProperty3(String property3) {
        this.property3 = property3;
    }
}
