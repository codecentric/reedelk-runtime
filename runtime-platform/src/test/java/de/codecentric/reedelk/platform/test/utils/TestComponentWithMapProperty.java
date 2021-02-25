package de.codecentric.reedelk.platform.test.utils;

import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;

import java.util.Map;

public class TestComponentWithMapProperty implements ProcessorSync {

    private Map<String, String> mapStringStringProperty;
    private Map<String, TestImplementor> mapStringCustomImplementorProperty;

    @Override
    public Message apply(FlowContext flowContext, Message message) {
        throw new UnsupportedOperationException("Test Only ProcessorSync");
    }

    public Map<String, String> getMapStringStringProperty() {
        return mapStringStringProperty;
    }

    public void setMapStringStringProperty(Map<String, String> mapStringStringProperty) {
        this.mapStringStringProperty = mapStringStringProperty;
    }

    public Map<String, TestImplementor> getMapStringCustomImplementorProperty() {
        return mapStringCustomImplementorProperty;
    }

    public void setMapStringCustomImplementorProperty(Map<String, TestImplementor> mapStringCustomImplementorProperty) {
        this.mapStringCustomImplementorProperty = mapStringCustomImplementorProperty;
    }
}
