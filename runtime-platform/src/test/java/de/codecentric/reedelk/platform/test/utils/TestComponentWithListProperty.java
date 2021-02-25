package de.codecentric.reedelk.platform.test.utils;

import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;

import java.util.List;

public class TestComponentWithListProperty implements ProcessorSync {

    private List<String> listStringProperty;
    private List<TestImplementor> listCustomImplementorProperty;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        throw new UnsupportedOperationException("Test Only ProcessorSync");
    }

    public List<String> getListStringProperty() {
        return listStringProperty;
    }

    public void setListStringProperty(List<String> listStringProperty) {
        this.listStringProperty = listStringProperty;
    }

    public List<TestImplementor> getListCustomImplementorProperty() {
        return listCustomImplementorProperty;
    }

    public void setListCustomImplementorProperty(List<TestImplementor> listCustomImplementorProperty) {
        this.listCustomImplementorProperty = listCustomImplementorProperty;
    }
}
