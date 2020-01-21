package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.annotation.When;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;

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
    public Message apply(Message message, FlowContext flowContext) {
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
