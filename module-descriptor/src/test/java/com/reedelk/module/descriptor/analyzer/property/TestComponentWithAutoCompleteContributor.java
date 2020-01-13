package com.reedelk.module.descriptor.analyzer.property;

import com.reedelk.runtime.api.annotation.AutoCompleteContributor;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.component.ProcessorSync;
import com.reedelk.runtime.api.message.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;

public class TestComponentWithAutoCompleteContributor implements ProcessorSync {

    @AutoCompleteContributor(contributions = {
            "messages[VARIABLE:Message[]]",
            "messages.size()[FUNCTION:int]"})
    @Property("Property with custom autocomplete contributions")
    private DynamicString propertyWithCustomContributions;

    @AutoCompleteContributor(message = false)
    private DynamicString propertyWithoutMessageContributions;

    @AutoCompleteContributor(context = false)
    private DynamicString propertyWithoutContextContributions;

    @AutoCompleteContributor(message = false, error = true)
    private DynamicString propertyWithErrorAndWithoutMessageContributions;

    private DynamicString propertyWithoutAutoCompleteContributor;

    @Override
    public Message apply(Message message, FlowContext flowContext) {
        throw new UnsupportedOperationException("not supposed to be called");
    }

    public void setPropertyWithCustomContributions(DynamicString propertyWithCustomContributions) {
        this.propertyWithCustomContributions = propertyWithCustomContributions;
    }

    public void setPropertyWithoutMessageContributions(DynamicString propertyWithoutMessageContributions) {
        this.propertyWithoutMessageContributions = propertyWithoutMessageContributions;
    }

    public void setPropertyWithoutContextContributions(DynamicString propertyWithoutContextContributions) {
        this.propertyWithoutContextContributions = propertyWithoutContextContributions;
    }

    public void setPropertyWithErrorAndWithoutMessageContributions(DynamicString propertyWithErrorAndWithoutMessageContributions) {
        this.propertyWithErrorAndWithoutMessageContributions = propertyWithErrorAndWithoutMessageContributions;
    }

    public void setPropertyWithoutAutoCompleteContributor(DynamicString propertyWithoutAutoCompleteContributor) {
        this.propertyWithoutAutoCompleteContributor = propertyWithoutAutoCompleteContributor;
    }
}
