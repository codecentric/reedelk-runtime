package de.codecentric.reedelk.platform.test.utils;

import de.codecentric.reedelk.runtime.api.component.Join;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;

import java.util.List;

public class TestJoinComponent implements Join {

    private String prop1;
    private long prop2;

    @Override
    public Message apply(FlowContext flowContext, List<Message> inputs) {
        throw new UnsupportedOperationException("Test Only Join");
    }

    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }

    public long getProp2() {
        return prop2;
    }

    public void setProp2(long prop2) {
        this.prop2 = prop2;
    }
}
